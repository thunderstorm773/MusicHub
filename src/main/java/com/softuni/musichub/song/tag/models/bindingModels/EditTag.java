package com.softuni.musichub.song.tag.models.bindingModels;

import com.softuni.musichub.song.tag.staticData.TagConstants;
import com.softuni.musichub.song.tag.validations.Tag;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EditTag {

    private String name;

    public EditTag() {
    }

    @NotBlank(message = TagConstants.TAG_NAME_BLANK_MESSAGE)
    @Size(min = TagConstants.TAG_NAME_MIN_LEN,
            message = TagConstants.TAG_NAME_MIN_LEN_MESSAGE)
    @Tag
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
