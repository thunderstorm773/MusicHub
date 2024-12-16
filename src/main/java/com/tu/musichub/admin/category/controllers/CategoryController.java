package com.tu.musichub.admin.category.controllers;

import com.tu.musichub.admin.category.models.bindingModels.AddCategory;
import com.tu.musichub.admin.category.models.bindingModels.EditCategory;
import com.tu.musichub.admin.category.models.views.CategoryView;
import com.tu.musichub.admin.category.services.CategoryExtractionServiceImpl;
import com.tu.musichub.admin.category.services.CategoryManipulationServiceImpl;
import com.tu.musichub.admin.category.staticData.CategoryConstants;
import com.tu.musichub.controller.BaseController;
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
@RequestMapping("/admin")
public class CategoryController extends BaseController {

    private final CategoryManipulationServiceImpl categoryManipulationService;

    private final CategoryExtractionServiceImpl categoryExtractionService;

    @Autowired
    public CategoryController(CategoryManipulationServiceImpl categoryManipulationService,
                              CategoryExtractionServiceImpl categoryExtractionService) {
        this.categoryManipulationService = categoryManipulationService;
        this.categoryExtractionService = categoryExtractionService;
    }

    @GetMapping("/categories/add")
    public ModelAndView getAddCategoryPage(@ModelAttribute AddCategory addCategory) {
        return this.view(CategoryConstants.ADD_CATEGORY_TITLE,
                CategoryConstants.ADD_CATEGORY_VIEW);
    }

    @PostMapping("/categories/add")
    public ModelAndView addCategory(@Valid @ModelAttribute AddCategory addCategory,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return this.view(CategoryConstants.ADD_CATEGORY_TITLE,
                    CategoryConstants.ADD_CATEGORY_VIEW);
        }

        this.categoryManipulationService.addCategory(addCategory);
        redirectAttributes.addFlashAttribute(Constants.INFO,
                CategoryConstants.CATEGORY_CREATED_MESSAGE);
        return this.redirect(CategoryConstants.ADD_CATEGORY_ROUTE);
    }

    @GetMapping("/categories/all")
    public ModelAndView getAllCategoriesPage(@PageableDefault(CategoryConstants.CATEGORIES_PER_PAGE) Pageable pageable) {
        Page<CategoryView> categories = this.categoryExtractionService.findAll(pageable);
        Map<String, Object> objectByKey = new HashMap<>();
        objectByKey.put(Constants.TABLE_ACTIONS_STYLE_ENABLED, "");
        objectByKey.put(Constants.PAGE, categories);
        return this.view(CategoryConstants.ALL_CATEGORIES_TITLE,
                CategoryConstants.ALL_CATEGORIES_VIEW, objectByKey);
    }

    @GetMapping("/categories/{id}/delete")
    public ModelAndView getDeleteCategoryPage(@PathVariable Long id) {
        CategoryView category = this.categoryExtractionService.findById(id);
        if (category == null) {
            return this.redirect(CategoryConstants.ALL_CATEGORIES_ROUTE);
        }

        Map<String, Object> objectByKey = new HashMap<>();
        objectByKey.put(CategoryConstants.CATEGORY, category);
        return this.view(CategoryConstants.DELETE_CATEGORY_TITLE,
                CategoryConstants.DELETE_CATEGORY_VIEW, objectByKey);
    }

    @PostMapping("/categories/{id}/delete")
    public ModelAndView deleteCategory(@PathVariable Long id) {
        this.categoryManipulationService.deleteById(id);
        return this.redirect(CategoryConstants.ALL_CATEGORIES_ROUTE);
    }

    @GetMapping("/categories/{id}/edit")
    public ModelAndView getEditCategoryPage(@PathVariable Long id) {
        CategoryView category = this.categoryExtractionService.findById(id);
        if (category == null) {
            return this.redirect(CategoryConstants.ALL_CATEGORIES_ROUTE);
        }

        Map<String, Object> objectByKey = new HashMap<>();
        objectByKey.put(CategoryConstants.EDIT_CATEGORY, category);
        return this.view(CategoryConstants.EDIT_CATEGORY_TITLE,
                CategoryConstants.EDIT_CATEGORY_VIEW, objectByKey);
    }

    @PostMapping("/categories/{id}/edit")
    public ModelAndView editCategory(@PathVariable Long id,
                                     @Valid @ModelAttribute EditCategory editCategory,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.view(CategoryConstants.EDIT_CATEGORY_TITLE,
                    CategoryConstants.EDIT_CATEGORY_VIEW);
        }

        this.categoryManipulationService.edit(editCategory, id);
        Map<String, Object> objectByKey = new HashMap<>();
        objectByKey.put(Constants.INFO, CategoryConstants.CATEGORY_EDITED_MESSAGE);
        return this.view(CategoryConstants.EDIT_CATEGORY_TITLE,
                CategoryConstants.EDIT_CATEGORY_VIEW, objectByKey);
    }
}
