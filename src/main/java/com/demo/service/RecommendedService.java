package com.demo.service;

import com.demo.model.HouseRecommended;
import com.demo.repository.BookingRepository;
import com.demo.repository.HouseRecommendedRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RecommendedService {

    private HouseRecommendedRepository recommendedRepository;

    public void insertRecommendation (HouseRecommended houserecommended)
    {
        recommendedRepository.save(houserecommended);
    }


}