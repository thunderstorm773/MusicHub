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
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final MapperUtil mapperUtil;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository,
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

    @Override
    public TagView findByName(String tagName) {
        Tag tag = this.tagRepository.findByName(tagName);
        if (tag == null) {
            return null;
        }

        return this.mapperUtil.getModelMapper().map(tag, TagView.class);
    }
}
