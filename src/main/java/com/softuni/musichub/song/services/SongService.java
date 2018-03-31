package com.softuni.musichub.song.services;

import com.softuni.musichub.song.exceptions.SongNotFoundException;
import com.softuni.musichub.song.models.bindingModels.EditSong;
import com.softuni.musichub.song.models.bindingModels.UploadSong;
import com.softuni.musichub.song.models.viewModels.SongDetailsView;
import com.softuni.musichub.song.models.viewModels.SongView;
import com.softuni.musichub.user.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.io.IOException;

public interface SongService {

    void upload(UploadSong uploadSong, User user) throws IOException;

    Page<SongView> findAll(Pageable pageable);

    Page<SongView> findAllByTitle(String songTitle, Pageable pageable);

    SongDetailsView getDetailsById(Long songId) throws Exception;

    Page<SongView> findAllByCategoryName(String categoryName, Pageable pageable);

    Page<SongView> findAllByTagName(String tagName, Pageable pageable);

    SongView findById(Long songId) throws SongNotFoundException;

    void deleteById(Long songId) throws Exception;

    EditSong getEditSongById(Long songId) throws SongNotFoundException;

    void edit(EditSong editSong, Long songId) throws SongNotFoundException;

    boolean isSongExists(Long songId);
}
