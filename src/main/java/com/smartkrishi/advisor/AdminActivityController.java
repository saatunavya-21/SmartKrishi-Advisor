
package com.smartkrishi.advisor;

import com.smartkrishi.advisor.repository.FarmerRepository;
import com.smartkrishi.advisor.repository.ProduceRepository;
import com.smartkrishi.advisor.repository.CropDemandRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminActivityController {

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private ProduceRepository produceRepository;

    @Autowired
    private CropDemandRepository cropDemandRepository;

    @GetMapping("/admin/activity")
    public String showActivity(
            HttpSession session, Model model) {

        if (!Boolean.TRUE.equals(
                session.getAttribute("adminLoggedIn"))) {
            return "redirect:/admin/login";
        }

        model.addAttribute(
                "farmers",
                farmerRepository.findAll()
        );

        model.addAttribute(
                "produceList",
                produceRepository.findAllByOrderByIdDesc()
        );

        model.addAttribute(
                "demandList",
                cropDemandRepository.findAll()
        );

        return "admin-activity";
    }
}
