package com.demo.repository;

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
        SELECT us FROM User us  WHERE us.firstName IS NOT NULL ORDER BY us.firstName ASC
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

}
