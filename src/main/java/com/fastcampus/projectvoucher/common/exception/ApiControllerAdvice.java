package com.fastcampus.projectvoucher.common.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

@RestControllerAdvice
@Slf4j
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(IllegalStateException e) {
        log.info(Arrays.toString(e.getStackTrace()));
        return creategetErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResponse handleIllegalStateException(IllegalStateException e) {
        log.info(Arrays.toString(e.getStackTrace()));
        return creategetErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        log.error(Arrays.toString(e.getStackTrace()));
        return creategetErrorResponse(e.getMessage());
    }

    //나중에 확장설을 고려해서 따로 메서드를 만듬
    private static ErrorResponse creategetErrorResponse(String e) {
        return new ErrorResponse(e, LocalDate.now(), UUID.randomUUID());
    }
}
