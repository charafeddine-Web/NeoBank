package com.neobank.dto;

import com.neobank.enums.OperationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationCreateDto {
    @NotNull
    private OperationType type;

    @NotNull
    @Positive
    private java.math.BigDecimal amount;
    private String sourceAccountNumber;
    private String destinationAccountNumber;

    private String currency;


}

