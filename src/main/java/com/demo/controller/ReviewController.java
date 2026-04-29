package com.demo.controller;

import com.demo.model.Review;
import com.demo.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/reviews")
@Controller
@RequiredArgsConstructor
class ReviewController {
    private final ReviewRepository reviewRepository;

    @GetMapping // /reviews
    public String list(Model model) {
        List<Review> reviews = reviewRepository.findAll();
        model.addAttribute("reviews", reviews);
        return "review/review-list";
    }
}
