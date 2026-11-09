package com.smartkrishi.advisor;

import com.smartkrishi.advisor.entity.Feedback;
import com.smartkrishi.advisor.repository.FeedbackRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @GetMapping("/feedback")
    public String showFeedbackForm(HttpSession session) {

        if (session.getAttribute("farmerId") == null) {
            return "redirect:/login";
        }

        return "feedback";
    }

    @PostMapping("/feedback")
    public String submitFeedback(
            @RequestParam String category,
            @RequestParam int rating,
            @RequestParam String comments,
            HttpSession session,
            Model model) {

        Long farmerId = (Long) session.getAttribute("farmerId");

        if (farmerId == null) {
            return "redirect:/login";
        }

        if (rating < 1 || rating > 5) {
            model.addAttribute("message", "Please select a rating from 1 to 5.");
            return "feedback";
        }

        Feedback feedback = new Feedback();

        feedback.setFarmerId(farmerId);
        feedback.setCategory(category);
        feedback.setRating(rating);
        feedback.setComments(comments);

        feedbackRepository.save(feedback);

        model.addAttribute("message",
                "Thank you! Your feedback was submitted successfully.");

        return "feedback";
    }
}