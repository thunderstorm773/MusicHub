package com.softuni.musichub.admin.controller;

import com.softuni.musichub.category.model.bindingModel.AddCategory;
import com.softuni.musichub.category.model.bindingModel.EditCategory;
import com.softuni.musichub.category.model.view.CategoryView;
import com.softuni.musichub.category.service.api.CategoryService;
import com.softuni.musichub.category.staticData.CategoryConstants;
import com.softuni.musichub.staticData.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String ADD_CATEGORY_TITLE = "Add category";

    private static final String ADD_CATEGORY_VIEW = "admin/category/add";

    private static final String ADD_CATEGORY = "addCategory";

    private static final String CATEGORY = "category";

    private static final String EDIT_CATEGORY = "editCategory";

    private static final String ALL_CATEGORIES_TITLE = "All categories";

    private static final String ALL_CATEGORIES_VIEW = "/admin/category/all";

    private static final String DELETE_CATEGORY_TITLE = "Delete category";

    private static final String DELETE_CATEGORY_VIEW = "admin/category/delete";

    private static final String EDIT_CATEGORY_TITLE = "Edit category";

    private static final String EDIT_CATEGORY_VIEW = "admin/category/edit";

    private final CategoryService categoryService;

    @Autowired
    public AdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories/add")
    public ModelAndView getAddCategoryPage(ModelAndView modelAndView,
                                           Model model) {
        if (!model.asMap().containsKey(ADD_CATEGORY)) {
            AddCategory addCategory = new AddCategory();
            modelAndView.addObject(ADD_CATEGORY, addCategory);
        }

        modelAndView.addObject(Constants.TITLE, ADD_CATEGORY_TITLE);
        modelAndView.addObject(Constants.VIEW, ADD_CATEGORY_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/categories/add")
    public ModelAndView addCategory(ModelAndView modelAndView,
                                    @Valid @ModelAttribute AddCategory addCategory,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(ADD_CATEGORY, addCategory);
            String bindingResultKey = Constants.BINDING_RESULT_PACKAGE + ADD_CATEGORY;
            redirectAttributes.addFlashAttribute(bindingResultKey, bindingResult);
            modelAndView.setViewName("redirect:/admin/categories/add");
            return modelAndView;
        }

        this.categoryService.addCategory(addCategory);
        modelAndView.setViewName("redirect:/admin/categories/all");
        return modelAndView;
    }

    @GetMapping("/categories/all")
    public ModelAndView getAllCategoriesPage(ModelAndView modelAndView) {
        List<CategoryView> categories = this.categoryService.findAll();
        modelAndView.addObject(CategoryConstants.CATEGORIES, categories);
        modelAndView.addObject(Constants.TITLE, ALL_CATEGORIES_TITLE);
        modelAndView.addObject(Constants.VIEW, ALL_CATEGORIES_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @GetMapping("/categories/{id}/delete")
    public ModelAndView getDeleteCategoryPage(ModelAndView modelAndView,
                                              @PathVariable Long id) {
        CategoryView category = this.categoryService.findById(id);
        if (category == null) {
            modelAndView.setViewName("redirect:/admin/categories/all");
            return modelAndView;
        }

        modelAndView.addObject(CATEGORY, category);
        modelAndView.addObject(Constants.TITLE, DELETE_CATEGORY_TITLE);
        modelAndView.addObject(Constants.VIEW, DELETE_CATEGORY_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/categories/{id}/delete")
    public ModelAndView deleteCategory(ModelAndView modelAndView,
                                       @PathVariable Long id) {
        this.categoryService.deleteById(id);
        modelAndView.setViewName("redirect:/admin/categories/all");
        return modelAndView;
    }

    @GetMapping("/categories/{id}/edit")
    public ModelAndView getEditCategoryPage(ModelAndView modelAndView,
                                            @PathVariable Long id) {
        CategoryView category = this.categoryService.findById(id);
        if (category == null) {
            modelAndView.setViewName("redirect:/admin/categories/all");
            return modelAndView;
        }

        modelAndView.addObject(EDIT_CATEGORY, category);
        modelAndView.addObject(Constants.TITLE, EDIT_CATEGORY_TITLE);
        modelAndView.addObject(Constants.VIEW, EDIT_CATEGORY_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/categories/{id}/edit")
    public ModelAndView editCategory(ModelAndView modelAndView,
                                     @PathVariable Long id,
                                     @Valid @ModelAttribute EditCategory editCategory,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject(Constants.TITLE, EDIT_CATEGORY_TITLE);
            modelAndView.addObject(Constants.VIEW, EDIT_CATEGORY_VIEW);
            modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
            return modelAndView;
        }

        this.categoryService.edit(editCategory, id);
        modelAndView.setViewName("redirect:/admin/categories/all");
        return modelAndView;
    }
}
