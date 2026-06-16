package com.demo.repository;

import com.demo.dto.UserRecommendationsDto;
import com.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

//    Optional<User> findById(Long id);

    //Resgistro verificar si el email o username estas ocupados
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    //login recuperar el user
//    Optional<org.springframework.security.core.userdetails.User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);



    // ACCIONES PARA VALIDACIONES EN RECOMENDACIONES

    @Query("""
        SELECT us FROM User us WHERE us.tokenforRecommended=?1
    """)
    Optional<User> verificarToken (String token);

    @Query("""
        SELECT us FROM User us WHERE us.email=?1
    """)
    Optional<User> verificarEmail (String email);

    // TODOS LOS USUARIOS ORDENADOS POR FIRSTNAME
    @Query("""
        SELECT us FROM User us
        ORDER BY
            CASE WHEN (us.firstName IS NOT NULL AND us.firstName <> '') THEN 0 ELSE 1 END ASC,
            us.firstName ASC,
            us.username ASC
    """)
    List<User> userallOrderFirstName ();

    // USUARIOS FILTRADOS POR TEXTO
    @Query("""
            SELECT us FROM User us  WHERE us.firstName IS NOT NULL AND
                ((:textfind IS NULL OR us.firstName LIKE  %:textfind%) OR
                (:textfind IS NULL OR us.lastName LIKE  %:textfind%))
            """)
    List<User> userallOrderFirstNameFilterText (
            @Param("textfind") String textoFind);

    @Query("""
        SELECT us FROM User us WHERE us.id IN (
            SELECT hr.userTo.id FROM HouseRecommended hr WHERE hr.userFrom.id=?1
        )
    """)
    List<User> userRecommendedByUser (Long idUser);

    @Query("""
        SELECT new com.demo.dto.UserRecommendationsDto(
            us.id as id,
            us.username as username,
            us.firstName as firstName,
            us.lastName as lastName,
            us.email as email,
            us.active as active,
            us.role as role,
            us.tokenforRecommended as tokenforRecommended,
            COALESCE(SUM(CASE WHEN hr.userFrom.id = ?1 THEN 1L ELSE 0L END), 0L) as recommendedCount,
            COALESCE(SUM(CASE WHEN hr.userTo.id = ?1 THEN 1L ELSE 0L END), 0L) as recommendationsReceivedCount
        )
        FROM User us
        LEFT JOIN HouseRecommended hr ON (hr.userFrom.id = ?1 AND hr.userTo.id = us.id) OR (hr.userFrom.id = us.id AND hr.userTo.id = ?1)
        WHERE us.id <> ?1
        GROUP BY us.id, us.username, us.firstName, us.lastName, us.email, us.active, us.role, us.tokenforRecommended
        ORDER BY
            COALESCE(SUM(CASE WHEN hr.userFrom.id = ?1 THEN 1L ELSE 0L END), 0L) DESC,
            COALESCE(SUM(CASE WHEN hr.userTo.id = ?1 THEN 1L ELSE 0L END), 0L) DESC,
            CASE WHEN (us.firstName IS NOT NULL AND us.firstName <> '') OR (us.lastName IS NOT NULL AND us.lastName <> '') THEN 0 ELSE 1 END ASC,
            us.firstName ASC,
            us.lastName ASC,
            us.username ASC
    """)
    List<UserRecommendationsDto> findAllUsersWithRecommendationCounts(Long currentUserId);
}
