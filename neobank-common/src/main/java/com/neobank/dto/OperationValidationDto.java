package com.neobank.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationValidationDto {
    private Long id;

    @NotNull
    private boolean approved;

    @Size(max = 1000)
    private String comment;

    private LocalDateTime validatedAt;

    private Long agentId;

    @NotNull
    private Long operationId;
}
