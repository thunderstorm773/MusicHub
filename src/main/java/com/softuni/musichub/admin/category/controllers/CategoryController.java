package com.softuni.musichub.admin.category.controllers;

import com.softuni.musichub.admin.category.models.bindingModels.AddCategory;
import com.softuni.musichub.admin.category.models.bindingModels.EditCategory;
import com.softuni.musichub.admin.category.models.views.CategoryView;
import com.softuni.musichub.admin.category.services.CategoryExtractionService;
import com.softuni.musichub.admin.category.services.CategoryManipulationService;
import com.softuni.musichub.admin.category.staticData.CategoryConstants;
import com.softuni.musichub.admin.staticData.AdminConstants;
import com.softuni.musichub.controller.BaseController;
import com.softuni.musichub.staticData.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    private final CategoryManipulationService categoryManipulationService;

    private final CategoryExtractionService categoryExtractionService;

    @Autowired
    public CategoryController(CategoryManipulationService categoryManipulationService,
                              CategoryExtractionService categoryExtractionService) {
        this.categoryManipulationService = categoryManipulationService;
        this.categoryExtractionService = categoryExtractionService;
    }

    @GetMapping("/categories/add")
    public ModelAndView getAddCategoryPage(Model model) {
        Map<String, Object> objectByKey = new HashMap<>();
        if (!model.asMap().containsKey(CategoryConstants.ADD_CATEGORY)) {
            objectByKey.put(CategoryConstants.ADD_CATEGORY, new AddCategory());
        }

        return this.view(CategoryConstants.ADD_CATEGORY_TITLE,
                CategoryConstants.ADD_CATEGORY_VIEW, objectByKey);
    }

    @PostMapping("/categories/add")
    public ModelAndView addCategory(@Valid @ModelAttribute AddCategory addCategory,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(CategoryConstants.ADD_CATEGORY, addCategory);
            String bindingResultKey = Constants.BINDING_RESULT_PACKAGE
                    + CategoryConstants.ADD_CATEGORY;
            redirectAttributes.addFlashAttribute(bindingResultKey, bindingResult);
            return this.redirect(CategoryConstants.ADD_CATEGORY_ROUTE);
        }

        this.categoryManipulationService.addCategory(addCategory);
        return this.redirect(CategoryConstants.ALL_CATEGORIES_ROUTE);
    }

    @GetMapping("/categories/all")
    public ModelAndView getAllCategoriesPage(@PageableDefault(CategoryConstants.CATEGORIES_PER_PAGE) Pageable pageable) {
        Page<CategoryView> categories = this.categoryExtractionService.findAll(pageable);
        Map<String, Object> objectByKey = new HashMap<>();
        objectByKey.put(AdminConstants.TABLE_ACTIONS_STYLE_ENABLED, "");
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
        return this.redirect(CategoryConstants.ALL_CATEGORIES_ROUTE);
    }
}
