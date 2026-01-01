package com.neobank.service.impl;

import com.neobank.dto.OperationCreateDto;
import com.neobank.dto.OperationResponseDto;
import com.neobank.entity.*;
import com.neobank.enums.OperationStatus;
import com.neobank.enums.OperationType;
import com.neobank.enums.Role;
import com.neobank.mapper.OperationMapper;
import com.neobank.repository.*;
import com.neobank.service.OperationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import com.neobank.exception.AccountNotFoundException;
import com.neobank.exception.InsufficientBalanceException;
import com.neobank.exception.OperationNotAllowedException;
import com.neobank.exception.UserNotFoundException;
import com.neobank.exception.OperationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;
    private final AccountRepository accountRepository;
    private final DocumentRepository documentRepository;
    private final EntityManager em;
    private final OperationValidationRepository operationValidationRepository;
    private final UserRepository userRepository;
    private final OperationMapper operationMapper;

    private static final BigDecimal THRESHOLD = new BigDecimal("10000");


    @Override
    @Transactional
    public OperationResponseDto createOperation(OperationCreateDto dto,  String email) {

        if (dto.getType() == null) throw new IllegalArgumentException("Operation type required");
        if (dto.getAmount() == null || dto.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be positive");

        Account account = accountRepository.findByAccountNumber(dto.getSourceAccountNumber()).orElseThrow(() -> new AccountNotFoundException("Account not found: " + dto.getSourceAccountNumber()));

        if (!account.getUser().getEmail().equals(email)) {
            throw new OperationNotAllowedException("Unauthorized operation on this account");
        }

        Account dest = null;
        if (dto.getType() == OperationType.TRANSFER ) {
            dest = accountRepository.findByAccountNumber(dto.getDestinationAccountNumber()).orElseThrow(() -> new AccountNotFoundException("Destination account not found: " + dto.getDestinationAccountNumber()));
            if (dest.getId().equals(account.getId())) throw new IllegalArgumentException("Destination account must differ");
        }

        Operation op = new Operation();
        op.setAmount(dto.getAmount());
        op.setCurrency(dto.getCurrency() == null ? "MAD" : dto.getCurrency());
        op.setType(dto.getType());
        op.setAccount(account);
        op.setAccountDestination(dest);
        op.setCreatedAt(LocalDateTime.now());

        if (dto.getAmount().compareTo(THRESHOLD) <= 0) {
            em.lock(account, LockModeType.PESSIMISTIC_WRITE);
            if (dto.getType() == OperationType.WITHDRAWAL || dto.getType() == OperationType.TRANSFER) {
                if (account.getBalance().compareTo(dto.getAmount()) < 0) {
                    throw new InsufficientBalanceException("Insufficient funds for operation");
                }
                account.setBalance(account.getBalance().subtract(dto.getAmount()));
            } else if (dto.getType() == OperationType.DEPOSIT) {
                account.setBalance(account.getBalance().add(dto.getAmount()));
            }
            if (dest != null) {
                em.lock(dest, LockModeType.PESSIMISTIC_WRITE);
                dest.setBalance(dest.getBalance().add(dto.getAmount()));
            }
            op.setStatus(OperationStatus.VALIDATED);
            op.setValidatedAt(LocalDateTime.now());
            op.setExecutedAt(LocalDateTime.now());
            accountRepository.save(account);
            if (dest != null) accountRepository.save(dest);
        } else {
            op.setStatus(OperationStatus.PENDING);
        }

        Operation saved = operationRepository.save(op);
        return operationMapper.toDto(saved);
    }

    @Override
    public OperationResponseDto getOperation(Long id) {
        Operation o = operationRepository.findById(id).orElseThrow(() -> new OperationNotFoundException("Operation not found with ID: " + id));
        return operationMapper.toDto(o);
    }

    @Override
    public List<OperationResponseDto> listAllOperations() {
        return operationRepository.findAll()
                .stream()
                .map(operationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OperationResponseDto> listOperationsForUser(String email) {
        return operationRepository
                .findByAccount_User_Email(email)
                .stream()
                .map(operationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OperationResponseDto> listPendingOperations() {
        return operationRepository.findAll().stream().filter(o -> o.getStatus() == OperationStatus.PENDING).map(operationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OperationResponseDto approveOperation(Long id, String agentUsername, String comment) {
        User agent = userRepository.findByEmail(agentUsername)
                .orElseThrow(() -> new UserNotFoundException("Agent not found: " + agentUsername));


        if (!agent.getRole().equals(Role.ADMIN)
                && !agent.getRole().equals(Role.AGENT_BANCAIRE)) {
            throw new OperationNotAllowedException("Only agent or admin can validate operations");
        }
        Operation op = operationRepository.findById(id).orElseThrow(() -> new OperationNotFoundException("Operation not found: " + id));
        if (op.getStatus() != OperationStatus.PENDING) throw new IllegalStateException("Operation not pending");

        Account account = accountRepository.findById(op.getAccount().getId()).orElseThrow(() -> new AccountNotFoundException("Account not found: " + op.getAccount().getId()));
        Account dest = null;
        if (op.getAccountDestination() != null) dest = accountRepository.findById(op.getAccountDestination().getId()).orElseThrow(() -> new AccountNotFoundException("Destination not found: " + op.getAccountDestination().getId()));

        em.lock(account, LockModeType.PESSIMISTIC_WRITE);
        if (op.getType() == OperationType.WITHDRAWAL || op.getType() == OperationType.TRANSFER) {
            if (account.getBalance().compareTo(op.getAmount()) < 0) {
                throw new InsufficientBalanceException("Insufficient funds at approval time");
            }
            account.setBalance(account.getBalance().subtract(op.getAmount()));
        } else if (op.getType() == OperationType.DEPOSIT) {
            account.setBalance(account.getBalance().add(op.getAmount()));
        }
        if (dest != null) {
            em.lock(dest, LockModeType.PESSIMISTIC_WRITE);
            dest.setBalance(dest.getBalance().add(op.getAmount()));
            accountRepository.save(dest);
        }
        accountRepository.save(account);

        op.setStatus(OperationStatus.APPROVED);
        op.setValidatedAt(LocalDateTime.now());
        op.setExecutedAt(LocalDateTime.now());


        OperationValidation validation = new OperationValidation();
        validation.setApproved(true);
        validation.setComment(comment);
        validation.setValidatedAt(LocalDateTime.now());
        validation.setAgent(agent);
        validation.setOperation(op);
        operationValidationRepository.save(validation);
        Operation saved = operationRepository.save(op);
        return operationMapper.toDto(saved);
    }

    @Override
    @Transactional
    public OperationResponseDto rejectOperation(Long id, String agentUsername, String comment) {

        User agent = userRepository.findByEmail(agentUsername)
                .orElseThrow(() -> new UserNotFoundException("Agent not found: " + agentUsername));

        Operation op = operationRepository.findById(id).orElseThrow(() -> new OperationNotFoundException("Operation not found: " + id));
        if (op.getStatus() != OperationStatus.PENDING) throw new IllegalStateException("Operation not pending");

        if (!agent.getRole().equals(Role.ADMIN)
                && !agent.getRole().equals(Role.AGENT_BANCAIRE)) {
            throw new OperationNotAllowedException("Only agent or admin can validate operations");
        }

        op.setStatus(OperationStatus.REJECTED);
        op.setValidatedAt(LocalDateTime.now());
        Operation saved = operationRepository.save(op);

        OperationValidation validation = new OperationValidation();
        validation.setApproved(false);
        validation.setComment(comment);
        validation.setValidatedAt(LocalDateTime.now());
        validation.setAgent(agent);
        validation.setOperation(op);
        operationValidationRepository.save(validation);

        return operationMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void uploadDocument(Long operationId, String filename, String contentType, byte[] content, String username) {
        Operation op = operationRepository.findById(operationId).orElseThrow(() -> new OperationNotFoundException("Operation not found: " + operationId));
        if (content == null || content.length == 0) throw new IllegalArgumentException("File empty");
        if (content.length > 5 * 1024 * 1024) throw new IllegalArgumentException("File too large");
        if (contentType == null || !(contentType.equals("application/pdf") || contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            throw new IllegalArgumentException("Invalid file type");
        }
        try {
            Document d = new Document();
            d.setFilename(filename);
            d.setFileType(contentType);
            d.setStoragePath("/files/" + System.currentTimeMillis() + "-" + filename);
            d.setContent(content);
            d.setUploadedAt(LocalDateTime.now());
            d.setOperation(op);
            documentRepository.save(d);
        } catch (Exception e) {
            throw new RuntimeException("Failed to store document", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Document getDocumentForOperation(Long operationId) {
        Document doc = documentRepository.findByOperation_Id(operationId)
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Document not found for operation: " + operationId));
        
        if (doc.getContent() != null) {
            int length = doc.getContent().length;
        }
        
        return doc;
    }


}
