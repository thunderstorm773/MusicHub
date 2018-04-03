package com.softuni.musichub.error.exceptionHandlers;

import com.softuni.musichub.error.staticData.ErrorConstants;
import com.softuni.musichub.home.staticData.HomeConstants;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions() {
        return "redirect:" + ErrorConstants.SERVER_ERROR_ROUTE;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleMethodArgumentTypeMismatchException() {
        return "redirect:" + HomeConstants.INDEX_ROUTE;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleHttpRequestMethodNotSupportedException() {
        return "redirect:" + HomeConstants.INDEX_ROUTE;
    }
}
