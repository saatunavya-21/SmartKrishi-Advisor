
package com.smartkrishi.advisor;

import com.smartkrishi.advisor.repository.MarketPriceRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BuyerMarketPriceController {

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    @GetMapping("/buyer/market-prices")
    public String showBuyerMarketPrices(
            HttpSession session, Model model) {

        if (session.getAttribute("buyerId") == null) {
            return "redirect:/buyer/login";
        }

        model.addAttribute("prices",
                marketPriceRepository.findAll());

        return "buyer-market-prices";
    }
}
