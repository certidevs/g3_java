package com.demo.repository;

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
        SELECT hs FROM HouseRecommended hs WHERE hs.tokenFrom=:tokenfrom AND hs.tokenTo=:tokento AND
         hs.houseRecommended.id=:idhouse
    """)
    Optional<HouseRecommended> findRecommendation ( @Param("tokenfrom") String tokenFrom,
                                                    @Param("tokento") String tokenTo,
                                                    @Param("idhouse") Long idHouse);


    // Recomendaciones lanzadas
    @Query("""
        SELECT hs FROM HouseRecommended hs WHERE hs.userRecommended.id=:idUsuario
    """)
    List<HouseRecommended> listHousesFrom(@Param("idUsuario") Long idUsuario);

    // Recomendaciones recibidas
    @Query("""
        SELECT hs FROM HouseRecommended hs WHERE hs.emailTo=:email OR
            hs.tokenTo=:token
    """)
    List<HouseRecommended> listHousesToEmail(@Param("email") String email,
                                             @Param("token") String token);

}
