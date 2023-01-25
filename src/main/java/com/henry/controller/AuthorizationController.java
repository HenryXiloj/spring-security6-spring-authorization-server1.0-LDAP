package com.henry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
public class AuthorizationController {

    @GetMapping(value={"", "/", "landing"})
    public String consent(Principal principal, Model model){

        System.out.println("user "+principal.getName());
        model.addAttribute("principalName", principal.getName());
        return "landing";
    }
}
