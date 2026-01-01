package com.neobank.controller.web;

import com.neobank.entity.Document;
import com.neobank.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/agent")
@RequiredArgsConstructor
public class AgentWebController {

    private final OperationService operationService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        model.addAttribute("pendingOperations", operationService.listPendingOperations());
        return "agent/dashboard";
    }

    @PostMapping("/operations/{id}/approve")
    public String approveOperation(@PathVariable Long id, @RequestParam(required = false) String comment, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            operationService.approveOperation(id, authentication.getName(), comment);
            redirectAttributes.addFlashAttribute("success", "Operation approved.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error approving operation: " + e.getMessage());
        }
        return "redirect:/agent/dashboard";
    }

    @PostMapping("/operations/{id}/reject")
    public String rejectOperation(@PathVariable Long id, @RequestParam(required = false) String comment, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            operationService.rejectOperation(id, authentication.getName(), comment);
            redirectAttributes.addFlashAttribute("success", "Operation rejected.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error rejecting operation: " + e.getMessage());
        }
        return "redirect:/agent/dashboard";
    }

    @GetMapping("/operations/{id}/document")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> viewDocument(@PathVariable Long id) {
        Document doc = operationService.getDocumentForOperation(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.getFilename() + "\"")
                .body(new ByteArrayResource(doc.getContent()));
    }
}
