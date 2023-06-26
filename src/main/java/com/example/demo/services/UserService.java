package com.example.demo.services;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Configuration
public class UserService implements UserDetailsService{

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
        return repository.findByEmailAddress(emailAddress).orElseThrow();
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

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        return repository.findByEmailAddress(emailAddress).orElseThrow(
                () -> new UsernameNotFoundException("Username not found")
        );
    }
}
