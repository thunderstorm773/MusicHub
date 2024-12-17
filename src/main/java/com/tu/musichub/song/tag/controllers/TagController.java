package com.tu.musichub.song.tag.controllers;

import com.tu.musichub.controller.BaseController;
import com.tu.musichub.song.tag.models.bindingModels.AddTag;
import com.tu.musichub.song.tag.models.bindingModels.EditTag;
import com.tu.musichub.song.tag.models.viewModels.TagView;
import com.tu.musichub.song.tag.services.TagExtractionServiceImpl;
import com.tu.musichub.song.tag.services.TagManipulationServiceImpl;
import com.tu.musichub.song.tag.staticData.TagConstants;
import com.tu.musichub.staticData.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/tags")
public class TagController extends BaseController {

    private final TagExtractionServiceImpl tagExtractionService;

    private final TagManipulationServiceImpl tagManipulationService;

    @Autowired
    public TagController(TagExtractionServiceImpl tagExtractionService,
                         TagManipulationServiceImpl tagManipulationService) {
        this.tagExtractionService = tagExtractionService;
        this.tagManipulationService = tagManipulationService;
    }

    @GetMapping("/all")
    public ModelAndView getAllTagsPage(@PageableDefault(TagConstants.TAGS_PER_PAGE) Pageable pageable) {
        Page<TagView> tagViewPage = this.tagExtractionService.findAll(pageable);
        Map<String, Object> objectByKey = new HashMap<>();
        objectByKey.put(Constants.PAGE, tagViewPage);
        objectByKey.put(Constants.TABLE_ACTIONS_STYLE_ENABLED, "");
        return this.view(TagConstants.ALL_TAGS_TITLE,
                TagConstants.ALL_TAGS_VIEW, objectByKey);
    }

    @GetMapping("/add")
    public ModelAndView getAddTagPage(@ModelAttribute AddTag addTag) {
        return this.view(TagConstants.ADD_TAG_TITLE, TagConstants.ADD_TAG_VIEW);
    }

    @PostMapping("/add")
    public ModelAndView addTag(@Valid @ModelAttribute AddTag addTag,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return this.view(TagConstants.ADD_TAG_TITLE, TagConstants.ADD_TAG_VIEW);
        }

        this.tagManipulationService.save(addTag);
        redirectAttributes.addFlashAttribute(Constants.INFO, TagConstants.TAG_ADDED_MESSAGE);
        return this.redirect(TagConstants.ALL_TAGS_ROUTE);
    }

    @GetMapping("/{id}/edit")
    public ModelAndView getEditTagPage(@PathVariable Long id) {
        EditTag editTag = this.tagExtractionService.findById(id);
        if (editTag == null) {
            return this.redirect(TagConstants.ALL_TAGS_ROUTE);
        }

        Map<String, Object> objectByKey = new HashMap<>();
        objectByKey.put(TagConstants.EDIT_TAG, editTag);
        return this.view(TagConstants.EDIT_TAG_TITLE,
                TagConstants.EDIT_TAG_VIEW, objectByKey);
    }

    @PostMapping("/{id}/edit")
    public ModelAndView editTag(@PathVariable Long id,
                                @Valid @ModelAttribute EditTag editTag,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return this.view(TagConstants.EDIT_TAG_TITLE, TagConstants.EDIT_TAG_VIEW);
        }

        this.tagManipulationService.edit(editTag, id);
        redirectAttributes.addFlashAttribute(Constants.INFO, TagConstants.TAG_EDITED_MESSAGE);
        return this.redirect(TagConstants.ALL_TAGS_ROUTE);
    }
}
