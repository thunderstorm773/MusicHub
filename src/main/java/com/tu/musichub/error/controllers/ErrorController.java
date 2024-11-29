package com.tu.musichub.error.controllers;

import com.tu.musichub.controller.BaseController;
import com.tu.musichub.error.staticData.ErrorConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/errors")
public class ErrorController extends BaseController {

    @GetMapping("/500")
    public ModelAndView getServerErrorPage() {
        return this.view(ErrorConstants.SERVER_ERROR_TITLE,
                ErrorConstants.SERVER_ERROR_VIEW);
    }

    @GetMapping("/404")
    public ModelAndView getNotFoundErrorPage() {
        return this.view(ErrorConstants.NOT_FOUND_ERROR_TITLE,
                ErrorConstants.NOT_FOUND_ERROR_VIEW);
    }
}
