package com.learningnotes.springunittesting.controller;

import org.springframework.stereotype.Component;

@Component
public class GreetingService
{

    public String greet(final String name)
    {
        return "Welcome, " + name;
    }

}
