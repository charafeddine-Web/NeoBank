package com.neobank.controller;

import com.neobank.dto.OperationResponseDto;
import com.neobank.dto.OperationValidationDto;
import com.neobank.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/agent/operations")
@RequiredArgsConstructor
public class AgentOperationController {

    private final OperationService operationService;

    @GetMapping("/test")
    public Authentication test(Authentication auth) {
        return auth;
    }


    @GetMapping("/pending")
    public ResponseEntity<List<OperationResponseDto>> listPending() {
        return ResponseEntity.ok(operationService.listPendingOperations());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<OperationResponseDto> approve(@PathVariable Long id, Principal principal, @RequestBody(required = false) OperationValidationDto request) {
        String comment = request != null ? request.getComment() : null;
        OperationResponseDto dto = operationService.approveOperation(id, principal.getName(), comment);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<OperationResponseDto> reject(@PathVariable Long id, Principal principal, @RequestBody(required = false) OperationValidationDto request) {
        String comment = request != null ? request.getComment() : null;
        OperationResponseDto dto = operationService.rejectOperation(id, principal.getName(), comment);
        return ResponseEntity.ok(dto);
    }
}

