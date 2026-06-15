package com.demo.controller;

import com.demo.model.House;
import com.demo.model.HouseRecommended;
import com.demo.model.User;
import com.demo.service.HouseService;
import com.demo.service.BookingService;
import com.demo.service.RecommendedService;
import com.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class RecommendedController {

    private final HouseService houseService;
    private final UserService userService;
    private final RecommendedService recommendedService;

    @GetMapping("recommended/{token}/{idHouse}/{idUsuario}")
    public String createRecommended(Model model,
                                    @PathVariable String token,
                                    @PathVariable Long idHouse,
                                    @PathVariable Long idUsuario) {

        Optional<House> house = houseService.findById(idHouse);
        Optional<User> user = userService.findById(idUsuario);

        if (house.isPresent() && user.isPresent()) {

            House houseValid = house.get();
            User userValid = user.get();

            HouseRecommended recommendation = new HouseRecommended();

            recommendation.setUserTo(null);
            recommendation.setHouseRecommended(houseValid);
            recommendation.setUserFrom(userValid);
            /*
            recommendation.setTokenFrom(token);
            // Datos usuario que crea la recomendacion
            recommendation.setUserRecommended(userValid);
            recommendation.setFirstNameFrom(userValid.getFirstName());
            recommendation.setLastNameFrom(userValid.getLastName());
            recommendation.setEmailFrom(userValid.getEmail());

            recommendation.setEmailFrom(userValid.getEmail());
            */


            model.addAttribute("recommendation", recommendation);

            return "house/house-recommended";
        } else {
            return "redirect:/booking/" + idUsuario.toString();
        }

    }

    @PostMapping("recommended")
    public String addRecommendation(
            @RequestParam Long houseId,
            /*
            @RequestParam Long userFromId,
            @RequestParam String tokenFrom,
             */
            @RequestParam(required = false) String tokenTo,
            @RequestParam(required = false) String emailTo,
            @RequestParam(required = false) String message,
            @AuthenticationPrincipal User userFrom,
            RedirectAttributes redirectAttributes)
            {

        // 1) Cargar entidades base (las que estaban en el formulario son referencias, las recargamos por id)
        House house = houseService.findById(houseId).orElseThrow();
        // User userFrom = userService.findById(userFromId).orElseThrow();

        // 2) Resolver destinatario, por token y luego email, abstraído en el UserService
        User userTo = userService.resolveUserByTokenOrEmail(tokenTo, emailTo).orElse(null);
        if (userTo == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ni el token ni el correo son válidos.");
            return "redirect:/recommended/" + userFrom.getTokenforRecommended() + "/" + houseId + "/" + userFrom.getId();
        }

        // 3) Comprobar duplicado (mismo recomendador, mismo destinatario, misma casa)
        Optional<HouseRecommended> dup = recommendedService.findRecommendation(
                userFrom.getTokenforRecommended(),
                userTo.getTokenforRecommended(),
                houseId);                                  // ← ahora SÍ el id de la casa
        if (dup.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ya ha recomendado esta casa al usuario.");
            return "redirect:/recommended/" + userFrom.getTokenforRecommended() + "/" + houseId + "/" + userFrom.getId();
        }

        // 4) Crear entidad NUEVA → id null → JPA hará INSERT seguro
        HouseRecommended nueva = HouseRecommended.builder()
                .houseRecommended(house)
                .userFrom(userFrom)
                .userTo(userTo)
                .message(message)
                .build();
                /*
                .tokenFrom(tokenFrom)
                .tokenTo(userTo.getTokenforRecommended())
                .emailFrom(userFrom.getEmail())
                .emailTo(userTo.getEmail())
                .firstNameFrom(userFrom.getFirstName())
                .lastNameFrom(userFrom.getLastName())
                .firstNameTo(userTo.getFirstName())
                .lastNameTo(userTo.getLastName())

                 */

        recommendedService.save(nueva);

        redirectAttributes.addFlashAttribute("mensajeExito","Recomendación creada satisfactoriamente.");

        return "redirect:/panel-control/" + userFrom.getId();
    }

    @GetMapping("recommended-show/{idUsuario}")
    public String showRecommendations(Model model, @PathVariable Long idUsuario) {
        // Recomendaciones lanzadas
        List<HouseRecommended> recommendedFrom = recommendedService.listHousesFrom(idUsuario);


        // Recomendaciones obtenidas
        User datosUsuario = userService.findById(idUsuario).orElseThrow();
        String token = datosUsuario.getTokenforRecommended();
        String email = datosUsuario.getEmail();

        List<HouseRecommended> recommendedToTokenEmail = recommendedService.listHousesToEmail(email, token);

        model.addAttribute("recommfrom", recommendedFrom);
        model.addAttribute("recommto", recommendedToTokenEmail);

        return "house/recommended-list";

    }

}
