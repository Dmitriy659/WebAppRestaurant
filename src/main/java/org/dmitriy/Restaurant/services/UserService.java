package org.dmitriy.Restaurant.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dmitriy.Restaurant.models.User;
import org.dmitriy.Restaurant.models.enums.Role;
import org.dmitriy.Restaurant.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        }
        user.getRoles().add(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("Saving new user with email: " + user.getEmail());
        return true;
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return null;
        }
        return userRepository.findByEmail(principal.getName());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public void make(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (user.isAdmin()) {
                user.getRoles().remove(Role.ROLE_ADMIN);
            }
            else {
                user.getRoles().add(Role.ROLE_ADMIN);
            }
            userRepository.save(user);
        }
    }

    public void delete(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.deleteById(id);
        }
    }
}
