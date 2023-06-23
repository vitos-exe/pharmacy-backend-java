package com.example.demo.services;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Configuration
public class UserService{

    UserRepository repository;

    PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public List<User> getAllUsers(){
        return repository.findAll();
    }

    public User getUserById(Long id){
        return repository.findById(id).orElseThrow();
    }

    public User getUserByEmail(String emailAddress){
        return repository.findByEmailAddress(emailAddress).orElseThrow(); //TODO this should not happen, fix
    }

    @Transactional
    public User createUser(User user){
        Long id = user.getId();
        if (id != null){
            repository.findById(id).ifPresent(__ -> {
                throw new IllegalArgumentException();
            });
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Transactional
    public User updateUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return repository.findById(user.getId()).map(repository::save).orElseThrow();
    }

    @Transactional
    public void deleteUserById(Long id){
        repository.delete(repository.findById(id).orElseThrow());
    }

    // Produces UserDetailsService production bean, that uses JDBC to match user credentials for authentication
    @Bean
    @ConditionalOnProperty(
            value = "spring.profiles.active",
            havingValue = "prod"
    )
    public UserDetailsService jdbcUserDetailsService(){
        return emailAddress -> repository.findByEmailAddress(emailAddress).orElseThrow(
                () -> new UsernameNotFoundException("Username not found")
        );
    }

    // TODO replace this with just initial data
    // Produces development bean, that has in-memory users to match credentials
    @Bean
    @ConditionalOnProperty(
            value = "spring.profiles.active",
            havingValue = "dev",
            matchIfMissing = true
    )
    public UserDetailsService inmemoryUserDetailsService(PasswordEncoder encoder){
        UserDetails user = org.springframework.security.core.userdetails.User.builder()
                .username("user@example.com")
                .password("12345")
                .authorities(User.Role.USER)
                .passwordEncoder(encoder::encode)
                .build();

        UserDetails admin = org.springframework.security.core.userdetails.User.builder()
                .username("admin@example.com")
                .password("12345")
                .authorities(User.Role.ADMIN)
                .passwordEncoder(encoder::encode)
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
