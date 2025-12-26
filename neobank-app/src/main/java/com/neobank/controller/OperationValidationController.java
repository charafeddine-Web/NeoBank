package com.neobank.controller;

import com.neobank.dto.OperationValidationDto;
import com.neobank.service.OperationValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/operation-validations")
@RequiredArgsConstructor
public class OperationValidationController {

    private final OperationValidationService service;

    @GetMapping("/operation/{operationId}")
    public ResponseEntity<OperationValidationDto> getByOperation(
            @PathVariable Long operationId) {

        return ResponseEntity.ok(service.getByOperationId(operationId));
    }

    @GetMapping
    public ResponseEntity<List<OperationValidationDto>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<OperationValidationDto>> listByAgent(
            @PathVariable Long agentId) {

        return ResponseEntity.ok(service.listByAgent(agentId));
    }
}
