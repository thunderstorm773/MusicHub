package com.softuni.musichub.song.controllers;

import com.softuni.musichub.admin.category.models.views.CategoryView;
import com.softuni.musichub.admin.category.services.CategoryService;
import com.softuni.musichub.admin.category.staticData.CategoryConstants;
import com.softuni.musichub.song.comment.staticData.CommentConstants;
import com.softuni.musichub.song.exceptions.SongNotFoundException;
import com.softuni.musichub.song.models.bindingModels.EditSong;
import com.softuni.musichub.song.models.bindingModels.UploadSong;
import com.softuni.musichub.song.models.viewModels.SongDetailsView;
import com.softuni.musichub.song.models.viewModels.SongView;
import com.softuni.musichub.song.services.SongService;
import com.softuni.musichub.song.staticData.SongConstants;
import com.softuni.musichub.staticData.Constants;
import com.softuni.musichub.user.entities.User;
import com.softuni.musichub.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/songs")
public class SongController {

    private final CategoryService categoryService;

    private final SongService songService;

    private final FileUtil fileUtil;

    @Autowired
    public SongController(CategoryService categoryService,
                          SongService songService,
                          FileUtil fileUtil) {
        this.categoryService = categoryService;
        this.songService = songService;
        this.fileUtil = fileUtil;
    }

    @ExceptionHandler(SongNotFoundException.class)
    public String handleSongNotFoundException() {
        return "redirect:/";
    }

    @GetMapping("/upload")
    public ModelAndView getUploadSongPage(ModelAndView modelAndView, Model model) {
        if (!model.asMap().containsKey(SongConstants.UPLOAD_SONG)) {
            UploadSong uploadSong = new UploadSong();
            modelAndView.addObject(SongConstants.UPLOAD_SONG, uploadSong);
        }

        List<CategoryView> categories = this.categoryService.findAll();
        modelAndView.addObject(SongConstants.VALIDATE_UPLOAD_SONG_JS_ENABLED, "");
        modelAndView.addObject(CategoryConstants.CATEGORIES, categories);
        modelAndView.addObject(Constants.TITLE, SongConstants.UPLOAD_SONG_TITLE);
        modelAndView.addObject(Constants.VIEW, SongConstants.UPLOAD_SONG_VIEW);
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
            redirectAttributes.addFlashAttribute(SongConstants.UPLOAD_SONG, uploadSong);
            String bindingResultKey = Constants.BINDING_RESULT_PACKAGE
                    + SongConstants.UPLOAD_SONG;
            redirectAttributes.addFlashAttribute(bindingResultKey, bindingResult);
            modelAndView.setViewName("redirect:/songs/upload");
            return modelAndView;
        }

        MultipartFile songTempFile = uploadSong.getFile();
        byte[] songTempFileBytes = songTempFile.getBytes();
        String songTempFileName = songTempFile.getOriginalFilename();
        // Create persisted file to upload in CDN after
        // this request is finished
        File songPersistedFile = this.fileUtil
                .createFile(songTempFileBytes, songTempFileName);
        uploadSong.setPersistedFile(songPersistedFile);
        this.songService.upload(uploadSong, user);
        redirectAttributes.addFlashAttribute(Constants.INFO, SongConstants.UPLOAD_SONG_SOON);
        modelAndView.setViewName("redirect:/songs/upload");
        return modelAndView;
    }

    @GetMapping("/details/{id}")
    public ModelAndView getSongDetailsPage(ModelAndView modelAndView,
                                           @PathVariable Long id) throws Exception {
        SongDetailsView songDetailsView = this.songService.getDetailsById(id);
        modelAndView.addObject(SongConstants.AUDIO_JS_STYLE_ENABLED, "");
        modelAndView.addObject(CommentConstants.POST_COMMENTS_JS_ENABLED, "");
        modelAndView.addObject(SongConstants.SONG_DETAILS, songDetailsView);
        modelAndView.addObject(Constants.TITLE, SongConstants.SONG_DETAILS_TITLE);
        modelAndView.addObject(Constants.VIEW, SongConstants.SONG_DETAILS_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView getDeleteSongPage(ModelAndView modelAndView,
                                          @PathVariable Long id) {
        SongView songView = this.songService.findById(id);
        modelAndView.addObject(Constants.TITLE, SongConstants.DELETE_SONG_TITLE);
        modelAndView.addObject(Constants.VIEW, SongConstants.DELETE_SONG_VIEW);
        modelAndView.addObject(SongConstants.DELETE_SONG, songView);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    public ModelAndView deleteSong(ModelAndView modelAndView,
                                   @PathVariable Long id) throws Exception {
        this.songService.deleteById(id);
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditSongPage(ModelAndView modelAndView,
                                        @PathVariable Long id,
                                        Model model) {
        EditSong editSong;
        if (!model.asMap().containsKey(SongConstants.EDIT_SONG)) {
            editSong = this.songService.getEditSongById(id);
        } else {
            editSong = (EditSong) model.asMap().get(SongConstants.EDIT_SONG);
        }

        List<CategoryView> categories = this.categoryService.findAll();
        modelAndView.addObject(CategoryConstants.CATEGORIES, categories);
        modelAndView.addObject(SongConstants.EDIT_SONG, editSong);
        modelAndView.addObject(Constants.TITLE, SongConstants.EDIT_SONG_TITLE);
        modelAndView.addObject(Constants.VIEW, SongConstants.EDIT_SONG_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editSong(ModelAndView modelAndView,
                                 @PathVariable Long id,
                                 @Valid @ModelAttribute EditSong editSong,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(SongConstants.EDIT_SONG, editSong);
            String bindingResultKey = Constants.BINDING_RESULT_PACKAGE
                    + SongConstants.EDIT_SONG;
            redirectAttributes.addFlashAttribute(bindingResultKey, bindingResult);
            modelAndView.setViewName("redirect:/songs/edit/" + id);
            return modelAndView;
        }

        this.songService.edit(editSong, id);
        modelAndView.setViewName("redirect:/songs/details/" + id);
        return modelAndView;
    }
}
