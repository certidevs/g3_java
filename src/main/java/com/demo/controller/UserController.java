package com.demo.controller;

import com.demo.model.Role;
import com.demo.model.User;
import com.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", userService.getByIdOrThrow(user.getId()));
        return "users/user-detail";
    }

    @GetMapping("/users/{id}")
    public String detailUser(Model model, @PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        if (currentUser.getRole() != Role.ROLE_ADMIN && currentUser.getId().equals(id)) {
            return "redirect:/profile";
        }
        User user = userService.getByIdOrThrow(id);
        user.setPassword(null);
        model.addAttribute("user", user);
        return "users/user-detail";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        model.addAttribute("edit", false);
        return "users/user-form";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(Model model, @PathVariable Long id, @AuthenticationPrincipal User currentUser,
                           RedirectAttributes redirectAttributes) {
        if (currentUser.getRole() != Role.ROLE_ADMIN && !currentUser.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para esto");
            return "redirect:/profile";
        }
        User user = userService.getByIdOrThrow(id);
        user.setPassword(null);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("edit", true);
        return "users/user-form";
    }

    @PostMapping("/users")
    public String save(@ModelAttribute("userForm") User userForm,
                       @AuthenticationPrincipal User currentUser,
                       RedirectAttributes redirectAttributes) {
        if (currentUser.getRole() != Role.ROLE_ADMIN && !currentUser.getId().equals(userForm.getId())) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para esto");
            return "redirect:/profile";
        }

        try {
            User user;
            if (userForm.getId() == null) {
                user = userService.create(userForm, currentUser);
                redirectAttributes.addFlashAttribute("message", "Usuario creado");
            } else {
                user = userService.update(userForm, currentUser);
                redirectAttributes.addFlashAttribute("message", "Usuario actualizado");
            }
            return "redirect:/users/" + user.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/users/edit/" + userForm.getId();
        }

    }
}
