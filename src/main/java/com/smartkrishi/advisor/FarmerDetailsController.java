
package com.smartkrishi.advisor;

import com.smartkrishi.advisor.entity.Farmer;
import com.smartkrishi.advisor.repository.FarmerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FarmerDetailsController {

    @Autowired
    private FarmerRepository farmerRepository;

    @GetMapping("/admin/farmer/{id}")
    public String viewFarmer(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        if (!Boolean.TRUE.equals(
                session.getAttribute("adminLoggedIn"))) {
            return "redirect:/admin/login";
        }

        Farmer farmer = farmerRepository.findById(id).orElse(null);

        if (farmer == null) {
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("farmer", farmer);

        return "admin-farmer-details";
    }
}
