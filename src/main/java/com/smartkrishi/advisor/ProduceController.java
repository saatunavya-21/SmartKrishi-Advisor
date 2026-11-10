package com.smartkrishi.advisor;

import com.smartkrishi.advisor.entity.Produce;
import com.smartkrishi.advisor.repository.ProduceRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProduceController {

    @Autowired
    private ProduceRepository produceRepository;

    @GetMapping("/produce/add")
    public String showProduceForm(HttpSession session, Model model) {

        Long farmerId = (Long) session.getAttribute("farmerId");
        if (farmerId == null) {
            return "redirect:/login";
        }

        List<Produce> myProduceList = produceRepository.findByFarmerId(farmerId);
        model.addAttribute("myProduceList", myProduceList);

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
            RedirectAttributes redirectAttributes) {

        Long farmerId = (Long) session.getAttribute("farmerId");

        if (farmerId == null) {
            return "redirect:/login";
        }

        if (cropName.isBlank() || location.isBlank() || quantity <= 0 || expectedPrice <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Please fill all fields. Quantity and expected price must be greater than zero.");
            return "redirect:/produce/add";
        }

        Produce produce = new Produce();
        produce.setFarmerId(farmerId);
        produce.setCropName(cropName.trim());
        produce.setQuantity(quantity);
        produce.setUnit(unit.trim());
        produce.setExpectedPrice(expectedPrice);
        produce.setQuality(quality.trim());
        produce.setLocation(location.trim());

        produceRepository.save(produce);

        redirectAttributes.addFlashAttribute("message",
                "Produce listed successfully!");

        return "redirect:/produce/add";
    }

    @PostMapping("/produce/delete/{id}")
    public String deleteMyProduce(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Long farmerId = (Long) session.getAttribute("farmerId");
        if (farmerId == null) {
            return "redirect:/login";
        }

        Produce produce = produceRepository.findById(id).orElse(null);
        if (produce != null && produce.getFarmerId().equals(farmerId)) {
            produceRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message",
                    "Produce listing removed successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Produce listing not found or unauthorized!");
        }

        return "redirect:/produce/add";
    }
}
