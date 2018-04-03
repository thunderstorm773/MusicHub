package com.softuni.musichub.song.tag.services;

import com.softuni.musichub.song.tag.models.viewModels.TagView;

public interface TagExtractionService {

    TagView findByName(String tagName);
}
