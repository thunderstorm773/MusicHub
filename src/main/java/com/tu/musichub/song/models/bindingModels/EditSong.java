package com.tu.musichub.song.models.bindingModels;

import com.tu.musichub.song.staticData.SongConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class EditSong {

    @NotBlank(message = SongConstants.TITLE_BLANK_MESSAGE)
    @Size(min = SongConstants.TITLE_MIN_LEN,
            message = SongConstants.TITLE_MIN_MESSAGE)
    private String title;

    @NotNull(message = SongConstants.CATEGORY_EMPTY_MESSAGE)
    private Long categoryId;

    private String tagNames;
}
