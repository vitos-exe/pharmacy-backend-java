package com.example.demo.services;

import com.example.demo.model.Medicine;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.IdentityHolder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(UserService.class)
class UserServiceTest {
    @Autowired
    UserRepository repository;
    @Autowired
    UserService service;
    @MockBean
    IdentityHolder identityHolder;

    @TestConfiguration
    static class TestConfig{
        @Bean
        PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }
    }

    @Test
    void getAllUsers() {
        List<User> users = service.getAllUsers();
        assertEquals(2, users.size());
        assertEquals("User", users.get(0).getFullName());
        System.out.println(users);
        assertEquals("City2", users.get(1).getAddress());
    }

    @Test
    void getUserById_Success() {
        Mockito.when(identityHolder.getIdentity())
                .thenReturn(repository.findById(2L).get());

        User user = service.getUserById(1L);
        assertEquals("User", user.getFullName());
        assertEquals(User.Role.USER, user.getRole());
    }

    @Test
    void getUserById_NoSuchElementException() {
        Mockito.when(identityHolder.getIdentity())
                .thenReturn(repository.findById(2L).get());

        assertThrows(NoSuchElementException.class, () -> service.getUserById(3L));
    }

    @Test
    void getUserById_SecurityException() {
        Mockito.when(identityHolder.getIdentity())
                .thenReturn(repository.findById(1L).get());

        assertThrows(SecurityException.class, () -> service.getUserById(2L));
    }

    @Test
    void createUser() {
        User newUser = new User(
                3L,
                "User2",
                "user2@example.com",
                "12345",
                "Capital",
                User.Role.USER,
                Set.of()
        );

        service.createUser(newUser);
        Optional<User> justCreated = repository.findById(3L);
        assertTrue(justCreated.isPresent());
        assertEquals(newUser.getFullName(), justCreated.get().getFullName());
    }

    @Test
    void updateUser() {
        Mockito.when(identityHolder.getIdentity())
                .thenReturn(repository.findById(1L).get());

        User newUser = new User(
                1L,
                "User2",
                "user2@example.com",
                "12345",
                "Capital",
                User.Role.USER,
                Set.of()
        );

        service.updateUser(newUser);
        Optional<User> updatedUser = repository.findById(1L);
        assertTrue(updatedUser.isPresent());
        assertEquals(newUser.getFullName(), updatedUser.get().getFullName());
    }

    @Test
    void deleteUserById_Success() {
        Mockito.when(identityHolder.getIdentity())
                        .thenReturn(repository.findById(2L).get());
        service.deleteUserById(1L);
        assertTrue(repository.findById(1L).isEmpty());
    }

    @Test
    void deleteUserById_SecurityException(){
        Mockito.when(identityHolder.getIdentity())
                .thenReturn(repository.findById(1L).get());
        assertThrows(SecurityException.class, () -> service.deleteUserById(2L));
    }
}