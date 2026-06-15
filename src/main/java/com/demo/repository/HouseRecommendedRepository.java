package com.demo.repository;

import com.demo.dto.UserRecommendationsDto;
import com.demo.model.Booking;
import com.demo.model.HouseRecommended;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HouseRecommendedRepository extends JpaRepository<HouseRecommended, Long> {

    // TODO

    @Query("""
        SELECT hs FROM HouseRecommended hs WHERE hs.userFrom.tokenforRecommended=:tokenfrom AND hs.userTo.tokenforRecommended=:tokento AND
         hs.houseRecommended.id=:idhouse
    """)
    Optional<HouseRecommended> findRecommendation ( @Param("tokenfrom") String tokenFrom,
                                                    @Param("tokento") String tokenTo,
                                                    @Param("idhouse") Long idHouse);


    // Recomendaciones lanzadas
    @Query("""
        SELECT hs FROM HouseRecommended hs WHERE hs.userFrom.id=:idUsuario
    """)
    List<HouseRecommended> listHousesFrom(@Param("idUsuario") Long idUsuario);

    // Recomendaciones recibidas
    @Query("""
        SELECT hs FROM HouseRecommended hs WHERE hs.userTo.email=:email OR
            hs.userTo.tokenforRecommended=:token
    """)
    List<HouseRecommended> listHousesToEmail(@Param("email") String email,
                                             @Param("token") String token);

    // Top de recomendados por id userFrom
    @Query("""
                SELECT new com.demo.dto.UserRecommendationsDto(
                    us.id,
                    us.username,
                    us.firstName,
                    us.lastName,
                    us.tokenforRecommended,
                    COUNT(hr)
                )
                FROM HouseRecommended hr
                JOIN hr.userTo us
                WHERE hr.userFrom.id = ?1
                GROUP BY us.id, us.username, us.firstName, us.lastName, us.tokenforRecommended
                ORDER BY COUNT(hr) DESC
            """)
    List<UserRecommendationsDto> userRecommendedByUserTop (Long idUser);
}
