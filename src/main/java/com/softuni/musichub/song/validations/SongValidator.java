package com.softuni.musichub.song.validations;

import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class SongValidator implements ConstraintValidator<Song, MultipartFile>{

    private static final long SONG_FILE_MAX_SIZE_IN_MB = 10;

    private static final int BYTES_IN_MB = 1_048_576;

    private final String[] ALLOWED_CONTENT_TYPES = {"audio/mp3", "audio/mpeg"};

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if (multipartFile == null) {
            return false;
        }

        Long fileSizeInMB = multipartFile.getSize() / BYTES_IN_MB;
        if (fileSizeInMB > SONG_FILE_MAX_SIZE_IN_MB) {
            return false;
        }

        String fileContentType = multipartFile.getContentType();
        boolean isFileHasAllowedContentType = Arrays
                .stream(ALLOWED_CONTENT_TYPES).anyMatch(t -> t.equals(fileContentType));
        if (!isFileHasAllowedContentType) {
            return false;
        }

        return true;
    }
}
