package com.fastcampus.project02.infomanagement.exception.dto;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE) //생성자 private로 설정
public class ErrorResponse {
    private int code;
    private String message;

    public static ErrorResponse of(@NotNull HttpStatus httpStatus, String message) {
        return new ErrorResponse(httpStatus.value(), message);
    }

    public static ErrorResponse of(HttpStatus httpStatus, FieldError fieldError) {
        if (fieldError == null)
            return new ErrorResponse(httpStatus.value(), "invalid params");
        return new ErrorResponse(httpStatus.value(), fieldError.getDefaultMessage());
    }
}
