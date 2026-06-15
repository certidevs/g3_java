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
        Long recommendedCount
) {
    // Constructor de compatibilidad para consultas simplificadas
    public UserRecommendationsDto(
            Long id,
            String username,
            String firstName,
            String lastName,
            String tokenforRecommended,
            Long recommendedCount
    ) {
        this(id, username, firstName, lastName, null, null, null, tokenforRecommended, recommendedCount);
    }
}
