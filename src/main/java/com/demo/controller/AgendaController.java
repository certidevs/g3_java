package com.demo.controller;

import com.demo.model.User;
import com.demo.repository.HouseRepository;
import com.demo.repository.ReviewRepository;
import com.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class AgendaController {

    private final UserRepository userRepository;

    @GetMapping("agenda")
    public String showAgenda(Model model) {

        List<User> agenda = userRepository.findAll();

        model.addAttribute("agenda",agenda);

        return "/guest/agenda";


    }


}
