package com.neobank.mapper;

import com.neobank.dto.OperationDto;
import com.neobank.entity.Account;
import com.neobank.entity.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OperationMapper {

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "accountDestination.id", target = "accountDestinationId")
    @Mapping(source = "validation.id", target = "validationId")
    OperationDto toDto(Operation operation);

    @Mapping(source = "accountId", target = "account.id")
    @Mapping(source = "accountDestinationId", target = "accountDestination.id")
    Operation toEntity(OperationDto dto);

    default Account accountFromId(Long id) {
        if (id == null) return null;
        Account a = new Account();
        a.setId(id);
        return a;
    }

}
