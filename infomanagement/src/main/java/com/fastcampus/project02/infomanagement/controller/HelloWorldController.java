package com.fastcampus.project02.infomanagement.controller;

import com.fastcampus.project02.infomanagement.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//command + b = 어노테이션 기능 확인 가능
@RestController // @Controller + @ResponseBody
public class HelloWorldController {

    @GetMapping(value = "/api/helloWorld")
    public String helloWorld(){
        return "Hello World";
    }

    @GetMapping(value = "/api/helloException")
    public String helloException() {
        throw new RuntimeException();
    }
}

