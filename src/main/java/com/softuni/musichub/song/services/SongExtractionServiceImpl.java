package com.softuni.musichub.song.services;

import com.softuni.musichub.song.comment.models.viewModels.CommentView;
import com.softuni.musichub.song.entities.Song;
import com.softuni.musichub.song.exceptions.SongNotFoundException;
import com.softuni.musichub.song.models.bindingModels.EditSong;
import com.softuni.musichub.song.models.viewModels.SongDetailsView;
import com.softuni.musichub.song.models.viewModels.SongView;
import com.softuni.musichub.song.repositories.SongRepository;
import com.softuni.musichub.song.staticData.SongConstants;
import com.softuni.musichub.song.tag.entities.Tag;
import com.softuni.musichub.util.CdnUtil;
import com.softuni.musichub.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SongExtractionServiceImpl implements SongExtractionService {

    private final SongRepository songRepository;

    private final MapperUtil mapperUtil;

    private final CdnUtil cdnUtil;

    @Autowired
    public SongExtractionServiceImpl(SongRepository songRepository,
                                     MapperUtil mapperUtil,
                                     CdnUtil cdnUtil) {
        this.songRepository = songRepository;
        this.mapperUtil = mapperUtil;
        this.cdnUtil = cdnUtil;
    }

    private Set<String> mapTagNames(Set<Tag> tags) {
        return tags.stream().map(Tag::getName).collect(Collectors.toSet());
    }

    private void sortCommentsByPublishedDateDesc(SongDetailsView songDetailsView) {
        List<CommentView> commentViews = songDetailsView.getComments();
        Comparator<CommentView> publishedDateComparator = (c1, c2) ->
                c2.getPublishedOn().compareTo(c1.getPublishedOn());

        List<CommentView> sortedComments = commentViews.stream().sorted(publishedDateComparator)
                .collect(Collectors.toList());
        songDetailsView.setComments(sortedComments);
    }

    @Override
    public Page<SongView> findAll(Pageable pageable) {
        Page<Song> songPage = this.songRepository.findAll(pageable);
        return this.mapperUtil.convertToPage(pageable, songPage, SongView.class);
    }

    @Override
    public Page<SongView> findAllByTitle(String songTitle, Pageable pageable) {
        Page<Song> songPage = this.songRepository.findAllByTitle(songTitle, pageable);
        return this.mapperUtil.convertToPage(pageable, songPage, SongView.class);
    }

    @Cacheable(value = SongConstants.SONGS_CACHE_NAME, key = "#songId")
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
                .getResourceDownloadUrl(songPartialUrl, CdnUtil.VIDEO_RESOURCE_TYPE);
        songDetailsView.setDownloadUrl(songDownloadUrl);
        String songStreamingUrl = this.cdnUtil
                .getResourceFullUrl(songPartialUrl, CdnUtil.VIDEO_RESOURCE_TYPE);
        songDetailsView.setStreamingUrl(songStreamingUrl);
        return songDetailsView;
    }

    @Override
    public Page<SongView> findAllByCategoryName(String categoryName, Pageable pageable) {
        Page<Song> songPage = this.songRepository.findAllByCategoryName(categoryName, pageable);
        return this.mapperUtil.convertToPage(pageable, songPage, SongView.class);
    }

    @Override
    public Page<SongView> findAllByTagName(String tagName, Pageable pageable) {
        Page<Song> songPage = this.songRepository.findAllByTagName(tagName, pageable);
        return this.mapperUtil.convertToPage(pageable, songPage, SongView.class);
    }

    @Override
    public SongView findById(Long id) throws SongNotFoundException {
        Song song = this.songRepository.findById(id).orElse(null);
        if (song == null) {
            throw new SongNotFoundException();
        }

        return this.mapperUtil.getModelMapper().map(song, SongView.class);
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
    public boolean isSongExists(Long songId) {
        return this.songRepository.existsById(songId);
    }
}
