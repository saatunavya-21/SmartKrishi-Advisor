package com.smartkrishi.advisor;

import com.smartkrishi.advisor.repository.FarmerRepository;
import com.smartkrishi.advisor.repository.FeedbackRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(
            HttpSession session, Model model) {

        if (!Boolean.TRUE.equals(
                session.getAttribute("adminLoggedIn"))) {
            return "redirect:/admin/login";
        }

        model.addAttribute(
                "totalFarmers",
                farmerRepository.count());

        model.addAttribute(
                "totalFeedback",
                feedbackRepository.count());

        model.addAttribute(
                "farmers",
                farmerRepository.findAll());

        model.addAttribute(
                "feedbackList",
                feedbackRepository.findAll());

        return "admin-dashboard";
    }
}