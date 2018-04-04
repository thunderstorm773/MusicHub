package com.softuni.musichub.song.tag.services;

import com.softuni.musichub.song.tag.models.bindingModels.EditTag;
import com.softuni.musichub.song.tag.models.viewModels.TagView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagExtractionService {

    TagView findByName(String tagName);

    Page<TagView> findAll(Pageable pageable);

    EditTag findById(Long id);
}
