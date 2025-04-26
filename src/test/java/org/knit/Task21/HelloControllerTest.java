package org.knit.Task21;

import org.junit.jupiter.api.Test;
import org.knit.solutions.Task21.controller.HelloController;
import org.knit.solutions.Task21.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = HelloController.class)
@ContextConfiguration(classes = Application.class)
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSayHello() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Привет, мир!"));
    }

    @Test
    void testEcho() throws Exception {
        String json = """
            {
              "text": "Тестовое сообщение"
            }
        """;

        mockMvc.perform(post("/api/echo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Тестовое сообщение"));
    }
}
