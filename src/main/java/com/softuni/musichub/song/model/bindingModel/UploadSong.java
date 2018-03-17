package com.softuni.musichub.song.model.bindingModel;

import com.softuni.musichub.song.staticData.SongConstants;
import com.softuni.musichub.song.validation.Song;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;

public class UploadSong {

    private MultipartFile file;

    private File persistedFile;

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

    // Get content of multipart file from here,
    // because after request is finished, the content
    // of multipart file will be destroyed
    public File getPersistedFile() {
        return persistedFile;
    }

    public void setPersistedFile(File persistedFile) {
        this.persistedFile = persistedFile;
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
