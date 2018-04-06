package com.softuni.musichub.home.controllers;

import com.google.gson.Gson;
import com.softuni.musichub.controller.BaseController;
import com.softuni.musichub.home.staticData.HomeConstants;
import com.softuni.musichub.song.models.viewModels.SongView;
import com.softuni.musichub.song.services.SongExtractionService;
import com.softuni.musichub.staticData.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController extends BaseController {

    private final SongExtractionService songExtractionService;

    private final Gson gson;

    @Autowired
    public HomeController(SongExtractionService songExtractionService, Gson gson) {
        this.songExtractionService = songExtractionService;
        this.gson = gson;
    }

    private Page<SongView> getSongPageBySearchParam(String songTitle, String categoryName,
                                                    String tagName, Pageable pageable) {
        Page<SongView> songsPage;
        if (songTitle == null && categoryName == null && tagName == null) {
            songsPage = this.songExtractionService.findAll(pageable);
        } else if (songTitle != null) {
            songsPage = this.songExtractionService.findAllByTitle(songTitle, pageable);
        } else if (categoryName != null) {
            songsPage = this.songExtractionService.findAllByCategoryName(categoryName, pageable);
        } else {
            songsPage = this.songExtractionService.findAllByTagName(tagName, pageable);
        }

        return songsPage;
    }

    @GetMapping("/")
    public ModelAndView getHomePage(@RequestParam(value = HomeConstants.SONG_TITLE, required = false) String songTitle,
                                    @RequestParam(value = HomeConstants.CATEGORY_NAME, required = false) String categoryName,
                                    @RequestParam(value = HomeConstants.TAG_NAME, required = false) String tagName,
                                    @PageableDefault(size = HomeConstants.SONGS_PER_PAGE,
                                            sort = HomeConstants.UPLOADED_ON,
                                            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<SongView> songsPage =
                this.getSongPageBySearchParam(songTitle, categoryName, tagName, pageable);
        Map<String, Object> objectByKey = new HashMap<>();
        objectByKey.put(HomeConstants.BROWSE_SONGS_STYLE_ENABLED, "");
        objectByKey.put(HomeConstants.ASYNC_LOAD_SONGS_JS_ENABLED, "");
        objectByKey.put(Constants.PAGE, songsPage);
        return this.view(HomeConstants.HOME_TITLE, HomeConstants.HOME_VIEW, objectByKey);
    }

    @GetMapping("/songs/browse/js")
    @ResponseBody
    public String getSongsAsJson(String songTitle,
                                 @PageableDefault(size = HomeConstants.SONGS_PER_PAGE,
                                         sort = HomeConstants.UPLOADED_ON,
                                         direction = Sort.Direction.DESC) Pageable pageable) {
        Page<SongView> songsPage =
                this.getSongPageBySearchParam(songTitle, null, null, pageable);
        return this.gson.toJson(songsPage);
    }

    @GetMapping("/about-us")
    public ModelAndView getAboutUsPage() {
        return this.view(HomeConstants.ABOUT_US_TITLE, HomeConstants.ABOUT_US_VIEW);
    }

    @GetMapping("/index")
    public ModelAndView redirectToHomePage() {
        return this.redirect(HomeConstants.INDEX_ROUTE);
    }
}
