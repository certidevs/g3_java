package com.demo.dto;

import com.demo.model.enums.Role;

public record UserRecommendationsDto(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        Boolean active,
        Role role,
        String tokenforRecommended,
        Long recommendedCount,
        Long recommendationsReceivedCount
) {
}
