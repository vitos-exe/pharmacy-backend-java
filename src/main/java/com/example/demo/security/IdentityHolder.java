package com.example.demo.security;

import com.example.demo.model.User;

/**
 * Interface for retrieving current authenticated user's identity
 */
public interface IdentityHolder {
    User getIdentity();
}
