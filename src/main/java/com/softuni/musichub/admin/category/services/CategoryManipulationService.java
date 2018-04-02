package com.softuni.musichub.admin.category.services;

import com.softuni.musichub.admin.category.models.bindingModels.AddCategory;
import com.softuni.musichub.admin.category.models.bindingModels.EditCategory;

public interface CategoryManipulationService {

    void addCategory(AddCategory addCategory);

    void deleteById(Long categoryId);

    void edit(EditCategory editCategory, Long id);
}
