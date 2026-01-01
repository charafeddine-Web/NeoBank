package com.neobank.service;

import com.neobank.dto.OperationCreateDto;
import com.neobank.dto.OperationResponseDto;
import com.neobank.entity.Document;
import java.security.Principal;
import java.util.List;

public interface OperationService {

    OperationResponseDto createOperation(OperationCreateDto dto,  String email);

    OperationResponseDto getOperation(Long id);

    List<OperationResponseDto> listOperationsForUser(String username);

    List<OperationResponseDto> listPendingOperations();

    OperationResponseDto approveOperation(Long id, String agentUsername, String comment);

    List<OperationResponseDto> listAllOperations();

    OperationResponseDto rejectOperation(Long id, String agentUsername, String comment);

    void uploadDocument(Long operationId, String filename, String contentType, byte[] content, String username);

    Document getDocumentForOperation(Long operationId);
}
