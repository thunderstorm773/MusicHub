package com.softuni.musichub.song.tag.services;

import com.softuni.musichub.song.tag.models.bindingModels.AddTag;
import com.softuni.musichub.song.tag.models.viewModels.TagView;

public interface TagManipulationService {

    TagView save(AddTag addTag);
}
