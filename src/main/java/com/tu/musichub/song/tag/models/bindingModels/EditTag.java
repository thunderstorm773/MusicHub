package com.tu.musichub.song.tag.models.bindingModels;

import com.tu.musichub.song.tag.staticData.TagConstants;
import com.tu.musichub.song.tag.validations.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class EditTag {

    @NotBlank(message = TagConstants.TAG_NAME_BLANK_MESSAGE)
    @Size(min = TagConstants.TAG_NAME_MIN_LEN,
            message = TagConstants.TAG_NAME_MIN_LEN_MESSAGE)
    @Tag
    private String name;
}
