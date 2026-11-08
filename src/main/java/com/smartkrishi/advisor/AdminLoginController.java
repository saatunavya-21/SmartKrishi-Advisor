package com.smartkrishi.advisor;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminLoginController {

    @Value("${admin.email:admin@smartkrishi.com}")
    private String configuredAdminEmail;

    @Value("${admin.password:Admin@123}")
    private String configuredAdminPassword;

    @GetMapping("/admin/login")
    public String showAdminLogin() {
        return "admin-login";
    }

    @PostMapping("/admin/login")
    public String adminLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        String envEmail = System.getenv("ADMIN_EMAIL");
        String envPassword = System.getenv("ADMIN_PASSWORD");

        String expectedEmail = (envEmail != null && !envEmail.isBlank()) ? envEmail : configuredAdminEmail;
        String expectedPassword = (envPassword != null && !envPassword.isBlank()) ? envPassword : configuredAdminPassword;

        if (expectedEmail != null && expectedPassword != null
                && expectedEmail.equalsIgnoreCase(email.trim())
                && expectedPassword.equals(password)) {

            session.setAttribute("adminLoggedIn", true);

            return "redirect:/admin/dashboard";
        }

        model.addAttribute(
                "message", "Invalid administrator credentials!");

        return "admin-login";
    }

    @GetMapping("/admin/logout")
    public String adminLogout(HttpSession session) {

        session.removeAttribute("adminLoggedIn");
        session.invalidate();

        return "redirect:/admin/login";
    }
}