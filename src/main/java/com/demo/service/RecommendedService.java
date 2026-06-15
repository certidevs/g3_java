package com.demo.service;

import com.demo.dto.UserRecommendationsDto;
import com.demo.model.HouseRecommended;
import com.demo.repository.HouseRecommendedRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RecommendedService {

    private final HouseRecommendedRepository recommendedRepository;

    public void insertRecommendation (HouseRecommended houserecommended)
    {
        recommendedRepository.save(houserecommended);
    }

    public void save(HouseRecommended houseRecommended) {
        recommendedRepository.save(houseRecommended);
    }

    public Optional<HouseRecommended> findRecommendation(String tokenFrom, String tokenTo, Long idHouse) {
        return recommendedRepository.findRecommendation(tokenFrom, tokenTo, idHouse);
    }

    public List<HouseRecommended> listHousesFrom(Long idUsuario) {
        return recommendedRepository.listHousesFrom(idUsuario);
    }

    public List<HouseRecommended> listHousesToEmail(String email, String token) {
        return recommendedRepository.listHousesToEmail(email, token);
    }

}