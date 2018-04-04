package com.softuni.musichub.song.tag.validations;

import com.softuni.musichub.song.tag.models.viewModels.TagView;
import com.softuni.musichub.song.tag.services.TagExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class TagValidator implements ConstraintValidator<Tag, String> {

    private final TagExtractionService tagExtractionService;

    @Autowired
    public TagValidator(TagExtractionService tagExtractionService) {
        this.tagExtractionService = tagExtractionService;
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        TagView tagView = this.tagExtractionService.findByName(name);
        return tagView == null;
    }
}
