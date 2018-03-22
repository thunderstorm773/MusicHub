package com.softuni.musichub.song.service.impl;

import com.softuni.musichub.category.entity.Category;
import com.softuni.musichub.category.model.view.CategoryView;
import com.softuni.musichub.category.service.api.CategoryService;
import com.softuni.musichub.song.entity.Song;
import com.softuni.musichub.song.exception.SongNotFoundException;
import com.softuni.musichub.song.model.bindingModel.UploadSong;
import com.softuni.musichub.song.model.viewModel.SongDetailsView;
import com.softuni.musichub.song.model.viewModel.SongView;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    private Page<SongView> constructSongViewPage(Page<Song> songPage) {
        List<Song> songs = songPage.getContent();
        List<SongView> songViews = this.mapperUtil.convertAll(songs, SongView.class);
        Pageable songsPageable = songPage.getPageable();
        Long totalSongsCount = songPage.getTotalElements();
        Page<SongView> songsViewPage = new PageImpl<>(songViews, songsPageable,
                totalSongsCount);
        return songsViewPage;
    }

    @Async
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
        File songFile = uploadSong.getPersistedFile();
        CompletableFuture<Map> taskResult = this.cdnUtil.upload(songFile, CDNUtil.VIDEO_RESOURCE_TYPE,
                CDNUtil.SONGS_FOLDER);
        CompletableFuture.anyOf(taskResult).join();
        Map uploadResult;
        try {
            uploadResult = taskResult.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }

        // Delete local file after it has been uploaded in CDN
        songFile.delete();
        String songPartialUrl = (String) uploadResult.get(CDNUtil.PUBLIC_ID_KEY);
        song.setSongPartialUrl(songPartialUrl);
        this.songRepository.save(song);
    }

    @Override
    public Page<SongView> findAll(Pageable pageable) {
        Page<Song> songPage = this.songRepository.findAll(pageable);
        return this.constructSongViewPage(songPage);
    }

    @Override
    public Page<SongView> findAllByTitle(String songTitle, Pageable pageable) {
        Page<Song> songPage = this.songRepository.findAllByTitle(songTitle, pageable);
        return this.constructSongViewPage(songPage);
    }

    @Override
    public SongDetailsView getDetailsById(Long songId) {
        Song song = this.songRepository.findById(songId).orElse(null);
        if (song == null) {
            throw new SongNotFoundException();
        }

        SongDetailsView songDetailsView = this.mapperUtil.getModelMapper()
                .map(song, SongDetailsView.class);
        return songDetailsView;
    }

    @Override
    public Page<SongView> findAllByCategoryName(String categoryName, Pageable pageable) {
        Page<Song> songPage = this.songRepository.findAllByCategoryName(categoryName, pageable);
        return this.constructSongViewPage(songPage);
    }

    @Override
    public Page<SongView> findAllByTagName(String tagName, Pageable pageable) {
        Page<Song> songPage = this.songRepository.findAllByTagName(tagName, pageable);
        return this.constructSongViewPage(songPage);
    }
}
