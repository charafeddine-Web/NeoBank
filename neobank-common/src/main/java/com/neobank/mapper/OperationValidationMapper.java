package com.neobank.mapper;

import com.neobank.dto.OperationValidationDto;
import com.neobank.entity.OperationValidation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OperationValidationMapper {

    @Mapping(source = "agent.id", target = "agentId")
    @Mapping(source = "operation.id", target = "operationId")
    OperationValidationDto toDto(OperationValidation validation);

    @Mapping(source = "agentId", target = "agent.id")
    @Mapping(source = "operationId", target = "operation.id")
    OperationValidation toEntity(OperationValidationDto dto);
}

