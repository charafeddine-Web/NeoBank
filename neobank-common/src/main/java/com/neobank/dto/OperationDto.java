package com.neobank.dto;

import com.neobank.enums.OperationStatus;
import com.neobank.enums.OperationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationDto {
    private Long id;

    @NotNull
    @Positive
    private BigDecimal amount;

    private String currency;

    @NotNull
    private OperationType type;

    private OperationStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime validatedAt;

    private LocalDateTime executedAt;

    @NotNull
    private Long accountId;

    private Long accountDestinationId;

    private Long validationId;

    private String idempotencyKey;
}
