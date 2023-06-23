package com.example.demo.security;


import com.example.demo.model.User;
import com.example.demo.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Objects;

@Component
public class IdentityProviderImpl implements IdentityProvider{
    UserService userService;

    public IdentityProviderImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User getCurrentIdentity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)){
            throw new RuntimeException("Authentication object was requested but it is not present in security context");
        }
        try {
            return userService.getUserByEmail(authentication.getName());
        } catch (NoSuchElementException e){
            throw new RuntimeException("Authenticated user was not found in datasource");
        }
    }
}
