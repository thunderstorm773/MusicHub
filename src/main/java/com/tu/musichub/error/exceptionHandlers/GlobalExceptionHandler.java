package com.tu.musichub.error.exceptionHandlers;

import com.tu.musichub.controller.BaseController;
import com.tu.musichub.error.staticData.ErrorConstants;
import com.tu.musichub.home.staticData.HomeConstants;
import com.tu.musichub.user.exceptions.PasswordResetTokenNotFoundException;
import com.tu.musichub.user.exceptions.UserNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseController{

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions() {
        return this.redirect(ErrorConstants.SERVER_ERROR_ROUTE);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ModelAndView handleMethodArgumentTypeMismatchException() {
        return this.redirect(HomeConstants.INDEX_ROUTE);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView handleHttpRequestMethodNotSupportedException() {
        return this.redirect(HomeConstants.INDEX_ROUTE);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException() {
        return this.redirect(HomeConstants.INDEX_ROUTE);
    }

    @ExceptionHandler(PasswordResetTokenNotFoundException.class)
    public ModelAndView handlePasswordResetTokenNotFoundException() {
        return this.redirect(HomeConstants.INDEX_ROUTE);
    }
}
