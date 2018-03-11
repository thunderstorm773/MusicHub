package com.softuni.musichub.category.validation;

import com.softuni.musichub.category.exception.CategoryNotFoundException;
import com.softuni.musichub.category.model.view.CategoryView;
import com.softuni.musichub.category.service.api.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class CategoryValidator implements ConstraintValidator<Category, String> {

    private final CategoryService categoryService;

    @Autowired
    public CategoryValidator(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public boolean isValid(String categoryName, ConstraintValidatorContext constraintValidatorContext) {
        try {
            CategoryView categoryView = this.categoryService.findByName(categoryName);
        } catch (CategoryNotFoundException e) {
            return true;
        }

        return false;
    }
}
