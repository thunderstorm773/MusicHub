package com.softuni.musichub.admin.category.validations;

import com.softuni.musichub.admin.category.staticData.CategoryConstants;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = CategoryValidator.class)
public @interface Category {

    String message() default CategoryConstants.CATEGORY_EXISTS_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
