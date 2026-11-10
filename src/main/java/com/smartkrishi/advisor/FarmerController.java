package com.smartkrishi.advisor;

import com.smartkrishi.advisor.entity.Farmer;
import com.smartkrishi.advisor.repository.FarmerRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class FarmerController {

    @Autowired
    private FarmerRepository farmerRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("farmer", new Farmer());
        return "register";
    }

    public String registerFarmer(Farmer farmer, Model model) {
        return registerFarmer(farmer, null, model);
    }

    @PostMapping("/register")
    public String registerFarmer(
            @ModelAttribute Farmer farmer,
            @RequestParam(required = false) String confirmPassword,
            Model model) {

        if (farmer.getEmail() == null || farmer.getEmail().trim().isEmpty() ||
            farmer.getPassword() == null || farmer.getPassword().trim().isEmpty() ||
            farmer.getName() == null || farmer.getName().trim().isEmpty()) {
            model.addAttribute("errorMessage", "All required fields must be filled!");
            return "register";
        }

        if (confirmPassword != null && !confirmPassword.trim().isEmpty()) {
            if (!farmer.getPassword().equals(confirmPassword)) {
                model.addAttribute("errorMessage", "Passwords do not match! Please verify your password.");
                return "register";
            }
        }

        String email = farmer.getEmail().trim();
        if (farmerRepository.findByEmail(email).isPresent()) {
            model.addAttribute("errorMessage",
                    "This email is already registered! Please log in or use another email.");
            return "register";
        }

        farmer.setEmail(email);
        farmer.setName(farmer.getName().trim());
        if (farmer.getFullName() == null || farmer.getFullName().trim().isEmpty()) {
            farmer.setFullName(farmer.getName());
        }
        farmer.setPassword(
            passwordEncoder.encode(farmer.getPassword())
        );

        farmerRepository.save(farmer);

        model.addAttribute("message",
                "Farmer registered successfully! You can now log in.");

        return "register";
    }
}
