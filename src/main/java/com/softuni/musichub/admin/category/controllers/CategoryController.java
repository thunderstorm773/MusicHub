package com.softuni.musichub.admin.category.controllers;

import com.softuni.musichub.admin.category.models.bindingModels.AddCategory;
import com.softuni.musichub.admin.category.models.bindingModels.EditCategory;
import com.softuni.musichub.admin.category.models.views.CategoryView;
import com.softuni.musichub.admin.category.services.CategoryExtractionService;
import com.softuni.musichub.admin.category.services.CategoryManipulationService;
import com.softuni.musichub.admin.category.staticData.CategoryConstants;
import com.softuni.musichub.admin.staticData.AdminConstants;
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

@Controller
@RequestMapping("/admin")
public class CategoryController {

    private final CategoryManipulationService categoryModifyingService;

    private final CategoryExtractionService categoryExtractionService;

    @Autowired
    public CategoryController(CategoryManipulationService categoryModifyingService,
                              CategoryExtractionService categoryExtractionService) {
        this.categoryModifyingService = categoryModifyingService;
        this.categoryExtractionService = categoryExtractionService;
    }

    @GetMapping("/categories/add")
    public ModelAndView getAddCategoryPage(ModelAndView modelAndView,
                                           Model model) {
        if (!model.asMap().containsKey(CategoryConstants.ADD_CATEGORY)) {
            AddCategory addCategory = new AddCategory();
            modelAndView.addObject(CategoryConstants.ADD_CATEGORY, addCategory);
        }

        modelAndView.addObject(Constants.TITLE, CategoryConstants.ADD_CATEGORY_TITLE);
        modelAndView.addObject(Constants.VIEW, CategoryConstants.ADD_CATEGORY_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/categories/add")
    public ModelAndView addCategory(ModelAndView modelAndView,
                                    @Valid @ModelAttribute AddCategory addCategory,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(CategoryConstants.ADD_CATEGORY, addCategory);
            String bindingResultKey = Constants.BINDING_RESULT_PACKAGE
                    + CategoryConstants.ADD_CATEGORY;
            redirectAttributes.addFlashAttribute(bindingResultKey, bindingResult);
            modelAndView.setViewName("redirect:/admin/categories/add");
            return modelAndView;
        }

        this.categoryModifyingService.addCategory(addCategory);
        modelAndView.setViewName("redirect:/admin/categories/all");
        return modelAndView;
    }

    @GetMapping("/categories/all")
    public ModelAndView getAllCategoriesPage(ModelAndView modelAndView,
                                             @PageableDefault(CategoryConstants.CATEGORIES_PER_PAGE) Pageable pageable) {
        Page<CategoryView> categories = this.categoryExtractionService.findAll(pageable);
        modelAndView.addObject(AdminConstants.TABLE_ACTIONS_STYLE_ENABLED, "");
        modelAndView.addObject(Constants.PAGE, categories);
        modelAndView.addObject(Constants.TITLE, CategoryConstants.ALL_CATEGORIES_TITLE);
        modelAndView.addObject(Constants.VIEW, CategoryConstants.ALL_CATEGORIES_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @GetMapping("/categories/{id}/delete")
    public ModelAndView getDeleteCategoryPage(ModelAndView modelAndView,
                                              @PathVariable Long id) {
        CategoryView category = this.categoryExtractionService.findById(id);
        if (category == null) {
            modelAndView.setViewName("redirect:/admin/categories/all");
            return modelAndView;
        }

        modelAndView.addObject(CategoryConstants.CATEGORY, category);
        modelAndView.addObject(Constants.TITLE, CategoryConstants.DELETE_CATEGORY_TITLE);
        modelAndView.addObject(Constants.VIEW, CategoryConstants.DELETE_CATEGORY_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/categories/{id}/delete")
    public ModelAndView deleteCategory(ModelAndView modelAndView,
                                       @PathVariable Long id) {
        this.categoryModifyingService.deleteById(id);
        modelAndView.setViewName("redirect:/admin/categories/all");
        return modelAndView;
    }

    @GetMapping("/categories/{id}/edit")
    public ModelAndView getEditCategoryPage(ModelAndView modelAndView,
                                            @PathVariable Long id) {
        CategoryView category = this.categoryExtractionService.findById(id);
        if (category == null) {
            modelAndView.setViewName("redirect:/admin/categories/all");
            return modelAndView;
        }

        modelAndView.addObject(CategoryConstants.EDIT_CATEGORY, category);
        modelAndView.addObject(Constants.TITLE, CategoryConstants.EDIT_CATEGORY_TITLE);
        modelAndView.addObject(Constants.VIEW, CategoryConstants.EDIT_CATEGORY_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/categories/{id}/edit")
    public ModelAndView editCategory(ModelAndView modelAndView,
                                     @PathVariable Long id,
                                     @Valid @ModelAttribute EditCategory editCategory,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject(Constants.TITLE, CategoryConstants.EDIT_CATEGORY_TITLE);
            modelAndView.addObject(Constants.VIEW, CategoryConstants.EDIT_CATEGORY_VIEW);
            modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
            return modelAndView;
        }

        this.categoryModifyingService.edit(editCategory, id);
        modelAndView.setViewName("redirect:/admin/categories/all");
        return modelAndView;
    }
}
