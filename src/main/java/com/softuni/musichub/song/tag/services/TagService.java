package com.softuni.musichub.song.tag.services;

import com.softuni.musichub.song.tag.models.bindingModels.AddTag;
import com.softuni.musichub.song.tag.models.viewModels.TagView;

public interface TagService {

    void save(AddTag addTag);

    TagView findByName(String tagName);
}
