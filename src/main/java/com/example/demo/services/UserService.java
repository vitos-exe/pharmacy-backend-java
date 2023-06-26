package com.example.demo.services;

import com.example.demo.security.Utils;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.IdentityHolder;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

@Service
@Configuration
public class UserService implements UserDetailsService{
    private final static Supplier<NoSuchElementException> userNotFoundSupplier =
            () -> new NoSuchElementException("User was not found");
    UserRepository repository;
    PasswordEncoder encoder;
    IdentityHolder identityHolder;

    public UserService(UserRepository repository, PasswordEncoder encoder, IdentityHolder identityHolder) {
        this.repository = repository;
        this.encoder = encoder;
        this.identityHolder = identityHolder;
    }

    public List<User> getAllUsers(){
        return repository.findAll();
    }

    public User getUserById(Long id){
        Utils.verifyResourceAccess(id, identityHolder.getIdentity());
        return repository.findById(id).orElseThrow();
    }

    public User getUserByEmail(String emailAddress){
        return repository.findByEmailAddress(emailAddress).orElseThrow(userNotFoundSupplier);
    }

    @Transactional
    public User createUser(User user){
        Long id = user.getId();
        if (id != null){
            repository.findById(id).ifPresent(__ -> {
                throw new IllegalArgumentException("User with such id already exists");
            });
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Transactional
    public User updateUser(User user){
        Utils.verifyResourceAccess(user.getId(), identityHolder.getIdentity());
        user.setPassword(encoder.encode(user.getPassword()));
        repository.findById(user.getId()).ifPresentOrElse(
                __ -> repository.save(user),
                () -> {throw userNotFoundSupplier.get();}
        );
        return user;
    }

    @Transactional
    public void deleteUserById(Long id){
        Utils.verifyResourceAccess(id, identityHolder.getIdentity());
        repository.delete(repository.findById(id).orElseThrow(userNotFoundSupplier));
    }

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        return repository.findByEmailAddress(emailAddress).orElseThrow(
                () -> new UsernameNotFoundException("Username not found")
        );
    }
}
