package com.tu.musichub.admin.category.services;

import com.tu.musichub.admin.category.models.bindingModels.AddCategory;
import com.tu.musichub.admin.category.models.bindingModels.EditCategory;
import com.tu.musichub.admin.category.models.views.CategoryView;

public interface CategoryManipulationService {

    CategoryView addCategory(AddCategory addCategory);

    boolean deleteById(Long categoryId);

    CategoryView edit(EditCategory editCategory, Long id);
}
