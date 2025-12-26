package com.neobank.service;

import com.neobank.dto.OperationValidationDto;

import java.util.List;

public interface OperationValidationService {

    OperationValidationDto getByOperationId(Long operationId);

    List<OperationValidationDto> listAll();

    List<OperationValidationDto> listByAgent(Long agentId);

}
