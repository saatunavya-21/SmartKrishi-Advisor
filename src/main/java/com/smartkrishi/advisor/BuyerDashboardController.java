
package com.smartkrishi.advisor;

import com.smartkrishi.advisor.entity.Buyer;
import com.smartkrishi.advisor.repository.BuyerRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BuyerDashboardController {

    @Autowired
    private BuyerRepository buyerRepository;

    @GetMapping("/buyer/dashboard")
    public String showBuyerDashboard(
            HttpSession session, Model model) {

        Long buyerId = (Long) session.getAttribute("buyerId");

        if (buyerId == null) {
            return "redirect:/buyer/login";
        }

        Buyer buyer = buyerRepository.findById(buyerId).orElse(null);

        if (buyer == null) {
            session.removeAttribute("buyerId");
            session.removeAttribute("buyerName");
            return "redirect:/buyer/login";
        }

        model.addAttribute("buyerName", buyer.getName());

        return "buyer-dashboard";
    }
}
