package com.example.conflictTracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaForwardController {

    @GetMapping({"/conflicts", "/conflicts/{id:[0-9]+}"})
    public String forwardToSpa() {
        return "forward:/index.html";
    }
}
