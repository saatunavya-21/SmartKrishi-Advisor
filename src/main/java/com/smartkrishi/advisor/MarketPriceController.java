
package com.smartkrishi.advisor;

import com.smartkrishi.advisor.repository.MarketPriceRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MarketPriceController {

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    @GetMapping("/market-prices")
    public String showMarketPrices(
            HttpSession session, Model model) {

        if (session.getAttribute("farmerId") == null) {
            return "redirect:/login";
        }

        model.addAttribute("prices",
                marketPriceRepository.findAll());

        return "market-prices";
    }
}
