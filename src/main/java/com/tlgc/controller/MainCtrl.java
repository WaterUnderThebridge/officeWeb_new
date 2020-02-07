package com.tlgc.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MainCtrl {
    @GetMapping("/")
    public String login(HttpServletRequest request) {
        return "franchise/index";
    }
}