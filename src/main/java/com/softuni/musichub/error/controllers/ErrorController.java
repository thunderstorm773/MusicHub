package com.softuni.musichub.error.controllers;

import com.softuni.musichub.error.staticData.ErrorConstants;
import com.softuni.musichub.staticData.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/errors")
public class ErrorController {

    @GetMapping("/500")
    public ModelAndView getServerErrorPage(ModelAndView modelAndView) {
        modelAndView.addObject(Constants.TITLE, ErrorConstants.SERVER_ERROR_TITLE);
        modelAndView.addObject(Constants.VIEW, ErrorConstants.SERVER_ERROR_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @GetMapping("/404")
    public ModelAndView getNotFoundErrorPage(ModelAndView modelAndView) {
        modelAndView.addObject(Constants.TITLE, ErrorConstants.NOT_FOUND_ERROR_TITLE);
        modelAndView.addObject(Constants.VIEW, ErrorConstants.NOT_FOUND_ERROR_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }
}
