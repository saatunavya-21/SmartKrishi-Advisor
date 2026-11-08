package com.smartkrishi.advisor;

import com.smartkrishi.advisor.entity.CropDemand;
import com.smartkrishi.advisor.repository.CropDemandRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CropDemandController {

    @Autowired
    private CropDemandRepository cropDemandRepository;

    @GetMapping("/buyer/crop-demand")
    public String showDemandForm(HttpSession session, Model model) {

        Long buyerId = (Long) session.getAttribute("buyerId");

        if (buyerId == null) {
            return "redirect:/buyer/login";
        }

        List<CropDemand> myDemands = cropDemandRepository.findByBuyerId(buyerId);
        model.addAttribute("myDemands", myDemands);

        return "crop-demand";
    }

    @PostMapping("/buyer/crop-demand")
    public String submitDemand(
            @RequestParam String cropName,
            @RequestParam double quantity,
            @RequestParam String unit,
            @RequestParam String location,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Long buyerId = (Long) session.getAttribute("buyerId");

        if (buyerId == null) {
            return "redirect:/buyer/login";
        }

        if (cropName.isBlank() || location.isBlank() || quantity <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Please fill all fields. Quantity must be greater than zero.");
            return "redirect:/buyer/crop-demand";
        }

        CropDemand demand = new CropDemand();
        demand.setBuyerId(buyerId);
        demand.setCropName(cropName.trim());
        demand.setQuantity(quantity);
        demand.setUnit(unit.trim());
        demand.setLocation(location.trim());

        cropDemandRepository.save(demand);

        redirectAttributes.addFlashAttribute("message",
                "Crop demand submitted successfully!");

        return "redirect:/buyer/crop-demand";
    }

    @PostMapping("/buyer/crop-demand/delete/{id}")
    public String deleteMyDemand(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Long buyerId = (Long) session.getAttribute("buyerId");
        if (buyerId == null) {
            return "redirect:/buyer/login";
        }

        CropDemand demand = cropDemandRepository.findById(id).orElse(null);
        if (demand != null && demand.getBuyerId().equals(buyerId)) {
            cropDemandRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message",
                    "Crop requirement removed successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Crop demand not found or unauthorized!");
        }

        return "redirect:/buyer/crop-demand";
    }
}
