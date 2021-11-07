package com.learningnotes.springunittesting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
 class WelcomeControllerUnitTestMockMvc {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void welcomeShouldReturnMessage() throws Exception {
        final MvcResult mvc = mockMvc.perform(MockMvcRequestBuilders.get("/welcome"))
            .andDo(print())
            .andExpect(status().isOk())
                .andReturn();
        // .andExpect(content().string(containsString("Hello!, welcome")));
        assertThat(mvc.getResponse().getContentAsString()).isEqualTo("Hello!, welcome");
    }
}
