package com.tu.musichub.song.models.viewModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class SongView {

    private Long id;

    private String title;

    private String categoryName;

    private String uploaderUsername;

    private Date uploadedOn;
}
