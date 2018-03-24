package com.softuni.musichub.song.model.bindingModel;

import com.softuni.musichub.song.staticData.SongConstants;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EditSong {

    private String title;

    private Long categoryId;

    private String tagNames;

    public EditSong() {
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

    public String getTagNames() {
        return tagNames;
    }

    public void setTagNames(String tagNames) {
        this.tagNames = tagNames;
    }
}
