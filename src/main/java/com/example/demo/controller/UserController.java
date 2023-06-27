package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {
    UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<User> getAllUsers(){
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return service.getUserById(id);
    }

    @PostMapping("/")
    public User postUser(@RequestBody User user){
        return service.createUser(user);
    }

    @PutMapping("/{id}")
    public User putUser(@PathVariable Long id, @RequestBody User user){
        if (!Objects.equals(user.getId(), id)){
            throw new IllegalArgumentException("Path id and one in request body don't match");
        }
        return service.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        service.deleteUserById(id);
    }
}
