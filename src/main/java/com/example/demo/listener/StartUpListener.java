package com.example.demo.listener;

import com.example.demo.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StartUpListener {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public StartUpListener(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Encodes initial users passwords(used only for development)
     */
    @EventListener(ApplicationReadyEvent.class)
    @Profile("dev")
    @Transactional
    public void encodeInitialUsersPassword(){
        userRepository.findAll().forEach(user -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        });
    }
}
