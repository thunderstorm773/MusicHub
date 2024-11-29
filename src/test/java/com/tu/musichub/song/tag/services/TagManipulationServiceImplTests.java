package com.tu.musichub.song.tag.services;

import com.tu.musichub.song.tag.entities.Tag;
import com.tu.musichub.song.tag.models.bindingModels.AddTag;
import com.tu.musichub.song.tag.models.bindingModels.EditTag;
import com.tu.musichub.song.tag.models.viewModels.TagView;
import com.tu.musichub.song.tag.repositories.TagRepository;
import com.tu.musichub.staticData.TestConstants;
import com.tu.musichub.util.MapperUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles(TestConstants.TEST_PROFILE)
@SpringBootTest
public class TagManipulationServiceImplTests {

    private static final String EXPECTED_TAG_NAME = "tagName";
    private static final String EXPECTED_NEW_TAG_NAME = "newTagName";
    private static final Long EXPECTED_TAG_ID = 1L;
    private static final Long TEST_TAG_ID = 5L;
    private static final Long NON_EXISTENCE_TAG_ID = 10L;
    private static final Long EXISTENCE_TAG_ID = EXPECTED_TAG_ID;

    private AddTag testAddTag;
    private EditTag testEditTag;
    private Tag testTag;

    @MockBean
    private TagRepository tagRepository;

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private TagManipulationService tagManipulationService;

    @Captor
    private ArgumentCaptor<Tag> tagArgumentCaptor;

    private void fillAddTag() {
        this.testAddTag = new AddTag();
        this.testAddTag.setName(EXPECTED_TAG_NAME);
    }

    private void fillTag() {
        this.testTag = new Tag();
        this.testTag.setId(EXPECTED_TAG_ID);
        this.testTag.setName(EXPECTED_TAG_NAME);
    }

    private void fillEditTag() {
        this.testEditTag = new EditTag();
        this.testEditTag.setName(EXPECTED_NEW_TAG_NAME);
    }

    @Before
    public void setUp() {
        this.fillAddTag();
        this.fillTag();
        this.fillEditTag();
        
        Mockito.when(this.tagRepository.save(this.tagArgumentCaptor.capture()))
                .thenReturn(this.testTag);

        Mockito.when(this.tagRepository.existsById(NON_EXISTENCE_TAG_ID))
                .thenReturn(false);

        Mockito.when(this.tagRepository.existsById(EXISTENCE_TAG_ID))
                .thenReturn(true);
    }

    @Test
    public void testSave_withAddTag_shouldInvokesTagRepositorySave() {
        this.tagManipulationService.save(this.testAddTag);
        Mockito.verify(this.tagRepository, Mockito.times(1))
                .save(this.tagArgumentCaptor.capture());
    }

    @Test
    public void testSave_withAddTag_returnsCorrectlyMappedTagView() {
        TagView tagView = this.tagManipulationService.save(this.testAddTag);
        Assert.assertEquals(EXPECTED_TAG_ID, tagView.getId());
        Assert.assertEquals(EXPECTED_TAG_NAME, tagView.getName());
    }

    @Test
    public void testEdit_withTagId_shouldInvokesTagRepositoryExistsById() {
        this.tagManipulationService.edit(this.testEditTag, TEST_TAG_ID);
        Mockito.verify(this.tagRepository, Mockito.times(1))
                .existsById(TEST_TAG_ID);
    }

    @Test
    public void testEdit_withNonExistenceTagId_shouldNotInvokesTagRepositorySave() {
        this.tagManipulationService.edit(this.testEditTag, NON_EXISTENCE_TAG_ID);
        Mockito.verify(this.tagRepository, Mockito.times(0))
                .save(this.tagArgumentCaptor.capture());
    }

    @Test
    public void testEdit_withExistenceTagId_shouldInvokesTagRepositorySave() {
        this.tagManipulationService.edit(this.testEditTag, EXISTENCE_TAG_ID);
        Mockito.verify(this.tagRepository, Mockito.times(1))
                .save(this.tagArgumentCaptor.capture());

        Tag tagWithNewData = this.tagArgumentCaptor.getValue();
        Assert.assertEquals(EXPECTED_TAG_ID, tagWithNewData.getId());
        Assert.assertEquals(EXPECTED_NEW_TAG_NAME, tagWithNewData.getName());
    }
}