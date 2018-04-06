package com.softuni.musichub.song.services;

import com.softuni.musichub.admin.category.entities.Category;
import com.softuni.musichub.admin.category.models.views.CategoryView;
import com.softuni.musichub.admin.category.services.CategoryExtractionService;
import com.softuni.musichub.song.entities.Song;
import com.softuni.musichub.song.exceptions.SongNotFoundException;
import com.softuni.musichub.song.models.bindingModels.EditSong;
import com.softuni.musichub.song.models.bindingModels.UploadSong;
import com.softuni.musichub.song.repositories.SongRepository;
import com.softuni.musichub.song.tag.entities.Tag;
import com.softuni.musichub.song.tag.models.bindingModels.AddTag;
import com.softuni.musichub.song.tag.models.viewModels.TagView;
import com.softuni.musichub.song.tag.services.TagExtractionService;
import com.softuni.musichub.song.tag.services.TagManipulationService;
import com.softuni.musichub.song.tag.staticData.TagConstants;
import com.softuni.musichub.staticData.Constants;
import com.softuni.musichub.user.entities.User;
import com.softuni.musichub.util.CdnUtil;
import com.softuni.musichub.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class SongManipulationServiceImpl implements SongManipulationService {

    private final SongRepository songRepository;

    private final CdnUtil cdnUtil;

    private final MapperUtil mapperUtil;

    private final CategoryExtractionService categoryExtractionService;

    private final TagExtractionService tagExtractionService;

    private final TagManipulationService tagManipulationService;

    @Autowired
    public SongManipulationServiceImpl(SongRepository songRepository,
                                       CdnUtil cdnUtil,
                                       MapperUtil mapperUtil,
                                       CategoryExtractionService categoryExtractionService,
                                       TagExtractionService tagExtractionService, TagManipulationService tagManipulationService) {
        this.songRepository = songRepository;
        this.cdnUtil = cdnUtil;
        this.mapperUtil = mapperUtil;
        this.categoryExtractionService = categoryExtractionService;
        this.tagExtractionService = tagExtractionService;
        this.tagManipulationService = tagManipulationService;
    }

    private TagView createTag(String tagName) {
        AddTag addTag = new AddTag(tagName);
        return this.tagManipulationService.save(addTag);
    }

    private Set<Tag> getTagsByTagNames(String tagsAsString) {
        Set<Tag> tags = new HashSet<>();
        if (tagsAsString.trim().length() > 1) {
            String[] tagTokens = tagsAsString.split(",\\s*");
            for (String tagName : tagTokens) {
                if (tagName.length() < TagConstants.TAG_NAME_MIN_LEN) {
                    continue;
                }

                TagView tagView = this.tagExtractionService.findByName(tagName);
                if (tagView == null) {
                    tagView = this.createTag(tagName);
                }

                Tag tag = this.mapperUtil.getModelMapper().map(tagView, Tag.class);
                tags.add(tag);
            }
        }

        return tags;
    }

    private Map uploadSongInCloud(File songFile) throws IOException {
        CompletableFuture<Map> taskResult = this.cdnUtil.upload(songFile, CdnUtil.VIDEO_RESOURCE_TYPE,
                CdnUtil.SONGS_FOLDER);
        CompletableFuture.anyOf(taskResult).join();
        Map uploadResult;
        try {
            uploadResult = taskResult.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }

        return uploadResult;
    }

    @Async
    @Override
    public void upload(UploadSong uploadSong, User user) throws IOException {
        Song song = this.mapperUtil.getModelMapper().map(uploadSong, Song.class);
        song.setId(null);
        Long categoryId = uploadSong.getCategoryId();
        CategoryView categoryView = this.categoryExtractionService.findById(categoryId);
        if (categoryView == null) {
            return;
        }

        Category category = this.mapperUtil.getModelMapper().map(categoryView, Category.class);
        song.setCategory(category);
        String tagsAsString = uploadSong.getTagsAsString();
        if (tagsAsString != null) {
            Set<Tag> tags = this.getTagsByTagNames(tagsAsString);
            song.setTags(tags);
        }

        song.setUploader(user);
        File songFile = uploadSong.getPersistedFile();
        Map uploadResult = this.uploadSongInCloud(songFile);
        // Delete local file after it has been uploaded in CDN
        songFile.delete();
        String songPartialUrl = (String) uploadResult.get(CdnUtil.PUBLIC_ID);
        song.setSongPartialUrl(songPartialUrl);
        this.songRepository.save(song);
    }

    @CacheEvict(value = Constants.SONGS_CACHE_NAME, key = "#songId")
    @Override
    public void deleteById(Long songId) throws Exception {
        Song song = this.songRepository.findById(songId).orElse(null);
        if (song == null) {
            throw new SongNotFoundException();
        }

        String songPartialUrl = song.getSongPartialUrl();
        // First delete resource from cdn then info in DB
        this.cdnUtil.deleteResource(songPartialUrl);
        this.songRepository.delete(song);
    }

    @Override
    @CacheEvict(value = Constants.SONGS_CACHE_NAME, key = "#songId")
    public void edit(EditSong editSong, Long songId) throws SongNotFoundException {
        Song song = this.songRepository.findById(songId).orElse(null);
        if (song == null) {
            throw new SongNotFoundException();
        }

        Long newCategoryId = editSong.getCategoryId();
        CategoryView categoryView = this.categoryExtractionService.findById(newCategoryId);
        if (categoryView == null) {
            return;
        }

        String newTitle = editSong.getTitle();
        Category newCategory = this.mapperUtil.getModelMapper().map(categoryView, Category.class);
        String tagNames = editSong.getTagNames();
        if (tagNames == null) {
            return;
        }

        Set<Tag> newTags = this.getTagsByTagNames(tagNames);
        song.setTitle(newTitle);
        song.setCategory(newCategory);
        song.setTags(newTags);
    }
}
