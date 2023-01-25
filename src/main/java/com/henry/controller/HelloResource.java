package com.henry.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HelloResource {

    @GetMapping("/hello")
    public String hello(Principal principal) {
        return "Hello "+principal.getName();
    }
}
