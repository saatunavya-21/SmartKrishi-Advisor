
package com.smartkrishi.advisor;

import com.smartkrishi.advisor.entity.Produce;
import com.smartkrishi.advisor.repository.ProduceRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProduceController {

    @Autowired
    private ProduceRepository produceRepository;

    @GetMapping("/produce/add")
    public String showProduceForm(HttpSession session) {

        if (session.getAttribute("farmerId") == null) {
            return "redirect:/login";
        }

        return "add-produce";
    }

    @PostMapping("/produce/add")
    public String addProduce(
            @RequestParam String cropName,
            @RequestParam double quantity,
            @RequestParam String unit,
            @RequestParam double expectedPrice,
            @RequestParam String quality,
            @RequestParam String location,
            HttpSession session,
            Model model) {

        Long farmerId = (Long) session.getAttribute("farmerId");

        if (farmerId == null) {
            return "redirect:/login";
        }

        if (quantity <= 0 || expectedPrice <= 0) {
            model.addAttribute("message",
                    "Quantity and price must be greater than zero.");
            return "add-produce";
        }

        Produce produce = new Produce();

        produce.setFarmerId(farmerId);
        produce.setCropName(cropName);
        produce.setQuantity(quantity);
        produce.setUnit(unit);
        produce.setExpectedPrice(expectedPrice);
        produce.setQuality(quality);
        produce.setLocation(location);

        produceRepository.save(produce);

        model.addAttribute("message",
                "Produce listed successfully!");

        return "add-produce";
    }
}
