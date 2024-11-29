package com.tu.musichub.controller;

import com.tu.musichub.staticData.Constants;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;

public abstract class BaseController {

    protected BaseController() {
    }

    protected ModelAndView redirect(String route) {
        return new ModelAndView("redirect:" + route);
    }

    protected ModelAndView view(String title, String viewTemplate) {
        return this.view(title, viewTemplate, null);
    }

    protected ModelAndView view(String title, String viewTemplate,
                               Map<String, Object> args) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(Constants.TITLE, title);
        modelAndView.addObject(Constants.VIEW, viewTemplate);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        if (args != null) {
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                modelAndView.addObject(key, value);
            }
        }

        return modelAndView;
    }
}
