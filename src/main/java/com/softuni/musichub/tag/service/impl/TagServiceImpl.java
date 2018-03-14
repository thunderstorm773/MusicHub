package com.softuni.musichub.tag.service.impl;

import com.softuni.musichub.tag.entity.Tag;
import com.softuni.musichub.tag.model.bindingModel.AddTag;
import com.softuni.musichub.tag.model.viewModel.TagView;
import com.softuni.musichub.tag.repository.TagRepository;
import com.softuni.musichub.tag.service.api.TagService;
import com.softuni.musichub.utils.MapperUtil;
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
    public void save(AddTag addTag) {
        Tag tag = this.mapperUtil.getModelMapper().map(addTag, Tag.class);
        this.tagRepository.save(tag);
    }

    @Override
    public TagView findByName(String tagName) {
        Tag tag = this.tagRepository.findByName(tagName);
        if (tag == null) {
            return null;
        }

        TagView tagView = this.mapperUtil.getModelMapper().map(tag, TagView.class);
        return tagView;
    }
}
