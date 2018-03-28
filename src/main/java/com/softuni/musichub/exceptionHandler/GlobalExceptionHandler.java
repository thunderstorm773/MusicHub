package com.softuni.musichub.exceptionHandler;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions() {
        return "redirect:/errors/500";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleMethodArgumentTypeMismatchException() {
        return "redirect:/songs/browse";
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleHttpRequestMethodNotSupportedException() {
        return "redirect:/songs/browse";
    }
}
