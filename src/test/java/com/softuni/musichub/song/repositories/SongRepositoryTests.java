package com.softuni.musichub.song.repositories;

import com.softuni.musichub.song.entities.Song;
import com.softuni.musichub.song.tag.entities.Tag;
import com.softuni.musichub.staticData.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles(Constants.TEST_PROFILE)
public class SongRepositoryTests {

    private static final String TEST_SONG_TITLE = "li";
    private static final String TEST_SONG_TAG = "Dj";
    private static final int EXPECTED_SONGS_SIZE = 2;

    @Autowired
    private SongRepository songRepository;

    private Pageable testPageable;

    @Test
    public void testFindAllByTitle_withTitleAndPageable_returnsExpectedSongsSize() {
        Page<Song> songPage = this.songRepository
                .findAllByTitle(TEST_SONG_TITLE, this.testPageable);
        List<Song> songs = songPage.getContent();
        int expectedSongsSize = songs.stream()
                .filter(s -> s.getTitle().contains(TEST_SONG_TITLE))
                .collect(Collectors.toList()).size();

        Assert.assertEquals(expectedSongsSize, songs.size());
    }

    @Test
    public void testFindAllByTitle_withTitleAndPageable_returnsExpectedSongs() {
        Page<Song> songPage = this.songRepository
                .findAllByTitle(TEST_SONG_TITLE, this.testPageable);
        List<Song> songs = songPage.getContent();

        for (Song song : songs) {
            boolean isSongContainsTitle = song.getTitle().contains(TEST_SONG_TITLE);
            Assert.assertTrue(isSongContainsTitle);
        }
    }

    @Test
    public void testFindAllByTagName_withTagAndPageable_returnsExpectedSongsSize() {
        Page<Song> songPage = this.songRepository
                .findAllByTagName(TEST_SONG_TAG, this.testPageable);
        List<Song> songs = songPage.getContent();

        Assert.assertEquals(EXPECTED_SONGS_SIZE, songs.size());
    }

    @Test
    public void testFindAllByTagName_withTagAndPageable_returnsExpectedSongs() {
        Page<Song> songPage = this.songRepository
                .findAllByTagName(TEST_SONG_TAG, this.testPageable);
        List<Song> songs = songPage.getContent();

        for (Song song : songs) {
            Set<Tag> tags = song.getTags();
            boolean isSongHasTag = tags.stream().anyMatch(t -> t.getName().equals(TEST_SONG_TAG));
            Assert.assertTrue(isSongHasTag);
        }
    }
}
