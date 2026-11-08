
package com.smartkrishi.advisor;

import com.smartkrishi.advisor.repository.ProduceRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BuyerProduceController {

    @Autowired
    private ProduceRepository produceRepository;

    @GetMapping("/buyer/produce")
    public String viewProduce(
            HttpSession session, Model model) {

        if (session.getAttribute("buyerId") == null) {
            return "redirect:/buyer/login";
        }

        model.addAttribute(
                "produceList",
                produceRepository.findAllByOrderByIdDesc()
        );

        return "buyer-produce";
    }
}
