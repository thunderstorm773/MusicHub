package com.softuni.musichub.song.validation;

import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SongValidator implements ConstraintValidator<Song, MultipartFile>{

    private static final long SONG_FILE_MAX_SIZE_IN_MB = 10;

    private static final int BYTES_IN_MB = 1_048_576;

    private final String MP3_CONTENT_TYPE = "audio/mp3";

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
        if (!fileContentType.equalsIgnoreCase(MP3_CONTENT_TYPE)) {
            return false;
        }

        return true;
    }
}
