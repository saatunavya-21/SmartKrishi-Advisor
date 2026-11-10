package com.smartkrishi.advisor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {

    @GetMapping("/contact")
    public String showContactPage() {
        return "contact";
    }

    @PostMapping("/contact")
    public String submitContactForm(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String message,
            Model model) {

        if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty() || message == null || message.trim().isEmpty()) {
            model.addAttribute("errorMessage", "Please provide your Name, Email, and Message.");
            return "contact";
        }

        model.addAttribute("successMessage", "Thank you, " + name.trim() + "! Your inquiry has been received. Our agricultural support team will contact you shortly.");
        return "contact";
    }
}

