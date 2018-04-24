package com.softuni.musichub.song.services;

import com.softuni.musichub.song.exceptions.SongNotFoundException;
import com.softuni.musichub.song.models.bindingModels.EditSong;
import com.softuni.musichub.song.models.viewModels.SongDetailsView;
import com.softuni.musichub.song.models.viewModels.SongView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SongExtractionService {

    Page<SongView> findAll(Pageable pageable);

    Page<SongView> findAllByTitle(String songTitle, Pageable pageable);

    SongDetailsView getDetailsById(Long songId) throws SongNotFoundException;

    Page<SongView> findAllByCategoryName(String categoryName, Pageable pageable);

    Page<SongView> findAllByTagName(String tagName, Pageable pageable);

    SongView findById(Long songId) throws SongNotFoundException;

    EditSong getEditSongById(Long songId) throws SongNotFoundException;

    boolean isSongExists(Long songId);
}
