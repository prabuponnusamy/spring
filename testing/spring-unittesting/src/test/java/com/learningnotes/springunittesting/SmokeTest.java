package com.learningnotes.springunittesting;

import com.learningnotes.springunittesting.controller.WelcomeController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SmokeTest {

    @Autowired
    private WelcomeController welcomeController;

    @Test
    void contextLoads() throws Exception {
        assertThat(welcomeController).isNotNull();
    }
}
