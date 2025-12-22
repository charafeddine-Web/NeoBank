package com.neobank.mapper;

import com.neobank.dto.AccountDto;
import com.neobank.entity.Account;
import com.neobank.entity.Operation;
import com.neobank.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "operationIds", source = "operations")
    AccountDto toDto(Account account);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "operations", source = "operationIds")
    Account toEntity(AccountDto dto);

    // utility methods used by MapStruct
    default List<Long> operationsToIds(List<Operation> operations) {
        if (operations == null) return null;
        return operations.stream().map(Operation::getId).collect(Collectors.toList());
    }

    default List<Operation> idsToOperations(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> {
            Operation o = new Operation();
            o.setId(id);
            return o;
        }).collect(Collectors.toList());
    }

    default User userFromId(Long id) {
        if (id == null) return null;
        User u = new User();
        u.setId(id);
        return u;
    }

    default Long userToId(User user) {
        if (user == null) return null;
        return user.getId();
    }
}
