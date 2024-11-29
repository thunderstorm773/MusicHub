package com.tu.musichub.admin.category.models.bindingModels;

import com.tu.musichub.admin.category.staticData.CategoryConstants;
import com.tu.musichub.admin.category.validations.Category;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddCategory {

    private String name;

    public AddCategory() {
    }

    @NotBlank(message = CategoryConstants.CATEGORY_BLANK_MESSAGE)
    @Size(min = CategoryConstants.CATEGORY_MIN_LEN,
            message = CategoryConstants.CATEGORY_MIN_LEN_MESSAGE)
    @Category
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
