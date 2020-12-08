package com.fastcampus.project02.infomanagement.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class helloWorldControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .alwaysDo(print()).build();
    }
    @Test
    void helloWorldTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/helloWorld")
        )
        .andDo( print() )
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("Hello World")); //받아온 결과의 body 내용 확인
    }

    @Test
    void helloException() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/helloException"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("can't find cause"));
    }
}