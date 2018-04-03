package com.softuni.musichub.song.tag.services;

import com.softuni.musichub.song.tag.entities.Tag;
import com.softuni.musichub.song.tag.models.bindingModels.AddTag;
import com.softuni.musichub.song.tag.models.viewModels.TagView;
import com.softuni.musichub.song.tag.repositories.TagRepository;
import com.softuni.musichub.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class TagManipulationServiceImpl implements TagManipulationService{

    private final TagRepository tagRepository;

    private final MapperUtil mapperUtil;

    @Autowired
    public TagManipulationServiceImpl(TagRepository tagRepository,
                                      MapperUtil mapperUtil) {
        this.tagRepository = tagRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public TagView save(AddTag addTag) {
        Tag tag = this.mapperUtil.getModelMapper().map(addTag, Tag.class);
        Tag savedTag = this.tagRepository.save(tag);
        return this.mapperUtil.getModelMapper().map(savedTag, TagView.class);
    }
}
