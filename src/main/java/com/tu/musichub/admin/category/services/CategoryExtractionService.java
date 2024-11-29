package com.tu.musichub.admin.category.services;

import com.tu.musichub.admin.category.models.views.CategoryView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CategoryExtractionService {

    CategoryView findByName(String categoryName);

    Page<CategoryView> findAll(Pageable pageable);

    List<CategoryView> findAll();

    CategoryView findById(Long categoryId);
}
