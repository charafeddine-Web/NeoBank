package com.neobank.mapper;

import com.neobank.dto.AuthResponse;
import com.neobank.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "token", ignore = true)
    @Mapping(target = "tokenType", constant = "Bearer")
    AuthResponse toAuthResponse(User user);
}
