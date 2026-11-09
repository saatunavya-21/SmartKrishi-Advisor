
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
    public String showBuyerRegistration() {
        return "buyer-register";
    }

    @PostMapping("/buyer/register")
    public String registerBuyer(
            @ModelAttribute Buyer buyer,
            Model model) {

        if (buyerRepository.findByEmail(buyer.getEmail()).isPresent()) {
            model.addAttribute("message",
                    "This email is already registered!");
            return "buyer-register";
        }

        buyer.setPassword(
                passwordEncoder.encode(buyer.getPassword()));

        buyerRepository.save(buyer);

        model.addAttribute("message",
                "Buyer registered successfully!");

        return "buyer-register";
    }
}
