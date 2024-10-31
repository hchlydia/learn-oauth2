package com.example.learn_oauth2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping("/")
    public String index(Authentication authentication) {
        String userName = authentication.getName(); //provider ID的值
        return "Hello " + userName;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome!";
    }
}
