package com.softuni.musichub.user.models.viewModels;

import com.softuni.musichub.song.models.viewModels.SongView;
import java.util.List;

public class ProfileView {

    private String username;

    private List<SongView> songs;

    public ProfileView() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<SongView> getSongs() {
        return songs;
    }

    public void setSongs(List<SongView> songs) {
        this.songs = songs;
    }
}
