package com.tu.musichub.song.tag.models.bindingModels;

import com.tu.musichub.song.tag.staticData.TagConstants;
import com.tu.musichub.song.tag.validations.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddTag {

    @NotBlank(message = TagConstants.TAG_NAME_BLANK_MESSAGE)
    @Size(min = TagConstants.TAG_NAME_MIN_LEN,
            message = TagConstants.TAG_NAME_MIN_LEN_MESSAGE)
    @Tag
    private String name;
}
