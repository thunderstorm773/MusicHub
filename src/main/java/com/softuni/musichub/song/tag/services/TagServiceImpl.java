package com.softuni.musichub.song.tag.services;

import com.softuni.musichub.song.tag.entities.Tag;
import com.softuni.musichub.song.tag.models.bindingModels.AddTag;
import com.softuni.musichub.song.tag.models.viewModels.TagView;
import com.softuni.musichub.song.tag.repositories.TagRepository;
import com.softuni.musichub.song.tag.services.TagService;
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
