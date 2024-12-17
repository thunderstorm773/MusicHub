package com.tu.musichub.song.models.bindingModels;

import com.tu.musichub.song.staticData.SongConstants;
import com.tu.musichub.song.validations.Song;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;

@NoArgsConstructor
@Getter
@Setter
public class UploadSong {

    @NotNull(message = SongConstants.SONG_FILE_EMPTY_MESSAGE)
    @Song
    private MultipartFile file;

    private File persistedFile;

    @NotBlank(message = SongConstants.TITLE_BLANK_MESSAGE)
    @Size(min = SongConstants.TITLE_MIN_LEN,
            message = SongConstants.TITLE_MIN_MESSAGE)
    private String title;

    @NotNull(message = SongConstants.CATEGORY_EMPTY_MESSAGE)
    private Long categoryId;

    private String tagsAsString;
}
