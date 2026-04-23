package com.demo.controller;

import com.demo.model.House;
import com.demo.repository.HouseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HouseController {

    private final HouseRepository houseRepository;

    public HouseController(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @GetMapping("houses")
    public String house(Model model){
        List <House>  houses = houseRepository.findAll();
        model.addAttribute("houses", houses);
        return "house-list";

    }


}
