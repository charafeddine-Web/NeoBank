package com.neobank.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {

    private Long id;

    private String accountNumber;

    @NotNull
    @PositiveOrZero
    private BigDecimal balance;

    @NotNull
    private Long userId;

    private List<Long> operationIds;

    private Long version;
}
