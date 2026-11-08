package com.smartkrishi.advisor;

import com.smartkrishi.advisor.entity.Buyer;
import com.smartkrishi.advisor.repository.BuyerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BuyerController {

    @Autowired
    private BuyerRepository buyerRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @GetMapping("/buyer/register")
    public String showBuyerRegistration(Model model) {
        model.addAttribute("buyer", new Buyer());
        return "buyer-register";
    }

    @PostMapping("/buyer/register")
    public String registerBuyer(
            @ModelAttribute Buyer buyer,
            Model model) {

        if (buyer.getEmail() == null || buyer.getEmail().trim().isEmpty() ||
            buyer.getPassword() == null || buyer.getPassword().trim().isEmpty() ||
            buyer.getName() == null || buyer.getName().trim().isEmpty()) {
            model.addAttribute("errorMessage", "All required fields must be filled!");
            return "buyer-register";
        }

        String email = buyer.getEmail().trim();
        if (buyerRepository.findByEmail(email).isPresent()) {
            model.addAttribute("errorMessage",
                    "This email is already registered! Please log in or use another email.");
            return "buyer-register";
        }

        buyer.setEmail(email);
        buyer.setName(buyer.getName().trim());
        buyer.setPassword(
                passwordEncoder.encode(buyer.getPassword()));

        buyerRepository.save(buyer);

        model.addAttribute("message",
                "Buyer registered successfully! You can now log in.");

        return "buyer-register";
    }
}
