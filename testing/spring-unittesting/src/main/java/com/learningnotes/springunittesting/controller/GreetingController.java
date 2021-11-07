package com.learningnotes.springunittesting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController
{
    @Autowired
    private GreetingService greetingService;

    @GetMapping("/greeting/{name}")
    public String greet(@PathVariable("name") final String name)
    {
        return greetingService.greet(name);
    }
}
