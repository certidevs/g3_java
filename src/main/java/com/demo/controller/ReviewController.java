package com.demo.controller;

import com.demo.model.Review;
import com.demo.model.User;
import com.demo.repository.HouseRepository;
import com.demo.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@RequestMapping("/reviews")
@Controller
@AllArgsConstructor
class ReviewController {
    private final ReviewService reviewService;
    private final HouseRepository houseRepository;

    @GetMapping // /reviews
    public String reviewList(Model model) {
        List<Review> reviews = reviewService.findAll();
        model.addAttribute("reviews", reviews);
        return "review/review-list";
    }

    @GetMapping("/{id}")
    public String review(Model model, @PathVariable Long id) {
        model.addAttribute("review", reviewService.findById(id).orElseThrow());
        return "review/review-detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Review> reviewOptional = reviewService.findById(id);
        if (reviewOptional.isPresent()) {
            reviewService.delete(reviewOptional.get());
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
        Optional<Review> reviewOptional = reviewService.findById(id);
        if (reviewOptional.isPresent()) {
            model.addAttribute("review", reviewOptional.get());
            return "review/review-form";
        }
        redirectAttributes.addFlashAttribute("message", "Review not found.");
        return "redirect:/reviews";
    }

    @PostMapping
    public String saveReview(@ModelAttribute Review review, @AuthenticationPrincipal User currentUser, RedirectAttributes redirectAttributes) {
        // TODO reviewService.isValid(review)
        //         si no es correcta redirectAttributes.addFlashAttribute("review_error", "Tu review no cumple los terminos y condiciones de la plataforma");
        review.setUser(currentUser);
        reviewService.save(review);
        redirectAttributes.addFlashAttribute("review_message", "La reseña se ha guardado correctamente.");

        return "redirect:/houses/" + review.getHouse().getId() + "#reviews";
    }

}
