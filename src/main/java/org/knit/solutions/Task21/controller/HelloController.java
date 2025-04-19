package org.knit.solutions.Task21.controller;

import org.knit.solutions.Task21.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        log.info("GET /api/hello вызван");
        return "Привет, мир!";
    }

    @PostMapping("/echo")
    public Message echo(@RequestBody Message input) {
        log.info("POST /api/echo с телом: {}", input.getText());
        return input;
    }
}
