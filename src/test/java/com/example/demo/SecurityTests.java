package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void testPermitAllEndpoint(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        assertTrue(restTemplate.getForEntity("/medicine/", String.class).getStatusCode().is2xxSuccessful());
        assertTrue(restTemplate
                .postForEntity("/user/", new HttpEntity<>(new User(), httpHeaders), String.class)
                .getStatusCode().is2xxSuccessful());
    }

    @Test
    void testAllAuthenticationRequiredEndpoint_Success(){
        TestRestTemplate templateWithBasicAuth = restTemplate.withBasicAuth("user@example.com", "12345");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        assertTrue(templateWithBasicAuth.getForEntity("/order/1", String.class).getStatusCode().is2xxSuccessful());
        assertTrue(templateWithBasicAuth
                .postForEntity("/order/", new HttpEntity<>(Collections.emptyList(), httpHeaders), String.class)
                .getStatusCode().is2xxSuccessful());
        assertTrue(templateWithBasicAuth.getForEntity("/user/1", String.class).getStatusCode().is2xxSuccessful());
    }
}
