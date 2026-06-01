package com.demo.service;

import com.demo.controller.dto.RegisterForm;
import com.demo.model.House;
import com.demo.model.Role;
import com.demo.model.User;
import com.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username));
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
        return userRepository.save(user);
    }

    public List<User> getAgendaUsers(String textFind) {
        if (textFind == null || textFind.isBlank()) {
            return userRepository.userallOrderFirstName();
        }
        return userRepository.userallOrderFirstNameFilterText(textFind);
    }
    public List<User> getAgendaUsers() {
        return getAgendaUsers(null);
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
