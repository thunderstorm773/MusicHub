package com.softuni.musichub.admin.controller;

import com.softuni.musichub.admin.staticData.AdminConstants;
import com.softuni.musichub.category.model.bindingModel.AddCategory;
import com.softuni.musichub.category.model.bindingModel.EditCategory;
import com.softuni.musichub.category.model.view.CategoryView;
import com.softuni.musichub.category.service.api.CategoryService;
import com.softuni.musichub.category.staticData.CategoryConstants;
import com.softuni.musichub.staticData.Constants;
import com.softuni.musichub.user.exception.UserNotFoundException;
import com.softuni.musichub.user.model.bindingModel.EditUser;
import com.softuni.musichub.user.model.viewModel.RoleView;
import com.softuni.musichub.user.model.viewModel.UserView;
import com.softuni.musichub.user.service.api.RoleService;
import com.softuni.musichub.user.service.api.UserService;
import com.softuni.musichub.user.staticData.UserConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CategoryService categoryService;

    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public AdminController(CategoryService categoryService,
                           UserService userService,
                           RoleService roleService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException() {
        return "redirect:/admin/users/all";
    }

    @GetMapping("/categories/add")
    public ModelAndView getAddCategoryPage(ModelAndView modelAndView,
                                           Model model) {
        if (!model.asMap().containsKey(AdminConstants.ADD_CATEGORY)) {
            AddCategory addCategory = new AddCategory();
            modelAndView.addObject(AdminConstants.ADD_CATEGORY, addCategory);
        }

        modelAndView.addObject(Constants.TITLE, AdminConstants.ADD_CATEGORY_TITLE);
        modelAndView.addObject(Constants.VIEW, AdminConstants.ADD_CATEGORY_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/categories/add")
    public ModelAndView addCategory(ModelAndView modelAndView,
                                    @Valid @ModelAttribute AddCategory addCategory,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(AdminConstants.ADD_CATEGORY, addCategory);
            String bindingResultKey = Constants.BINDING_RESULT_PACKAGE
                    + AdminConstants.ADD_CATEGORY;
            redirectAttributes.addFlashAttribute(bindingResultKey, bindingResult);
            modelAndView.setViewName("redirect:/admin/categories/add");
            return modelAndView;
        }

        this.categoryService.addCategory(addCategory);
        modelAndView.setViewName("redirect:/admin/categories/all");
        return modelAndView;
    }

    @GetMapping("/categories/all")
    public ModelAndView getAllCategoriesPage(ModelAndView modelAndView,
                                             @PageableDefault(AdminConstants.CATEGORIES_PER_PAGE) Pageable pageable) {
        Page<CategoryView> categories = this.categoryService.findAll(pageable);
        Integer categoriesCount = categories.getNumberOfElements();
        Integer pageNumber = categories.getNumber();
        if (categoriesCount == 0 && (pageNumber != 0)) {
            modelAndView.setViewName("redirect:/admin/categories/all");
            return modelAndView;
        }

        modelAndView.addObject(AdminConstants.TABLE_ACTIONS_STYLE_ENABLED, "");
        modelAndView.addObject(Constants.PAGE, categories);
        modelAndView.addObject(Constants.TITLE, AdminConstants.ALL_CATEGORIES_TITLE);
        modelAndView.addObject(Constants.VIEW, AdminConstants.ALL_CATEGORIES_VIEW);
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

        modelAndView.addObject(CategoryConstants.CATEGORY, category);
        modelAndView.addObject(Constants.TITLE, AdminConstants.DELETE_CATEGORY_TITLE);
        modelAndView.addObject(Constants.VIEW, AdminConstants.DELETE_CATEGORY_VIEW);
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

        modelAndView.addObject(AdminConstants.EDIT_CATEGORY, category);
        modelAndView.addObject(Constants.TITLE, AdminConstants.EDIT_CATEGORY_TITLE);
        modelAndView.addObject(Constants.VIEW, AdminConstants.EDIT_CATEGORY_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/categories/{id}/edit")
    public ModelAndView editCategory(ModelAndView modelAndView,
                                     @PathVariable Long id,
                                     @Valid @ModelAttribute EditCategory editCategory,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject(Constants.TITLE, AdminConstants.EDIT_CATEGORY_TITLE);
            modelAndView.addObject(Constants.VIEW, AdminConstants.EDIT_CATEGORY_VIEW);
            modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
            return modelAndView;
        }

        this.categoryService.edit(editCategory, id);
        modelAndView.setViewName("redirect:/admin/categories/all");
        return modelAndView;
    }

    @GetMapping("/users/all")
    public ModelAndView getAllUsersPage(ModelAndView modelAndView,
                                        @PageableDefault(AdminConstants.USERS_PER_PAGE) Pageable pageable,
                                        @RequestParam(value = "username", required = false) String username) {
        Page<UserView> usersViewPage;
        if (username == null) {
            usersViewPage = this.userService.findAll(pageable);
        } else {
            usersViewPage = this.userService.findAllByUsernameContains(username, pageable);
        }

        Integer usersCount = usersViewPage.getNumberOfElements();
        Integer pageNumber = pageable.getPageNumber();
        if (usersCount == 0 && (pageNumber != 0)) {
            modelAndView.setViewName("redirect:/admin/users/all");
            return modelAndView;
        }

        modelAndView.addObject(AdminConstants.TABLE_ACTIONS_STYLE_ENABLED, "");
        modelAndView.addObject(Constants.PAGE, usersViewPage);
        modelAndView.addObject(Constants.TITLE, AdminConstants.ALL_USERS_TITLE);
        modelAndView.addObject(Constants.VIEW, AdminConstants.ALL_USERS_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @GetMapping("/users/delete/{username}")
    public ModelAndView getDeleteUserPage(ModelAndView modelAndView,
                                          @PathVariable String username) {
        UserView userView = this.userService.findByUsername(username);
        modelAndView.addObject(AdminConstants.DELETE_USER, userView);
        modelAndView.addObject(Constants.TITLE, AdminConstants.DELETE_USER_TITLE);
        modelAndView.addObject(Constants.VIEW, AdminConstants.DELETE_USER_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/users/delete/{username}")
    public ModelAndView deleteUser(ModelAndView modelAndView,
                                   @PathVariable String username,
                                   @AuthenticationPrincipal UserDetails loggedInUser,
                                   RedirectAttributes redirectAttributes) {
        String loggedInUserUsername = loggedInUser.getUsername();
        if (loggedInUserUsername.equals(username)) {
            redirectAttributes.addFlashAttribute(Constants.ERROR,
                    AdminConstants.USER_CANNOT_BE_DELETED_MESSAGE);
            modelAndView.setViewName("redirect:/admin/users/delete/" + username);
            return modelAndView;
        }

        this.userService.deleteByUsername(username);
        modelAndView.setViewName("redirect:/admin/users/all");
        return modelAndView;
    }

    @GetMapping("/users/edit/{username}")
    public ModelAndView getEditUserPage(ModelAndView modelAndView,
                                        Model model,
                                        @PathVariable String username) {
        UserView userView = this.userService.findByUsername(username);
        if (model.asMap().containsKey(AdminConstants.EDIT_USER)) {
            EditUser editUser = (EditUser) model.asMap().get(AdminConstants.EDIT_USER);
            Set<String> roleNames = editUser.getRoleNames();
            userView.setRoleNames(roleNames);
        }

        List<RoleView> roleViews = this.roleService.findAll();
        modelAndView.addObject(UserConstants.ROLES, roleViews);
        modelAndView.addObject(AdminConstants.EDIT_USER, userView);
        modelAndView.addObject(Constants.TITLE, AdminConstants.EDIT_USER_TITLE);
        modelAndView.addObject(Constants.VIEW, AdminConstants.EDIT_USER_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/users/edit/{username}")
    public ModelAndView editUser(ModelAndView modelAndView,
                                 @PathVariable String username,
                                 @Valid @ModelAttribute EditUser editUser,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(AdminConstants.EDIT_USER, editUser);
            redirectAttributes.addFlashAttribute(Constants.ERROR,
                    AdminConstants.NOT_SELECTED_ROLES_MESSAGE);
            modelAndView.setViewName("redirect:/admin/users/edit/" + username);
            return modelAndView;
        }

        this.userService.edit(editUser, username);
        modelAndView.setViewName("redirect:/admin/users/all");
        return modelAndView;
    }


}
