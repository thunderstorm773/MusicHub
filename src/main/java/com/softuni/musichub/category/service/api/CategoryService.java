package com.softuni.musichub.category.service.api;

import com.softuni.musichub.category.model.bindingModel.AddCategory;
import com.softuni.musichub.category.model.bindingModel.EditCategory;
import com.softuni.musichub.category.model.view.CategoryView;
import java.util.List;

public interface CategoryService {

    void addCategory(AddCategory addCategory);

    CategoryView findByName(String categoryName);

    List<CategoryView> findAll();

    CategoryView findById(Long categoryId);

    void deleteById(Long categoryId);

    void edit(EditCategory editCategory, Long id);
}
