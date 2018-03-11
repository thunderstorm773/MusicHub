package com.softuni.musichub.music.controller;

import com.softuni.musichub.category.model.view.CategoryView;
import com.softuni.musichub.category.service.api.CategoryService;
import com.softuni.musichub.category.staticData.CategoryConstants;
import com.softuni.musichub.staticData.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
@RequestMapping("/music")
public class MusicController {

    private static final String UPLOAD_MUSIC_TITLE = "Upload music";

    private static final String UPLOAD_MUSIC_VIEW = "music/upload";

    private final CategoryService categoryService;

    @Autowired
    public MusicController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/upload")
    public ModelAndView getUploadMusicPage(ModelAndView modelAndView) {
        List<CategoryView> categories = this.categoryService.findAll();
        modelAndView.addObject(CategoryConstants.CATEGORIES, categories);
        modelAndView.addObject(Constants.TITLE, UPLOAD_MUSIC_TITLE);
        modelAndView.addObject(Constants.VIEW, UPLOAD_MUSIC_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }
}
