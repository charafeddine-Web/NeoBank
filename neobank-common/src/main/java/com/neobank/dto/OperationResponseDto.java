package com.neobank.dto;

import com.neobank.enums.OperationStatus;
import com.neobank.enums.OperationType;
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
public class OperationResponseDto {
    private Long id;
    private OperationType type;
    private BigDecimal amount;
    private String currency;
    private OperationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime validatedAt;
    private Long validationId;
    private LocalDateTime executedAt;
    private Long accountId;
    private Long accountDestinationId;
}

