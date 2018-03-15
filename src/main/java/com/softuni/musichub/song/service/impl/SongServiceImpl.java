package com.softuni.musichub.song.service.impl;

import com.softuni.musichub.category.entity.Category;
import com.softuni.musichub.category.model.view.CategoryView;
import com.softuni.musichub.category.service.api.CategoryService;
import com.softuni.musichub.song.entity.Song;
import com.softuni.musichub.song.model.bindingModel.UploadSong;
import com.softuni.musichub.song.repository.SongRepository;
import com.softuni.musichub.song.service.api.SongService;
import com.softuni.musichub.tag.entity.Tag;
import com.softuni.musichub.tag.model.bindingModel.AddTag;
import com.softuni.musichub.tag.model.viewModel.TagView;
import com.softuni.musichub.tag.service.api.TagService;
import com.softuni.musichub.user.entity.User;
import com.softuni.musichub.utils.CDNUtil;
import com.softuni.musichub.utils.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    private final CategoryService categoryService;

    private final TagService tagService;

    private final MapperUtil mapperUtil;

    private final CDNUtil cdnUtil;

    @Autowired
    public SongServiceImpl(SongRepository songRepository,
                           CategoryService categoryService,
                           TagService tagService,
                           MapperUtil mapperUtil,
                           CDNUtil cdnUtil) {
        this.songRepository = songRepository;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.mapperUtil = mapperUtil;
        this.cdnUtil = cdnUtil;
    }

    private Set<Tag> getTags(String tagsAsString) {
        Set<Tag> tags = new HashSet<>();
        if (tagsAsString.trim().length() > 0) {
            String[] tagTokens = tagsAsString.split(",\\s*");
            for (String tagName : tagTokens) {
                TagView tagView = this.tagService.findByName(tagName);
                if (tagView == null) {
                    AddTag addTag = new AddTag();
                    addTag.setName(tagName);
                    this.tagService.save(addTag);
                    tagView = this.tagService.findByName(tagName);
                }

                Tag tag = this.mapperUtil.getModelMapper().map(tagView, Tag.class);
                tags.add(tag);
            }
        }

        return tags;
    }

    @Override
    public void upload(UploadSong uploadSong, User user) throws IOException {
        Song song = this.mapperUtil.getModelMapper().map(uploadSong, Song.class);
        song.setId(null);
        Long categoryId = uploadSong.getCategoryId();
        CategoryView categoryView = this.categoryService.findById(categoryId);
        if (categoryView == null) {
            return;
        }

        Category category = this.mapperUtil.getModelMapper()
                .map(categoryView, Category.class);
        song.setCategory(category);
        String tagsAsString = uploadSong.getTagsAsString();
        if (tagsAsString != null) {
            Set<Tag> tags = this.getTags(tagsAsString);
            song.setTags(tags);
        }

        song.setUploader(user);
        MultipartFile songFile = uploadSong.getFile();
        Map uploadResult = this.cdnUtil.upload(songFile, CDNUtil.VIDEO_RESOURCE_TYPE,
                CDNUtil.SONGS_FOLDER);
        String songPartialUrl = (String) uploadResult.get(CDNUtil.PUBLIC_ID_KEY);
        song.setSongPartialUrl(songPartialUrl);
        this.songRepository.save(song);
    }
}
