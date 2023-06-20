package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class JustTest {
    @Test
    public void test(){
        System.out.println(new BCryptPasswordEncoder().encode("1234"));
    }
}
