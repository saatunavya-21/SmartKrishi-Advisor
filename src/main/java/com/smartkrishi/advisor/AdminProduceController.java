
package com.smartkrishi.advisor;

import com.smartkrishi.advisor.repository.ProduceRepository;
import com.smartkrishi.advisor.repository.CropDemandRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminProduceController {

    @Autowired
    private ProduceRepository produceRepository;

    @Autowired
    private CropDemandRepository cropDemandRepository;


    // Delete Farmer Produce
    @PostMapping("/admin/delete-produce/{id}")
    public String deleteProduce(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!Boolean.TRUE.equals(
                session.getAttribute("adminLoggedIn"))) {

            return "redirect:/admin/login";
        }

        if (produceRepository.existsById(id)) {

            produceRepository.deleteById(id);

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Produce listing deleted successfully!"
            );

        } else {

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Produce listing not found!"
            );
        }

        return "redirect:/admin/activity";
    }


    // Delete Buyer Demand
    @PostMapping("/admin/delete-demand/{id}")
    public String deleteDemand(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!Boolean.TRUE.equals(
                session.getAttribute("adminLoggedIn"))) {

            return "redirect:/admin/login";
        }

        if (cropDemandRepository.existsById(id)) {

            cropDemandRepository.deleteById(id);

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Buyer demand deleted successfully!"
            );

        } else {

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Buyer demand not found!"
            );
        }

        return "redirect:/admin/activity";
    }

}
