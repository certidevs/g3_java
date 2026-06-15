package com.demo.service;

import com.demo.dto.RegisterForm;
import com.demo.dto.UserRecommendationsDto;
import com.demo.model.User;
import com.demo.model.enums.Role;
import com.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public User getByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username));
    }

    public void toggle(Long id) {
        User user = getByIdOrThrow(id);
        user.setActive(!user.getActive());
        userRepository.save(user);
    }

    public User register(RegisterForm form) {
        if (userRepository.existsByUsername(form.getUsername()))
            throw new IllegalArgumentException("El nombre de usuario ya existe");

        if (userRepository.existsByEmail(form.getEmail()))
            throw new IllegalArgumentException("El correo electrónico ya existe");

        if (!form.getPassword().equals(form.getPasswordConfirm()))
            throw new IllegalArgumentException("Las contraseñas no coinciden");

        User user = new User();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        // user.setPassword(form.getPassword()); // texto plano sin cifrar
        user.setPassword(passwordEncoder.encode(form.getPassword())); // password cifrada con bcrypt
        user.setRole(Role.ROLE_USER);
        user.setTokenforRecommended(generateRecommendedToken());
        return userRepository.save(user);
    }

    public String generateRecommendedToken() {
        SecureRandom scr = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 8; i++) {
            int index = scr.nextInt(caracteres.length());
            sb.append(caracteres.charAt(index));
        }
        return sb.toString();
    }

    public User update(User userForm, User actor) {
        User userDB = getByIdOrThrow(userForm.getId());

        userRepository.findByEmail(userForm.getEmail())
                .filter(u -> !u.getId().equals(userForm.getId()))
                .ifPresent(u -> {
                    throw new IllegalArgumentException("El correo electrónico ya existe");
                });

        userRepository.findByUsername(userForm.getUsername())
                .filter(u -> !u.getId().equals(userForm.getId()))
                .ifPresent(u -> {
                    throw new IllegalArgumentException("El nombre de usuario ya existe");
                });

        userDB.setUsername(userForm.getUsername());
        userDB.setEmail(userForm.getEmail());
        userDB.setFirstName(userForm.getFirstName());
        userDB.setLastName(userForm.getLastName());
        if (actor.getRole() == Role.ROLE_ADMIN) {
            // Un administrador no puede cambiarse el rol ni desactivarse a sí mismo
            if (!actor.getUsername().equals(userDB.getUsername())) {
                userDB.setRole(userForm.getRole());
                if (userForm.getActive() != null) {
                    userDB.setActive(userForm.getActive());
                }
            }
        } else {
            userDB.setRole(Role.ROLE_USER);
        }
        // userDB.setImageUrl(userForm.getImageUrl());

        if (StringUtils.hasText(userForm.getPassword())) {
            userDB.setPassword(passwordEncoder.encode(userForm.getPassword()));
        }

        return userRepository.save(userDB);
    }

    public User create(User userForm, User actor) {
        if (userRepository.existsByUsername(userForm.getUsername()))
            throw new IllegalArgumentException("El nombre de usuario ya existe");

        if (userRepository.existsByEmail(userForm.getEmail()))
            throw new IllegalArgumentException("El correo electrónico ya existe");

        if (!StringUtils.hasText(userForm.getPassword()))
            throw new IllegalArgumentException("La contraseña es obligatoria");

        if (userForm.getRole() == null) {
            userForm.setRole(Role.ROLE_USER);
        }

        if (actor.getRole() != Role.ROLE_ADMIN && (userForm.getRole() != Role.ROLE_USER)) {
            throw new IllegalArgumentException("No puedes asignar un rol diferente a USER");
        }

        User newUser = User.builder()
                .username(userForm.getUsername())
                .email(userForm.getEmail())
                .firstName(userForm.getFirstName())
                .lastName(userForm.getLastName())
                .role(userForm.getRole())
                .password(passwordEncoder.encode(userForm.getPassword()))
                // .active(true)
                // .imageUrl(...)
                .build();

        return userRepository.save(newUser);
    }

    public List<UserRecommendationsDto> getAgendaUsers(String textFind) {
        List<UserRecommendationsDto> allUsers = userRepository.findAllUsersWithRecommendationCount();
        if (textFind == null || textFind.isBlank()) {
            return allUsers;
        }
        String query = normalize(textFind);
        return allUsers.stream()
                .filter(u -> normalize(u.username()).contains(query)
                        || normalize(u.firstName()).contains(query)
                        || normalize(u.lastName()).contains(query))
                .toList();
    }

    public List<UserRecommendationsDto> getAgendaUsers() {
        return getAgendaUsers(null);
    }

    private String normalize(String str) {
        if (str == null) {
            return "";
        }
        return java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }

    public Optional<User> resolveUserByTokenOrEmail(String token, String email) {
        if (token != null && !token.isBlank()) {
            return userRepository.verificarToken(token);
        }
        if (email != null && !email.isBlank()) {
            return userRepository.verificarEmail(email);
        }
        return Optional.empty();
    }
}
