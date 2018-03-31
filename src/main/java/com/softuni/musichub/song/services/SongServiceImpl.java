package com.softuni.musichub.song.services;

import com.softuni.musichub.admin.category.entities.Category;
import com.softuni.musichub.admin.category.models.views.CategoryView;
import com.softuni.musichub.admin.category.services.CategoryService;
import com.softuni.musichub.song.comment.models.viewModels.CommentView;
import com.softuni.musichub.song.entities.Song;
import com.softuni.musichub.song.exceptions.SongNotFoundException;
import com.softuni.musichub.song.models.bindingModels.EditSong;
import com.softuni.musichub.song.models.bindingModels.UploadSong;
import com.softuni.musichub.song.models.viewModels.SongDetailsView;
import com.softuni.musichub.song.models.viewModels.SongView;
import com.softuni.musichub.song.repositories.SongRepository;
import com.softuni.musichub.song.tag.entities.Tag;
import com.softuni.musichub.song.tag.models.bindingModels.AddTag;
import com.softuni.musichub.song.tag.models.viewModels.TagView;
import com.softuni.musichub.song.tag.services.TagService;
import com.softuni.musichub.user.entities.User;
import com.softuni.musichub.util.CDNUtil;
import com.softuni.musichub.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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

    private Set<String> mapTagNames(Set<Tag> tags) {
        return tags.stream().map(Tag::getName).collect(Collectors.toSet());
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

    private void sortCommentsByPublishedDateDesc(SongDetailsView songDetailsView) {
        List<CommentView> commentViews = songDetailsView.getComments();
        Comparator<CommentView> commentViewComparator = (c1, c2) ->
            c2.getPublishedOn().compareTo(c1.getPublishedOn());

        List<CommentView> sortedComments = commentViews.stream().sorted(commentViewComparator)
                .collect(Collectors.toList());
        songDetailsView.setComments(sortedComments);
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
    public SongDetailsView getDetailsById(Long songId) throws Exception {
        Song song = this.songRepository.findById(songId).orElse(null);
        if (song == null) {
            throw new SongNotFoundException();
        }

        SongDetailsView songDetailsView = this.mapperUtil.getModelMapper()
                .map(song, SongDetailsView.class);
        this.sortCommentsByPublishedDateDesc(songDetailsView);
        String songPartialUrl = song.getSongPartialUrl();
        String songDownloadUrl = this.cdnUtil
                .getResourceDownloadUrl(songPartialUrl, CDNUtil.VIDEO_RESOURCE_TYPE);
        songDetailsView.setDownloadUrl(songDownloadUrl);
        String songStreamingUrl = this.cdnUtil
                .getResourceFullUrl(songPartialUrl, CDNUtil.VIDEO_RESOURCE_TYPE);
        songDetailsView.setStreamingUrl(songStreamingUrl);
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

    @Override
    public SongView findById(Long id) throws SongNotFoundException {
        Song song = this.songRepository.findById(id).orElse(null);
        if (song == null) {
            throw new SongNotFoundException();
        }

        SongView songView = this.mapperUtil.getModelMapper().map(song, SongView.class);
        return songView;
    }

    @Override
    public void deleteById(Long songId) throws Exception {
        Song song = this.songRepository.findById(songId).orElse(null);
        if (song == null) {
            throw new SongNotFoundException();
        }

        String songPartialUrl = song.getSongPartialUrl();
        this.cdnUtil.deleteResource(songPartialUrl);
        this.songRepository.delete(song);
    }

    @Override
    public EditSong getEditSongById(Long songId) throws SongNotFoundException {
        Song song = this.songRepository.findById(songId).orElse(null);
        if (song == null) {
            throw new SongNotFoundException();
        }

        EditSong editSong = this.mapperUtil.getModelMapper().map(song, EditSong.class);
        Set<Tag> tags = song.getTags();
        Set<String> tagNames = this.mapTagNames(tags);
        String concatenatedTagNames = String.join(", ", tagNames);
        editSong.setTagNames(concatenatedTagNames);
        return editSong;
    }

    @Override
    public void edit(EditSong editSong, Long songId) throws SongNotFoundException {
        Song song = this.songRepository.findById(songId).orElse(null);
        if (song == null) {
            throw new SongNotFoundException();
        }

        Long newCategoryId = editSong.getCategoryId();
        CategoryView categoryView = this.categoryService.findById(newCategoryId);
        if (categoryView == null) {
            return;
        }

        String newTitle = editSong.getTitle();
        Category newCategory = this.mapperUtil.getModelMapper()
                .map(categoryView, Category.class);
        String tagNames = editSong.getTagNames();
        if (tagNames == null) {
            return;
        }

        Set<Tag> newTags = this.getTags(tagNames);
        song.setTitle(newTitle);
        song.setCategory(newCategory);
        song.setTags(newTags);
    }

    @Override
    public boolean isSongExists(Long songId) {
        return this.songRepository.existsById(songId);
    }
}
