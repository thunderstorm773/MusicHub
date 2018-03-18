package com.softuni.musichub.song.service.api;

import com.softuni.musichub.song.model.bindingModel.UploadSong;
import com.softuni.musichub.song.model.viewModel.SongView;
import com.softuni.musichub.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.io.IOException;

public interface SongService {

    void upload(UploadSong uploadSong, User user) throws IOException;

    Page<SongView> findAll(Pageable pageable);

    Page<SongView> findAllByTitle(String songTitle, Pageable pageable);
}
