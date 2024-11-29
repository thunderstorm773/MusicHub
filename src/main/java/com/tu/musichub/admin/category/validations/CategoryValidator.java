package com.tu.musichub.admin.category.validations;

import com.tu.musichub.admin.category.models.views.CategoryView;
import com.tu.musichub.admin.category.services.CategoryExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class CategoryValidator implements ConstraintValidator<Category, String> {

    private final CategoryExtractionService categoryExtractionService;

    @Autowired
    public CategoryValidator(CategoryExtractionService categoryExtractionService) {
        this.categoryExtractionService = categoryExtractionService;
    }

    @Override
    public boolean isValid(String categoryName, ConstraintValidatorContext constraintValidatorContext) {
        CategoryView categoryView = this.categoryExtractionService.findByName(categoryName);
        return categoryView == null;
    }
}
