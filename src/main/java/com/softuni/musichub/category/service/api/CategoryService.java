package com.softuni.musichub.category.service.api;

import com.softuni.musichub.category.model.bindingModel.AddCategory;
import com.softuni.musichub.category.model.bindingModel.EditCategory;
import com.softuni.musichub.category.model.view.CategoryView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    void addCategory(AddCategory addCategory);

    CategoryView findByName(String categoryName);

    Page<CategoryView> findAll(Pageable pageable);

    List<CategoryView> findAll();

    CategoryView findById(Long categoryId);

    void deleteById(Long categoryId);

    void edit(EditCategory editCategory, Long id);
}
