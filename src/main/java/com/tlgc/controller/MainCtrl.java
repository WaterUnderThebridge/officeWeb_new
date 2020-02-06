package com.tlgc.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainCtrl {
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String login() {
        return "/franchise/index";
    }
}