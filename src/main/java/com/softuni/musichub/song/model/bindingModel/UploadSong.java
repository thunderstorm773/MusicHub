package com.softuni.musichub.song.model.bindingModel;

import com.softuni.musichub.song.staticData.SongConstants;
import com.softuni.musichub.song.validation.Song;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UploadSong {

    private MultipartFile file;

    private String title;

    private Long categoryId;

    private String tagsAsString;

    public UploadSong() {
    }

    @NotNull(message = SongConstants.SONG_FILE_EMPTY_MESSAGE)
    @Song
    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @NotBlank(message = SongConstants.TITLE_BLANK_MESSAGE)
    @Size(min = SongConstants.TITLE_MIN_LEN,
            message = SongConstants.TITLE_MIN_MESSAGE)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull(message = SongConstants.CATEGORY_EMPTY_MESSAGE)
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTagsAsString() {
        return tagsAsString;
    }

    public void setTagsAsString(String tagsAsString) {
        this.tagsAsString = tagsAsString;
    }
}
