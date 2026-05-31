package com.demo.controller;

import com.demo.model.Review;
import com.demo.repository.HouseRepository;
import com.demo.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@RequestMapping("/reviews")
@Controller
@RequiredArgsConstructor
class ReviewController {
    private final ReviewRepository reviewRepository;
    private final HouseRepository houseRepository;

    @GetMapping // /reviews
    public String reviewList(Model model) {
        List<Review> reviews = reviewRepository.findAll();
        model.addAttribute("reviews", reviews);
        return "review/review-list";
    }

    @GetMapping("/{id}")
    public String review(Model model, @PathVariable Long id) {
        model.addAttribute("review", reviewRepository.findById(id).orElseThrow());
        return "review/review-detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isPresent()) {
            reviewRepository.delete(reviewOptional.get());
            redirectAttributes.addFlashAttribute("message", "Review deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Review not found.");
        }
        return "redirect:/reviews";
    }

    @GetMapping("/new")
    public String newReview(Model model, @RequestParam Long houseId) {
        Review review = new Review();

        if (houseId != null)
            review.setHouse(houseRepository.findById(houseId).orElseThrow());
        model.addAttribute("review", review);
        return "review/review-form";
    }

    @GetMapping("/edit/{id}")
    public String editReview(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isPresent()) {
            model.addAttribute("review", reviewOptional.get());
            return "review/review-form";
        }
        redirectAttributes.addFlashAttribute("message", "Review not found.");
        return "redirect:/reviews";
    }

    @PostMapping
    public String saveReview(@ModelAttribute Review review, RedirectAttributes redirectAttributes) {
        // TODO reviewService.isValid(review)
        //         si no es correcta redirectAttributes.addFlashAttribute("review_error", "Tu review no cumple los terminos y condiciones de la plataforma");
        reviewRepository.save(review);
        redirectAttributes.addFlashAttribute("review_message", "La reseña se ha guardado correctamente.");

        return "redirect:/houses/" + review.getHouse().getId() + "#reviews";
    }

}
