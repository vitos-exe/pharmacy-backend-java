package com.example.demo.security;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.NoSuchElementException;
import java.util.Objects;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(authorize -> {
            authorize
                    .requestMatchers("/error").permitAll()
                    .requestMatchers(HttpMethod.GET, "/medicine/*").permitAll()
                    .requestMatchers("/medicine/*").hasRole(User.Role.ADMIN.toString())
                    .requestMatchers("/order/*").authenticated()
                    .requestMatchers(HttpMethod.POST, "/order/").hasRole(User.Role.ADMIN.toString())
                    .requestMatchers(HttpMethod.PATCH, "/order/**").hasRole(User.Role.ADMIN.toString())
                    .requestMatchers(HttpMethod.POST, "/user/").permitAll()
                    .requestMatchers("/user/*").authenticated();
        }).csrf(AbstractHttpConfigurer::disable).httpBasic(Customizer.withDefaults()).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IdentityHolder identityHolder(UserRepository userRepository){
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.isNull(authentication)){
                throw new RuntimeException("Authentication object was requested but it is not present in security context");
            }
            return userRepository.findByEmailAddress(authentication.getName()).orElseThrow(
                    () -> new RuntimeException("Authenticated user was not found in datasource")
            );
        };
    }
}
