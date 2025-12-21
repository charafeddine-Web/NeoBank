package com.neobank.mapper;

import com.neobank.dto.RegisterRequest;
import com.neobank.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", expression = "java(dto.getRole() != null ? dto.getRole() : com.neobank.enums.Role.CLIENT)")
    User toEntity(RegisterRequest dto);
    RegisterRequest toDto(User user);

}
