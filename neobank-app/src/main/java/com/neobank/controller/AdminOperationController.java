package com.neobank.controller;

import com.neobank.dto.OperationResponseDto;
import com.neobank.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/admin/operations")
@RequiredArgsConstructor
public class AdminOperationController {

    private final OperationService operationService;

    @GetMapping
    public ResponseEntity<List<OperationResponseDto>> listOperations(
            @RequestParam(value = "status", required = false) String status,
            Principal principal) {

        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));

        List<OperationResponseDto> list;

        if (isAdmin && "PENDING".equalsIgnoreCase(status)) {
            list = operationService.listPendingOperations();
        } else if (isAdmin) {
            list = operationService.listAllOperations();
        } else {
            list = operationService.listOperationsForUser(principal.getName());
        }

        return ResponseEntity.ok(list);
    }



    @GetMapping("/{id}")
    public ResponseEntity<OperationResponseDto> getOperation(@PathVariable Long id) {
        OperationResponseDto dto = operationService.getOperation(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/force-approve")
    public ResponseEntity<OperationResponseDto> forceApprove(@PathVariable Long id,Principal principal, @RequestBody(required = false) String comment) {
        Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getAuthorities()
                .forEach(a -> System.out.println("AUTHORITY = " + a.getAuthority()));

        OperationResponseDto dto = operationService.approveOperation(id, principal.getName(), comment);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/force-reject")
    public ResponseEntity<OperationResponseDto> forceReject(@PathVariable Long id, @RequestBody(required = false) String comment) {
        OperationResponseDto dto = operationService.rejectOperation(id, "admin", comment);
        return ResponseEntity.ok(dto);
    }


}

