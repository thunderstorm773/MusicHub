package com.softuni.musichub.home.controllers;

import com.google.gson.Gson;
import com.softuni.musichub.home.staticData.HomeConstants;
import com.softuni.musichub.song.models.viewModels.SongView;
import com.softuni.musichub.song.services.SongService;
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

@Controller
public class HomeController {

    private final SongService songService;

    private final Gson gson;

    @Autowired
    public HomeController(SongService songService, Gson gson) {
        this.songService = songService;
        this.gson = gson;
    }

    @GetMapping("/")
    public ModelAndView getHomePage(ModelAndView modelAndView,
                                    @RequestParam(value = HomeConstants.SONG_TITLE_SEARCH_KEY, required = false) String songTitle,
                                    @RequestParam(value = HomeConstants.CATEGORY_NAME_SEARCH_KEY, required = false) String categoryName,
                                    @RequestParam(value = HomeConstants.TAG_NAME_SEARCH_KEY, required = false) String tagName,
                                    @PageableDefault(size = HomeConstants.SONGS_PER_PAGE,
                                                   sort = HomeConstants.UPLOADED_ON,
                                                   direction = Sort.Direction.DESC) Pageable pageable) {
        Page<SongView> songsPage;
        if (songTitle == null && categoryName == null && tagName == null) {
            songsPage = this.songService.findAll(pageable);
        } else if (songTitle != null) {
            songsPage = this.songService.findAllByTitle(songTitle, pageable);
        } else if (categoryName != null) {
            songsPage = this.songService.findAllByCategoryName(categoryName, pageable);
        } else {
            songsPage = this.songService.findAllByTagName(tagName, pageable);
        }

        Integer songsCount = songsPage.getNumberOfElements();
        Integer pageNumber = pageable.getPageNumber();
        if (songsCount == 0 && (pageNumber != 0)) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        modelAndView.addObject(HomeConstants.BROWSE_SONGS_STYLE_ENABLED, "");
        modelAndView.addObject(HomeConstants.ASYNC_LOAD_SONGS_JS_ENABLED, "");
        modelAndView.addObject(Constants.PAGE, songsPage);
        modelAndView.addObject(Constants.TITLE, HomeConstants.HOME_TITLE);
        modelAndView.addObject(Constants.VIEW, HomeConstants.HOME_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @GetMapping("/songs/browse/js")
    @ResponseBody
    public String getSongsAsJson(String songTitle,
                                 @PageableDefault(size = HomeConstants.SONGS_PER_PAGE,
                                         sort = HomeConstants.UPLOADED_ON,
                                         direction = Sort.Direction.DESC) Pageable pageable) {
        Page<SongView> songsPage;
        if (songTitle == null) {
            songsPage = this.songService.findAll(pageable);
        } else {
            songsPage = this.songService.findAllByTitle(songTitle, pageable);
        }

        return this.gson.toJson(songsPage);
    }

    @GetMapping("/index")
    public ModelAndView redirectToHomePage(ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @GetMapping("/about-us")
    public ModelAndView getAboutUsPage(ModelAndView modelAndView) {
        modelAndView.addObject(Constants.TITLE, HomeConstants.ABOUT_US_TITLE);
        modelAndView.addObject(Constants.VIEW, HomeConstants.ABOUT_US_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }
}
