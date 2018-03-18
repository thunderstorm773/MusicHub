package com.softuni.musichub.song.controller;

import com.google.gson.Gson;
import com.softuni.musichub.category.model.view.CategoryView;
import com.softuni.musichub.category.service.api.CategoryService;
import com.softuni.musichub.category.staticData.CategoryConstants;
import com.softuni.musichub.song.model.bindingModel.UploadSong;
import com.softuni.musichub.song.model.viewModel.SongView;
import com.softuni.musichub.song.service.api.SongService;
import com.softuni.musichub.staticData.Constants;
import com.softuni.musichub.user.entity.User;
import com.softuni.musichub.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/songs")
public class SongController {

    private static final String UPLOAD_SONG_TITLE = "Upload Song";

    private static final String UPLOAD_SONG_VIEW = "song/upload";

    private static final String BROWSE_SONGS_TITLE = "Browse songs";

    private static final String BROWSE_SONGS_VIEW = "song/browse";

    private static final String UPLOAD_SONG = "uploadSong";

    private static final String UPLOAD_SONG_SOON = "Song will be uploaded soon!";

    private static final String SONGS_KEY = "songs";

    private static final int SONGS_PER_PAGE = 12;

    private final CategoryService categoryService;

    private final SongService songService;

    private final FileUtil fileUtil;

    private final Gson gson;

    @Autowired
    public SongController(CategoryService categoryService,
                          SongService songService,
                          FileUtil fileUtil, Gson gson) {
        this.categoryService = categoryService;
        this.songService = songService;
        this.fileUtil = fileUtil;
        this.gson = gson;
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

        MultipartFile songTempFile = uploadSong.getFile();
        byte[] songTempFileBytes = songTempFile.getBytes();
        String songTempFileName = songTempFile.getOriginalFilename();
        // Create persisted file to upload in CDN after
        // this request is finished
        File songPersistedFile = this.fileUtil
                .createFile(songTempFileBytes, songTempFileName);
        uploadSong.setPersistedFile(songPersistedFile);
        this.songService.upload(uploadSong, user);
        redirectAttributes.addFlashAttribute(Constants.INFO, UPLOAD_SONG_SOON);
        modelAndView.setViewName("redirect:/songs/upload");
        return modelAndView;
    }

    @GetMapping("/browse")
    public ModelAndView getBrowseSongsPage(ModelAndView modelAndView,
                                          @PageableDefault(size = SONGS_PER_PAGE) Pageable pageable) {
        Page<SongView> songsPage = this.songService.findAll(pageable);
        Integer songsCount = songsPage.getNumberOfElements();
        Integer pageNumber = pageable.getPageNumber();
        if (songsCount == 0 && (pageNumber != 0)) {
            modelAndView.setViewName("redirect:/songs/browse");
            return modelAndView;
        }

        modelAndView.addObject(SONGS_KEY, songsPage);
        modelAndView.addObject(Constants.TITLE, BROWSE_SONGS_TITLE);
        modelAndView.addObject(Constants.VIEW, BROWSE_SONGS_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }
    @GetMapping("/browse/js")
    @ResponseBody
    public String getSongsAsJson(@PageableDefault(size = SONGS_PER_PAGE) Pageable pageable) {
        Page<SongView> songsPage = this.songService.findAll(pageable);
        String songsAsJson = this.gson.toJson(songsPage);
        return songsAsJson;
    }
}
