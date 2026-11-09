
package com.smartkrishi.advisor;

import com.smartkrishi.advisor.entity.CropDemand;
import com.smartkrishi.advisor.repository.CropDemandRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CropDemandController {

    @Autowired
    private CropDemandRepository cropDemandRepository;

    @GetMapping("/buyer/crop-demand")
    public String showDemandForm(HttpSession session) {

        if (session.getAttribute("buyerId") == null) {
            return "redirect:/buyer/login";
        }

        return "crop-demand";
    }

    @PostMapping("/buyer/crop-demand")
    public String submitDemand(
            @RequestParam String cropName,
            @RequestParam double quantity,
            @RequestParam String unit,
            @RequestParam String location,
            HttpSession session,
            Model model) {

        Long buyerId = (Long) session.getAttribute("buyerId");

        if (buyerId == null) {
            return "redirect:/buyer/login";
        }

        if (quantity <= 0) {
            model.addAttribute("message",
                    "Quantity must be greater than zero.");
            return "crop-demand";
        }

        CropDemand demand = new CropDemand();

        demand.setBuyerId(buyerId);
        demand.setCropName(cropName);
        demand.setQuantity(quantity);
        demand.setUnit(unit);
        demand.setLocation(location);

        cropDemandRepository.save(demand);

        model.addAttribute("message",
                "Crop demand submitted successfully!");

        return "crop-demand";
    }
}
