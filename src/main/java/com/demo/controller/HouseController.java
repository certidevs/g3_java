package com.demo.controller;

import com.demo.model.House;
import com.demo.model.Review;
import com.demo.model.StatusReserva;
import com.demo.repository.HouseRepository;
import com.demo.repository.ReviewRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller

public class HouseController {

    private final HouseRepository houseRepository;
    private final ReviewRepository reviewRepository;

    public HouseController(HouseRepository houseRepository, ReviewRepository reviewRepository) {
        this.houseRepository = houseRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/")
    public String home(Model model){
//        List<House>  houses = houseRepository.findAll();
//        model.addAttribute("houses", houses);
        return "home/index";

    }

    @GetMapping("/houses")
    public String houseList(Model model,
        @RequestParam(required = false)StatusReserva reserve,//se agg el reques param para filtrar por parametro de reserva
        @RequestParam(required = false) Double pricePerNight,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String province

    ){
//      List <House>  houses = houseRepository.findAll();
        List<House>  houseStatus = houseRepository.findByReserve(reserve,pricePerNight,title,province);//NUEVO METODO POR QUERY

        // lista de provincias para el select
        List<String> provinces = Arrays.asList("Madrid", "Barcelona", "Valencia", "Sevilla", "Málaga", "Bilbao");

        model.addAttribute("houses", houseStatus);
        model.addAttribute("provinces", provinces);
        model.addAttribute("selectedProvince", province);

        List <House>  houses = houseRepository.findByActiveTrue();
//        model.addAttribute("houses", houses);
        return "house/house-list";

    }

    @GetMapping("houses/disable/{id}")
    public String houseDisable(@PathVariable Long id,Model model){
        Optional<House> houseOptional = houseRepository.findById(id);
        if (houseOptional.isPresent()) {

            // casa sí existe
            House house = houseOptional.get();
            house.setActive(false);
            house.setReserve(StatusReserva.valueOf("NO_DISPONIBLE"));
            houseRepository.save(house);

        }

        return "redirect:/houses";

    }

    // nuevo metodo para traer un solo restaurante por su id
    @GetMapping("houses/{id}")
    public String houseDetail(@PathVariable Long id, Model model) {

        // buscar restaurante por su id: findById
        Optional<House> houseOptional = houseRepository.findById(id);
        if (houseOptional.isPresent()) {

            // casa sí existe
            House house = houseOptional.get();
            model.addAttribute("house", house);

            // cargar los platos (Dish) de este restaurant en el model
//            List<Dish> platos = dishRepository.findByRestaurantIdOrderByPrice(restaurant.getId());
//            model.addAttribute("dishes", platos);

            // reviews
            List<Review> reviews = reviewRepository.findByHouse_IdOrderByCreatedAtDesc(house.getId());
            model.addAttribute("reviews", reviews); // accesibles desde HTML

            return "house/house-detail";

        }


        return "redirect:/house";
    }

//ruta para entrar al formulario de casa
    @GetMapping("houses/new")
    public String newHouses(Model model) {
        //objeto vacío para el formulario
        model.addAttribute(("house"), new House());

    return "house/house-form";
}

@PostMapping ("houses")
public String createHouse (@ModelAttribute House house) {
    //guardar en la base de datos
    System.out.println("recibido" + house);
    houseRepository.save(house);
    return "redirect:/houses/" + house.getId();
}
}
