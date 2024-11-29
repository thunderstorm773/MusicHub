package com.tu.musichub.song.services;

import com.tu.musichub.song.exceptions.SongNotFoundException;
import com.tu.musichub.song.models.bindingModels.EditSong;
import com.tu.musichub.song.models.bindingModels.UploadSong;
import com.tu.musichub.user.entities.User;
import java.io.IOException;

public interface SongManipulationService {

    void upload(UploadSong uploadSong, User user) throws IOException;

    void deleteById(Long songId) throws Exception;

    void edit(EditSong editSong, Long songId) throws SongNotFoundException;
}
