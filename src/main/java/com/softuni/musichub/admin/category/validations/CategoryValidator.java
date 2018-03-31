package com.softuni.musichub.admin.category.validations;

import com.softuni.musichub.admin.category.models.views.CategoryView;
import com.softuni.musichub.admin.category.services.CategoryService;
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
        CategoryView categoryView = this.categoryService.findByName(categoryName);
        return categoryView == null;
    }
}
