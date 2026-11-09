
package com.smartkrishi.advisor;

import com.smartkrishi.advisor.entity.MarketPrice;
import com.smartkrishi.advisor.repository.MarketPriceRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MarketPriceAdminController {

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    @GetMapping("/admin/market-prices")
    public String showMarketPriceForm(
            HttpSession session, Model model) {

        if (!Boolean.TRUE.equals(
                session.getAttribute("adminLoggedIn"))) {
            return "redirect:/admin/login";
        }

        model.addAttribute("prices",
                marketPriceRepository.findAll());

        return "admin-market-prices";
    }

    @PostMapping("/admin/market-prices")
    public String addMarketPrice(
            @RequestParam String cropName,
            @RequestParam String marketName,
            @RequestParam String district,
            @RequestParam double price,
            @RequestParam String unit,
            HttpSession session,
            Model model) {

        if (!Boolean.TRUE.equals(
                session.getAttribute("adminLoggedIn"))) {
            return "redirect:/admin/login";
        }

        if (price <= 0) {
            model.addAttribute("message",
                    "Price must be greater than zero.");
            model.addAttribute("prices",
                    marketPriceRepository.findAll());
            return "admin-market-prices";
        }

        MarketPrice marketPrice = new MarketPrice();

        marketPrice.setCropName(cropName);
        marketPrice.setMarketName(marketName);
        marketPrice.setDistrict(district);
        marketPrice.setPrice(price);
        marketPrice.setUnit(unit);

        marketPriceRepository.save(marketPrice);

        model.addAttribute("message",
                "Market price added successfully!");

        model.addAttribute("prices",
                marketPriceRepository.findAll());

        return "admin-market-prices";
    }
}
