package com.tu.musichub.song.tag.validations;

import com.tu.musichub.song.tag.models.viewModels.TagView;
import com.tu.musichub.song.tag.services.TagExtractionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class TagValidator implements ConstraintValidator<Tag, String> {

    private final TagExtractionServiceImpl tagExtractionService;

    @Autowired
    public TagValidator(TagExtractionServiceImpl tagExtractionService) {
        this.tagExtractionService = tagExtractionService;
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        TagView tagView = this.tagExtractionService.findByName(name);
        return tagView == null;
    }
}
