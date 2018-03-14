package com.softuni.musichub.tag.service.api;

import com.softuni.musichub.tag.model.bindingModel.AddTag;
import com.softuni.musichub.tag.model.viewModel.TagView;

public interface TagService {

    void save(AddTag addTag);

    TagView findByName(String tagName);
}
