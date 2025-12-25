package com.neobank.controller;

import com.neobank.dto.OperationCreateDto;
import com.neobank.dto.OperationResponseDto;
import com.neobank.service.OperationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/client/operations")
@RequiredArgsConstructor
public class ClientOperationController {

    private final OperationService operationService;

    @PostMapping
    public ResponseEntity<OperationResponseDto> createOperation(@Valid @RequestBody OperationCreateDto dto,  Principal principal) {
        OperationResponseDto resp = operationService.createOperation(dto,principal.getName());
        return ResponseEntity.status(201).body(resp);
    }

    @PostMapping("/{id}/document")
    public ResponseEntity<Void> uploadDocument(@PathVariable Long id, @RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        byte[] bytes = file.getBytes();
        operationService.uploadDocument(id, file.getOriginalFilename(), file.getContentType(), bytes, principal.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<OperationResponseDto>> listOperations(Principal principal) {

        if (principal == null) {
            throw new RuntimeException("User not authenticated");
        }
        System.out.println("USERNAME FROM TOKEN = " + principal.getName());

        List<OperationResponseDto> list =
                operationService.listOperationsForUser(principal.getName());
        return ResponseEntity.ok(list);
    }


//    @GetMapping("/{id}")
//    public ResponseEntity<OperationResponseDto> getOperation(@PathVariable Long id, Principal principal) {
//        OperationResponseDto dto = operationService.getOperation(id, principal.getName());
//        return ResponseEntity.ok(dto);
//    }
}
