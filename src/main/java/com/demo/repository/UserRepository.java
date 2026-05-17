package com.demo.repository;

import com.demo.model.HouseRecommended;
import com.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

//    Optional<User> findById(Long id);

    // ACCIONES PARA VALIDACIONES EN RECOMENDACIONES

    @Query("""
        SELECT us FROM User us WHERE us.tokenforRecommended=?1 
    """)
    Optional<User> verificarToken (String token);

    @Query("""
        SELECT us FROM User us WHERE us.email=?1 
    """)
    Optional<User> verificarEmail (String email);



}
