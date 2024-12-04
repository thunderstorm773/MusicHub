package com.tu.musichub.song.tag.services;

import com.tu.musichub.song.tag.entities.Tag;
import com.tu.musichub.song.tag.models.bindingModels.EditTag;
import com.tu.musichub.song.tag.models.viewModels.TagView;
import com.tu.musichub.song.tag.repositories.TagRepository;
import com.tu.musichub.util.MapperUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TagExtractionServiceImplTests {

    private static final Long EXPECTED_TAG_ID = 1L;
    private static final Long EXISTENCE_TAG_ID = EXPECTED_TAG_ID;
    private static final String EXPECTED_TAG_NAME = "tagName";
    private static final String TEST_TAG_NAME = "uhhu";
    private static final String NON_EXISTENCE_TAG_NAME = "nonExistence";
    private static final String EXISTENCE_TAG_NAME = EXPECTED_TAG_NAME;
    private static final Long TEST_TAG_ID = 100L;
    private static final Long NON_EXISTENCE_TAG_ID = 20L;

    private Tag testTag;

    @MockBean
    private TagRepository tagRepository;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private TagExtractionService tagExtractionService;

    @Mock
    private Pageable pageable;

    private void fillTestTag() {
        this.testTag = new Tag();
        this.testTag.setId(EXPECTED_TAG_ID);
        this.testTag.setName(EXPECTED_TAG_NAME);
    }

    @Before
    public void setUp() {
        this.fillTestTag();

        Mockito.when(this.tagRepository.findByName(NON_EXISTENCE_TAG_NAME))
                .thenReturn(null);

        Mockito.when(this.tagRepository.findByName(EXISTENCE_TAG_NAME))
                .thenReturn(this.testTag);

        Mockito.when(this.tagRepository.findAll(this.pageable))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        Mockito.when(this.tagRepository.findById(NON_EXISTENCE_TAG_ID))
                .thenReturn(Optional.empty());

        Mockito.when(this.tagRepository.findById(EXISTENCE_TAG_ID))
                .thenReturn(Optional.of(this.testTag));
    }

    @Test
    public void testFindByName_withTagId_shouldInvokesTagRepositoryFindByName() {
        this.tagExtractionService.findByName(TEST_TAG_NAME);
        Mockito.verify(this.tagRepository, Mockito.times(1))
                .findByName(TEST_TAG_NAME);
    }

    @Test
    public void testFindByName_withNonExistenceTagId_returnsNull() {
        TagView tag = this.tagExtractionService.findByName(NON_EXISTENCE_TAG_NAME);
        Assert.assertNull(tag);
    }

    @Test
    public void testFindByName_withExistenceTagId_returnsCorrectlyMappedTagView() {
        TagView tag = this.tagExtractionService.findByName(EXISTENCE_TAG_NAME);
        Assert.assertEquals(EXPECTED_TAG_ID, tag.getId());
        Assert.assertEquals(EXPECTED_TAG_NAME, tag.getName());
    }

    @Test
    public void testFindAll_withPageable_shouldInvokesTagRepositoryFindAllWithPageable() {
        this.tagExtractionService.findAll(this.pageable);
        Mockito.verify(this.tagRepository, Mockito.times(1))
                .findAll(this.pageable);
    }

    @Test
    public void testFindById_withTagId_shouldInvokesTagRepositoryFindById() {
        this.tagExtractionService.findById(TEST_TAG_ID);
        Mockito.verify(this.tagRepository, Mockito.times(1))
                .findById(TEST_TAG_ID);
    }

    @Test
    public void testFindById_withNonExistenceTagId_returnsNull() {
        EditTag tagView = this.tagExtractionService.findById(NON_EXISTENCE_TAG_ID);
        Assert.assertNull(tagView);
    }

    @Test
    public void testFindById_withExistenceTagId_returnsCorrectlyMappedEditTag() {
        EditTag editTag = this.tagExtractionService.findById(EXISTENCE_TAG_ID);
        Assert.assertEquals(EXPECTED_TAG_NAME, editTag.getName());
    }
}