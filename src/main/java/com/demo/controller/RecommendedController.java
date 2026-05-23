package com.demo.controller;

import com.demo.model.House;
import com.demo.model.HouseRecommended;
import com.demo.model.User;
import com.demo.repository.BookingRepository;
import com.demo.repository.HouseRecommendedRepository;
import com.demo.repository.HouseRepository;
import com.demo.repository.UserRepository;
import com.demo.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class RecommendedController {

    private final HouseRepository houseRepository;
    private final UserRepository userRepository;
    private final HouseRecommendedRepository houseRecommendedRepository;

    public RecommendedController(
                             HouseRepository houseRepository,
                             UserRepository userRepository,
                             HouseRecommendedRepository houseRecommendedRepository) {
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
        this.houseRecommendedRepository = houseRecommendedRepository;
    }


    @GetMapping("recommended/{token}/{idHouse}/{idUsuario}")
    public String createRecommended (Model model,
                         @PathVariable String token,
                         @PathVariable Long idHouse,
                         @PathVariable Long idUsuario) {

        Optional<House> house = houseRepository.findById(idHouse);
        Optional<User> user = userRepository.findById(idUsuario);

        if (house.isPresent() && user.isPresent()) {

            House houseValid = house.get();
            User userValid = user.get();

            HouseRecommended recommendation = new HouseRecommended();

            recommendation.setHouseRecommended(houseValid);
            recommendation.setTokenFrom(token);
            // Datos usuario que crea la recomendacion
            recommendation.setUserRecommended(userValid);
            recommendation.setFirstNameFrom(userValid.getFirstName());
            recommendation.setLastNameFrom(userValid.getLastName());
            recommendation.setEmailFrom(userValid.getEmail());

            recommendation.setEmailFrom(userValid.getEmail());

            model.addAttribute("recommendation",recommendation);

            return "/guest/house-recommended";
        }
        else
        {
            return "redirect:/booking/" + idUsuario.toString();
        }

    }

    @PostMapping("recommended")
    public String addRecommendation (@ModelAttribute HouseRecommended houseRecommended,
                                     RedirectAttributes redirectAttributes)
                                      {

        Boolean bok = false;

        houseRecommended.setId(null);

        // Si no tiene token ni email
        String emailto = houseRecommended.getEmailTo();
        String tokento = houseRecommended.getTokenTo();

        User userValid = new User();

        // Buscar por token
        Optional<User> userwithToken = userRepository.verificarToken(tokento);
        if (userwithToken.isPresent())
        {
            userValid = userwithToken.get();
            // El token es valido
            emailto = userValid.getEmail();
            bok = true;
        }

        if (!bok) {
            Optional<User> userwithEmail = userRepository.verificarEmail(emailto);
            if (userwithEmail.isPresent())
            {
                userValid = userwithEmail.get();
                tokento=userValid.getTokenforRecommended();
                bok = true;
            }
        }

        if (!bok) {

            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ni el token ni el correo son válidos.");

            return"redirect:/recommended/" + houseRecommended.getTokenFrom() + "/" +
                    houseRecommended.getHouseRecommended().getId() + "/" +
                    houseRecommended.getUserRecommended().getId();

        }

        houseRecommended.setEmailTo(emailto);
        houseRecommended.setTokenTo(tokento);

        // Ver si ya está recomendada por el mismo usuario al mismo destinatario

        Optional<HouseRecommended> houseValid = houseRecommendedRepository.findRecommendation(
                houseRecommended.getTokenFrom(),
                houseRecommended.getTokenTo(),
                houseRecommended.getId());


        if (houseValid.isPresent()) {

            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ya ha recomendado esta casa al usuario.");

            return"redirect:/recommended/" + houseRecommended.getTokenFrom() + "/" +
                    houseRecommended.getId() + "/" +  houseRecommended.getUserRecommended().getId();

        }

        // Introducimos el nombre y apellidos del usuario destino
        houseRecommended.setFirstNameTo(userValid.getFirstName());
        houseRecommended.setLastNameTo(userValid.getLastName());

        String idredirect = houseRecommended.getUserRecommended().getId().toString();
        houseRecommendedRepository.save(houseRecommended);

        return "redirect:/panel-control/" + idredirect;

    }

    @GetMapping("recommended-show/{idUsuario}")
    public String showRecommendations(Model model,@PathVariable Long idUsuario)
    {
        // Recomendaciones lanzadas
        List<HouseRecommended> recommendedFrom = houseRecommendedRepository.listHousesFrom(idUsuario);


        // Recomendaciones obtenidas
        User datosUsuario = userRepository.findById(idUsuario).orElseThrow();
        String token = datosUsuario.getTokenforRecommended();
        String email = datosUsuario.getEmail();

        List<HouseRecommended> recommendedToTokenEmail  =   houseRecommendedRepository.listHousesToEmail(email,token);

        model.addAttribute("recommfrom",recommendedFrom);
        model.addAttribute("recommto",recommendedToTokenEmail);

        return "/guest/recommended-list";

    }

}
