package com.demo.controller;

import com.demo.model.Review;
import com.demo.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@RequestMapping("/reviews")
@Controller
@RequiredArgsConstructor
class ReviewController {
    private final ReviewRepository reviewRepository;

    @GetMapping // /reviews
    public String reviewList(Model model) {
        // List<Review> reviews = reviewRepository.findAll();
        List<Review> reviews = reviewRepository.findAllByActiveTrue();
        model.addAttribute("reviews", reviews);
        return "review/review-list";
    }

    @GetMapping("/deactivate/{id}")
    public String reviewDeactivate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            if (review.getActive() != false) {
                review.setActive(false);
                reviewRepository.save(review);
                redirectAttributes.addFlashAttribute("message", "Review deactivated successfully.");
            } else {
                redirectAttributes.addFlashAttribute("message", "Review is already deactivated.");
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Review not found.");
        }
        return "redirect:/reviews";
    }
}
