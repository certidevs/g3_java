package com.demo.controller;

import com.demo.model.Booking;
import com.demo.model.House;
import com.demo.model.User;
import com.demo.repository.HouseRepository;
import com.demo.repository.ReviewRepository;
import com.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class AgendaController {

    private final UserService userService;

    // Obtiene la tabla entera

    @GetMapping("agenda")
    public String showAgenda(Model model) {

        List<User> agenda = userService.getAgendaUsers();
        model.addAttribute("agenda", agenda);

        return "user/agenda";
    }

    // Para filtrar
    // FILTRADO
    @GetMapping("agenda-filter")
    public String listaFiltrada(Model model,
                                @RequestParam(required = false) String textfind) {


        List<User> agenda = userService.getAgendaUsers(textfind);
        model.addAttribute("agenda", agenda);

        return "user/agenda";

    }

}
