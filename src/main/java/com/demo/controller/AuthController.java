package com.demo.controller;


import com.demo.dto.RegisterForm;
import com.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class AuthController {
    private final UserService userService;

    // Navegar a formulario de registro
    // @GetMapping /register
    @GetMapping("register")
    public String register(Model model) {
        // opcion 1: entidad User
        // model.addAttribute("user", new User());
        // opcion 2: dto UserRegisterDTO
        model.addAttribute("user", new RegisterForm()); // UserRegisterDTO o RegisterForm
        return "auth/register";
    }

    // @PostMapping /register
    @PostMapping("register")
    public String register(@ModelAttribute("user") RegisterForm form, Model model, RedirectAttributes redirectAttributes) {
        try {
            userService.register(form);
            redirectAttributes.addFlashAttribute("message", "Cuenta creada correctamente, inicia sesión.");
            redirectAttributes.addFlashAttribute("registeredUsername", form.getUsername());
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("login")
    public String login() {
        return "auth/login";
    }
    // NO hace falta
    // @PostMapping /login   porque Spring Security lo hace automaticamente
    // @PostMapping /logout   Spring security lo hace automatico


}
