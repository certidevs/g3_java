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
        ORDER BY hs.timeRecommended DESC
    """)
    List<HouseRecommended> listHousesFrom(@Param("idUsuario") Long idUsuario);

    // Recomendaciones recibidas
    @Query("""
        SELECT hs FROM HouseRecommended hs WHERE hs.userTo.email=:email OR
            hs.userTo.tokenforRecommended=:token
        ORDER BY hs.timeRecommended DESC
    """)
    List<HouseRecommended> listHousesToEmail(@Param("email") String email,
                                             @Param("token") String token);

    @Query("""
        SELECT COUNT(hs) > 0 FROM HouseRecommended hs 
        WHERE (hs.userTo.email = :email OR hs.userTo.tokenforRecommended = :token) 
        AND hs.viewed = false
    """)
    boolean hasUnreadRecommendations(@Param("email") String email, @Param("token") String token);

    boolean existsByUserFromIdAndHouseRecommendedId(Long userFromId, Long houseId);

}

