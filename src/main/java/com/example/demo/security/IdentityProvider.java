package com.example.demo.security;

import com.example.demo.model.User;

public interface IdentityProvider {
    User getCurrentIdentity();
}
