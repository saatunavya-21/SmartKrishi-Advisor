package com.smartkrishi.advisor;

import com.smartkrishi.advisor.entity.Farmer;
import com.smartkrishi.advisor.repository.FarmerRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @Autowired
    private FarmerRepository farmerRepository;

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {

        Long farmerId = (Long) session.getAttribute("farmerId");

        if (farmerId == null) {
            return "redirect:/login";
        }

        Farmer farmer = farmerRepository.findById(farmerId).orElse(null);

        if (farmer == null) {
            session.invalidate();
            return "redirect:/login";
        }

        model.addAttribute("farmer", farmer);

        return "profile";
    }
}