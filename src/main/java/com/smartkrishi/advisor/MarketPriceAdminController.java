package com.smartkrishi.advisor;

import com.smartkrishi.advisor.entity.MarketPrice;
import com.smartkrishi.advisor.repository.MarketPriceRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MarketPriceAdminController {

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    @GetMapping("/admin/market-prices")
    public String showMarketPriceForm(
            @RequestParam(required = false) Long editId,
            HttpSession session, Model model) {

        if (!Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            return "redirect:/admin/login";
        }

        if (editId != null) {
            marketPriceRepository.findById(editId).ifPresent(item -> {
                model.addAttribute("editItem", item);
            });
        }

        model.addAttribute("prices", marketPriceRepository.findAll());
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
            RedirectAttributes redirectAttributes) {

        if (!Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            return "redirect:/admin/login";
        }

        if (price <= 0 || cropName.isBlank() || marketName.isBlank() || district.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Please fill all fields. Price must be greater than zero.");
            return "redirect:/admin/market-prices";
        }

        MarketPrice marketPrice = new MarketPrice();
        marketPrice.setCropName(cropName.trim());
        marketPrice.setMarketName(marketName.trim());
        marketPrice.setDistrict(district.trim());
        marketPrice.setPrice(price);
        marketPrice.setUnit(unit.trim());

        marketPriceRepository.save(marketPrice);

        redirectAttributes.addFlashAttribute("message",
                "Market reference price added successfully!");

        return "redirect:/admin/market-prices";
    }

    @PostMapping("/admin/market-prices/edit/{id}")
    public String updateMarketPrice(
            @PathVariable Long id,
            @RequestParam String cropName,
            @RequestParam String marketName,
            @RequestParam String district,
            @RequestParam double price,
            @RequestParam String unit,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            return "redirect:/admin/login";
        }

        MarketPrice existing = marketPriceRepository.findById(id).orElse(null);
        if (existing == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Market price record not found!");
            return "redirect:/admin/market-prices";
        }

        if (price <= 0 || cropName.isBlank() || marketName.isBlank() || district.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Please provide valid inputs. Price must be greater than zero.");
            return "redirect:/admin/market-prices?editId=" + id;
        }

        existing.setCropName(cropName.trim());
        existing.setMarketName(marketName.trim());
        existing.setDistrict(district.trim());
        existing.setPrice(price);
        existing.setUnit(unit.trim());

        marketPriceRepository.save(existing);

        redirectAttributes.addFlashAttribute("message",
                "Market price updated successfully!");

        return "redirect:/admin/market-prices";
    }

    @PostMapping("/admin/market-prices/delete/{id}")
    public String deleteMarketPrice(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            return "redirect:/admin/login";
        }

        if (marketPriceRepository.existsById(id)) {
            marketPriceRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message",
                    "Market price record deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Market price record not found!");
        }

        return "redirect:/admin/market-prices";
    }
}
