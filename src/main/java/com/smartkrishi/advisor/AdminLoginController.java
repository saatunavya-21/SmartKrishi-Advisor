package com.smartkrishi.advisor;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminLoginController {

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

        String adminEmail = System.getenv("ADMIN_EMAIL");
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        System.out.println("Admin email configured: " + (adminEmail != null));
        System.out.println("Admin password configured: " + (adminPassword != null));
        if (adminEmail != null && adminPassword != null
                && adminEmail.equals(email)
                && adminPassword.equals(password)) {

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

        return "redirect:/admin/login";
    }
}