package com.tu.musichub.song.tag.services;

import com.tu.musichub.song.tag.entities.Tag;
import com.tu.musichub.song.tag.models.bindingModels.EditTag;
import com.tu.musichub.song.tag.models.viewModels.TagView;
import com.tu.musichub.song.tag.repositories.TagRepository;
import com.tu.musichub.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class TagExtractionServiceImpl implements TagExtractionService {

    private final TagRepository tagRepository;

    private final MapperUtil mapperUtil;

    @Autowired
    public TagExtractionServiceImpl(TagRepository tagRepository,
                                    MapperUtil mapperUtil) {
        this.tagRepository = tagRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public TagView findByName(String tagName) {
        Tag tag = this.tagRepository.findByName(tagName);
        if (tag == null) {
            return null;
        }

        return this.mapperUtil.getModelMapper().map(tag, TagView.class);
    }

    @Override
    public Page<TagView> findAll(Pageable pageable) {
        Page<Tag> tagPage = this.tagRepository.findAll(pageable);
        return this.mapperUtil.convertToPage(pageable, tagPage, TagView.class);
    }

    @Override
    public EditTag findById(Long id) {
        Tag tag = this.tagRepository.findById(id).orElse(null);
        if (tag == null) {
            return null;
        }

        return this.mapperUtil.getModelMapper().map(tag, EditTag.class);
    }
}
