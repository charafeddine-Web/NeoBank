package com.neobank.controller.web;

import com.neobank.dto.AccountDto;
import com.neobank.dto.OperationCreateDto;
import com.neobank.service.AccountService;
import com.neobank.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientWebController {

    private final AccountService accountService;
    private final OperationService operationService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        String email = authentication.getName();
        model.addAttribute("accounts", accountService.listAccountsForUser(email));
        model.addAttribute("operations", operationService.listOperationsForUser(email));
        return "client/dashboard";
    }

    @GetMapping("/operations/new")
    public String newOperationForm(Model model) {
        model.addAttribute("operation", new OperationCreateDto());
        return "client/operation-form";
    }

    @PostMapping("/operations/new")
    public String createOperation(@ModelAttribute OperationCreateDto operationDto, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            operationService.createOperation(operationDto, authentication.getName());
            redirectAttributes.addFlashAttribute("success", "Operation created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating operation: " + e.getMessage());
        }
        return "redirect:/client/dashboard";
    }

    @GetMapping("/operations/{id}/upload")
    public String uploadDocumentForm(@PathVariable Long id, Model model) {
        model.addAttribute("operationId", id);
        return "client/upload-document";
    }

    @PostMapping("/operations/{id}/upload")
    public String uploadDocument(@PathVariable Long id, @RequestParam("file") MultipartFile file, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload.");
            return "redirect:/client/operations/" + id + "/upload";
        }
        try {
            operationService.uploadDocument(id, file.getOriginalFilename(), file.getContentType(), file.getBytes(), authentication.getName());
            redirectAttributes.addFlashAttribute("success", "Document uploaded successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload document: " + e.getMessage());
            return "redirect:/client/operations/" + id + "/upload";
        }
        return "redirect:/client/dashboard";
    }
}
