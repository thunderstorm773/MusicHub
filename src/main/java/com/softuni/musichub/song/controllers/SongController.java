package com.softuni.musichub.song.controllers;

import com.softuni.musichub.admin.category.models.views.CategoryView;
import com.softuni.musichub.admin.category.services.CategoryExtractionService;
import com.softuni.musichub.admin.category.staticData.CategoryConstants;
import com.softuni.musichub.home.staticData.HomeConstants;
import com.softuni.musichub.song.comment.staticData.CommentConstants;
import com.softuni.musichub.song.exceptions.SongNotFoundException;
import com.softuni.musichub.song.models.bindingModels.EditSong;
import com.softuni.musichub.song.models.bindingModels.UploadSong;
import com.softuni.musichub.song.models.viewModels.SongDetailsView;
import com.softuni.musichub.song.models.viewModels.SongView;
import com.softuni.musichub.song.services.SongExtractionService;
import com.softuni.musichub.song.services.SongManipulationService;
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

    private final CategoryExtractionService categoryExtractionService;

    private final SongManipulationService songManipulationService;

    private final SongExtractionService songExtractionService;

    @Autowired
    public SongController(CategoryExtractionService categoryExtractionService,
                          SongManipulationService songManipulationService,
                          SongExtractionService songExtractionService) {
        this.categoryExtractionService = categoryExtractionService;
        this.songManipulationService = songManipulationService;
        this.songExtractionService = songExtractionService;
    }

    private void saveSongInFileSystem(UploadSong uploadSong, FileUtil fileUtil)
            throws IOException {
        MultipartFile songTempFile = uploadSong.getFile();
        byte[] songTempFileBytes = songTempFile.getBytes();
        String songTempFileName = songTempFile.getOriginalFilename();
        File songPersistedFile = fileUtil.createFile(songTempFileBytes, songTempFileName);
        uploadSong.setPersistedFile(songPersistedFile);
    }

    @ExceptionHandler(SongNotFoundException.class)
    public String handleSongNotFoundException() {
        return "redirect:" + HomeConstants.INDEX_ROUTE;
    }

    @GetMapping("/upload")
    public ModelAndView getUploadSongPage(ModelAndView modelAndView, Model model) {
        if (!model.asMap().containsKey(SongConstants.UPLOAD_SONG)) {
            UploadSong uploadSong = new UploadSong();
            modelAndView.addObject(SongConstants.UPLOAD_SONG, uploadSong);
        }

        List<CategoryView> categories = this.categoryExtractionService.findAll();
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
                                   @AuthenticationPrincipal User user,
                                   FileUtil fileUtil) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(SongConstants.UPLOAD_SONG, uploadSong);
            String bindingResultKey = Constants.BINDING_RESULT_PACKAGE
                    + SongConstants.UPLOAD_SONG;
            redirectAttributes.addFlashAttribute(bindingResultKey, bindingResult);
            modelAndView.setViewName("redirect:" + SongConstants.SONG_UPLOAD_ROUTE);
            return modelAndView;
        }

        // Create persisted file to upload in CDN
        // and then set this file as field in uploadSong
        this.saveSongInFileSystem(uploadSong, fileUtil);
        this.songManipulationService.upload(uploadSong, user);
        redirectAttributes.addFlashAttribute(Constants.INFO, SongConstants.UPLOAD_SONG_SOON);
        modelAndView.setViewName("redirect:" + SongConstants.SONG_UPLOAD_ROUTE);
        return modelAndView;
    }

    @GetMapping("/details/{id}")
    public ModelAndView getSongDetailsPage(ModelAndView modelAndView,
                                           @PathVariable Long id) throws Exception {
        SongDetailsView songDetailsView = this.songExtractionService.getDetailsById(id);
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
        SongView songView = this.songExtractionService.findById(id);
        modelAndView.addObject(Constants.TITLE, SongConstants.DELETE_SONG_TITLE);
        modelAndView.addObject(Constants.VIEW, SongConstants.DELETE_SONG_VIEW);
        modelAndView.addObject(SongConstants.DELETE_SONG, songView);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    public ModelAndView deleteSong(ModelAndView modelAndView,
                                   @PathVariable Long id) throws Exception {
        this.songManipulationService.deleteById(id);
        modelAndView.setViewName("redirect:" + HomeConstants.INDEX_ROUTE);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditSongPage(ModelAndView modelAndView,
                                        @PathVariable Long id,
                                        Model model) {
        EditSong editSong;
        if (!model.asMap().containsKey(SongConstants.EDIT_SONG)) {
            editSong = this.songExtractionService.getEditSongById(id);
        } else {
            editSong = (EditSong) model.asMap().get(SongConstants.EDIT_SONG);
        }

        List<CategoryView> categories = this.categoryExtractionService.findAll();
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
            modelAndView.setViewName("redirect:" +
                    SongConstants.EDIT_SONG_BASE_ROUTE + id);
            return modelAndView;
        }

        this.songManipulationService.edit(editSong, id);
        modelAndView.setViewName("redirect:" +
                SongConstants.SONG_DETAILS_BASE_ROUTE + id);
        return modelAndView;
    }
}
