package com.softuni.musichub.song.controller;

import com.softuni.musichub.category.model.view.CategoryView;
import com.softuni.musichub.category.service.api.CategoryService;
import com.softuni.musichub.category.staticData.CategoryConstants;
import com.softuni.musichub.song.model.bindingModel.UploadSong;
import com.softuni.musichub.song.service.api.SongService;
import com.softuni.musichub.staticData.Constants;
import com.softuni.musichub.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/songs")
public class SongController {

    private static final String UPLOAD_SONG_TITLE = "Upload Song";

    private static final String UPLOAD_SONG_VIEW = "song/upload";

    private static final String UPLOAD_SONG = "uploadSong";

    private static final String UPLOADED_SUCCESS = "Song successfully uploaded!";

    private final CategoryService categoryService;

    private final SongService songService;

    @Autowired
    public SongController(CategoryService categoryService,
                          SongService songService) {
        this.categoryService = categoryService;
        this.songService = songService;
    }

    @GetMapping("/upload")
    public ModelAndView getUploadSongPage(ModelAndView modelAndView, Model model) {
        if (!model.asMap().containsKey(UPLOAD_SONG)) {
            UploadSong uploadSong = new UploadSong();
            modelAndView.addObject(UPLOAD_SONG, uploadSong);
        }

        List<CategoryView> categories = this.categoryService.findAll();
        modelAndView.addObject(CategoryConstants.CATEGORIES, categories);
        modelAndView.addObject(Constants.TITLE, UPLOAD_SONG_TITLE);
        modelAndView.addObject(Constants.VIEW, UPLOAD_SONG_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/upload")
    public ModelAndView uploadSong(ModelAndView modelAndView,
                                   @Valid @ModelAttribute UploadSong uploadSong,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   @AuthenticationPrincipal User user) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(UPLOAD_SONG, uploadSong);
            String bindingResultKey = Constants.BINDING_RESULT_PACKAGE + UPLOAD_SONG;
            redirectAttributes.addFlashAttribute(bindingResultKey, bindingResult);
            modelAndView.setViewName("redirect:/songs/upload");
            return modelAndView;
        }

        this.songService.upload(uploadSong, user);
        redirectAttributes.addFlashAttribute(Constants.SUCCESS, UPLOADED_SUCCESS);
        modelAndView.setViewName("redirect:/songs/upload");
        return modelAndView;
    }
}
