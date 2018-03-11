package com.softuni.musichub.category.model.bindingModel;

import com.softuni.musichub.category.staticData.CategoryConstants;
import com.softuni.musichub.category.validation.Category;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EditCategory {

    private String name;

    public EditCategory() {
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
