package com.softuni.musichub.song.staticData;

import com.softuni.musichub.staticData.Constants;

public class SongTestData {

    public static final String SONGS_UPLOAD_URL = "/songs/upload";

    public static final Integer EXPECTED_UPLOAD_SONG_PAGE_MODEL_SIZE = 5;

    public static final Integer EXPECTED_SONG_DETAILS_PAGE_MODEL_SIZE = 5;

    public static final Integer EXPECTED_DELETE_SONG_PAGE_MODEL_SIZE = 3;

    public static final Long NON_EXISTENCE_SONG_ID = 100L;

    public static final Long EXISTENCE_SONG_ID = 5L;

    public static final Long TEST_SONG_ID = 1L;

    public static final String SONGS_DETAILS_WITH_TEST_SONG_ID_URL = "/songs/details/"
            + TEST_SONG_ID;

    public static final String SONGS_DETAILS_WITH_NON_EXISTENCE_SONG_ID_URL = "/songs/details/"
            + NON_EXISTENCE_SONG_ID;

    public static final String SONGS_DETAILS_WITH_EXISTENCE_SONG_ID_URL = "/songs/details/"
            + EXISTENCE_SONG_ID;

    public static final String DELETE_SONG_PAGE_WITH_TEST_SONG_ID_URL = "/songs/delete/"
            + TEST_SONG_ID;

    public static final String DELETE_SONG_PAGE_WITH_NON_EXISTENCE_SONG_ID_URL = "/songs/delete/"
            + NON_EXISTENCE_SONG_ID;

    public static final String DELETE_SONG_PAGE_WITH_EXISTENCE_SONG_ID_URL = "/songs/delete/"
            + EXISTENCE_SONG_ID;

    public static final String EDIT_SONG_PAGE_WITH_EXISTENCE_SONG_ID_URL = "/songs/edit/"
            + EXISTENCE_SONG_ID;

    public static final String VALID_TITLE = "Dance";

    public static final String INVALID_MIN_SIZE_TITLE = "Da";

    public static final String EMPTY_TITLE = "";

    public static final Long VALID_CATEGORY_ID = 2L;

    public static final String TITLE_KEY = "title";

    public static final String CATEGORY_ID_KEY = "categoryId";

    public static final String EDIT_SONG_BINDING_RESULT_KEY = Constants.BINDING_RESULT_PACKAGE
            + SongConstants.EDIT_SONG;
}
