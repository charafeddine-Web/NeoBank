package com.neobank.mapper;

import com.neobank.dto.DocumentDto;
import com.neobank.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    @Mapping(source = "operation.id", target = "operationId")
    DocumentDto toDto(Document document);

    @Mapping(source = "operationId", target = "operation.id")
    Document toEntity(DocumentDto dto);
}

