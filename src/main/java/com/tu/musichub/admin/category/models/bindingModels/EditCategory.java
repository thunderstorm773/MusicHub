package com.tu.musichub.admin.category.models.bindingModels;

import com.tu.musichub.admin.category.staticData.CategoryConstants;
import com.tu.musichub.admin.category.validations.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class EditCategory {

    @NotBlank(message = CategoryConstants.CATEGORY_BLANK_MESSAGE)
    @Size(min = CategoryConstants.CATEGORY_MIN_LEN,
            message = CategoryConstants.CATEGORY_MIN_LEN_MESSAGE)
    @Category
    private String name;
}
