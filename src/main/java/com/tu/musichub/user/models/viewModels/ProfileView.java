package com.tu.musichub.user.models.viewModels;

import com.tu.musichub.song.models.viewModels.SongView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ProfileView {

    private String username;

    private List<SongView> songs;
}
