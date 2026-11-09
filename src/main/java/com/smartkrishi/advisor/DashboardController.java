
package com.smartkrishi.advisor;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {

        if (session.getAttribute("farmerId") == null) {
            return "redirect:/login";
        }

        model.addAttribute("farmerName",
                session.getAttribute("farmerName"));

        return "dashboard";
    }
}