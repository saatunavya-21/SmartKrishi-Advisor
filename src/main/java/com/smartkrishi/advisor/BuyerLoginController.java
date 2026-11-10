
package com.smartkrishi.advisor;

import com.smartkrishi.advisor.entity.Buyer;
import com.smartkrishi.advisor.repository.BuyerRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BuyerLoginController {

    @Autowired
    private BuyerRepository buyerRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @GetMapping("/buyer/login")
    public String showBuyerLogin() {
        return "buyer-login";
    }

    @PostMapping("/buyer/login")
    public String buyerLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Buyer buyer = buyerRepository.findByEmail(email).orElse(null);

        if (buyer != null &&
                passwordEncoder.matches(password, buyer.getPassword())) {

            session.setAttribute("buyerId", buyer.getId());
            session.setAttribute("buyerName", buyer.getName());

            return "redirect:/buyer/dashboard";
        }

        model.addAttribute("message",
                "Invalid email or password!");

        return "buyer-login";
    }

    @GetMapping("/buyer/logout")
    public String buyerLogout(HttpSession session) {
        session.removeAttribute("buyerId");
        session.removeAttribute("buyerName");
        session.invalidate();

        return "redirect:/buyer/login";
    }
}
