package com.fastcampus.project02.infomanagement.exception.handler;

import com.fastcampus.project02.infomanagement.exception.PersonNotFoundException;
import com.fastcampus.project02.infomanagement.exception.RenameNotPermittedException;
import com.fastcampus.project02.infomanagement.exception.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //아이디는 같은데 이름은 다른 경우 오류 핸들러
    @ExceptionHandler(RenameNotPermittedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //header 값 지정
    public ErrorResponse RenameNoPermittedException(RenameNotPermittedException ex)  {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(PersonNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //header 값 지정
    public ErrorResponse PersonNotFoundException(PersonNotFoundException ex){
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //header 값 지정
    public ErrorResponse handleRunTimeException(RuntimeException ex){
        log.error("server error: {}", ex.getMessage(), ex); //예외에 stack???? 뭐도 같이 남긴다.
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "can't find cause");
    }
}
