package com.neobank.service.impl;

import com.neobank.dto.OperationValidationDto;
import com.neobank.entity.OperationValidation;
import com.neobank.mapper.OperationValidationMapper;
import com.neobank.repository.OperationValidationRepository;
import com.neobank.service.OperationValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OperationValidationServiceImpl implements OperationValidationService {

    private final OperationValidationRepository repository;
    private final OperationValidationMapper mapper;

    @Override
    public OperationValidationDto getByOperationId(Long operationId) {
        OperationValidation v = repository.findByOperationId(operationId)
                .orElseThrow(() -> new RuntimeException("Validation not found"));
        return mapper.toDto(v);
    }

    @Override
    public List<OperationValidationDto> listAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OperationValidationDto> listByAgent(Long agentId) {
        return repository.findByAgentId(agentId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
