package com.softuni.musichub.home.controller;

import com.softuni.musichub.staticData.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private static final String INDEX_TITLE = "Index";

    private static final String INDEX_VIEW = "home/index";

    private static final String ABOUT_US_TITLE = "About us";

    private static final String ABOUT_US_VIEW = "home/about-us";

    @GetMapping("/")
    public ModelAndView getIndexPage(ModelAndView modelAndView) {
        modelAndView.addObject(Constants.TITLE, INDEX_TITLE);
        modelAndView.addObject(Constants.VIEW, INDEX_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @GetMapping("/about-us")
    public ModelAndView getAboutUsPage(ModelAndView modelAndView) {
        modelAndView.addObject(Constants.TITLE, ABOUT_US_TITLE);
        modelAndView.addObject(Constants.VIEW, ABOUT_US_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }
}
