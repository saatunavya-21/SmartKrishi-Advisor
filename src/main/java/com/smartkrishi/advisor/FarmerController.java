
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
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerFarmer(@ModelAttribute Farmer farmer,
                                 Model model) {

        farmer.setPassword(
            passwordEncoder.encode(farmer.getPassword())
        );

        farmerRepository.save(farmer);

        model.addAttribute("message",
                "Farmer registered successfully!");

        return "register";
    }
}
