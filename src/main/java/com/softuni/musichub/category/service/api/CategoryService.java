package com.softuni.musichub.category.service.api;

import com.softuni.musichub.category.exception.CategoryNotFoundException;
import com.softuni.musichub.category.model.bindingModel.AddCategory;
import com.softuni.musichub.category.model.bindingModel.EditCategory;
import com.softuni.musichub.category.model.view.CategoryView;
import java.util.List;

public interface CategoryService {

    void addCategory(AddCategory addCategory);

    CategoryView findByName(String categoryName)
            throws CategoryNotFoundException;

    List<CategoryView> findAll();

    CategoryView findById(Long categoryId) throws CategoryNotFoundException;

    void deleteById(Long categoryId) throws CategoryNotFoundException;

    void edit(EditCategory editCategory, Long id) throws CategoryNotFoundException;
}
