package com.neobank.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDto {
    private Long id;

    @NotBlank
    private String filename;

    @NotBlank
    private String fileType;

    private String storagePath;

    private LocalDateTime uploadedAt;

    private Long operationId;
}
