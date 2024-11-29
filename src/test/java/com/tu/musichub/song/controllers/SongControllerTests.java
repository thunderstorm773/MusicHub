package com.tu.musichub.song.controllers;

import com.tu.musichub.admin.category.models.views.CategoryView;
import com.tu.musichub.admin.category.services.CategoryExtractionService;
import com.tu.musichub.admin.category.staticData.CategoryConstants;
import com.tu.musichub.config.InterceptorConfig;
import com.tu.musichub.home.staticData.HomeConstants;
import com.tu.musichub.song.comment.staticData.CommentConstants;
import com.tu.musichub.song.exceptions.SongNotFoundException;
import com.tu.musichub.song.models.bindingModels.EditSong;
import com.tu.musichub.song.models.bindingModels.UploadSong;
import com.tu.musichub.song.models.viewModels.SongDetailsView;
import com.tu.musichub.song.models.viewModels.SongView;
import com.tu.musichub.song.services.SongExtractionService;
import com.tu.musichub.song.services.SongManipulationService;
import com.tu.musichub.song.staticData.SongConstants;
import com.tu.musichub.song.staticData.SongTestData;
import com.tu.musichub.staticData.Constants;
import com.tu.musichub.staticData.TestConstants;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.isA;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = SongController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = InterceptorConfig.class)})
@ActiveProfiles(TestConstants.TEST_PROFILE)
@WithMockUser(username = TestConstants.USER_USERNAME, password = TestConstants.USER_PASSWORD)
public class SongControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryExtractionService categoryExtractionService;

    @MockBean
    private SongManipulationService songManipulationService;

    @MockBean
    private SongExtractionService songExtractionService;

    @Captor
    private ArgumentCaptor<EditSong> editSongArgumentCaptor;

    @Before
    public void setUp() {
        Mockito.when(this.categoryExtractionService.findAll())
                .thenReturn(new ArrayList<>());

        Mockito.when(this.songExtractionService.getDetailsById(SongTestData.NON_EXISTENCE_SONG_ID))
                .thenThrow(SongNotFoundException.class);

        Mockito.when(this.songExtractionService.getDetailsById(SongTestData.TEST_SONG_ID))
                .thenReturn(new SongDetailsView());

        Mockito.when(this.songExtractionService.getDetailsById(SongTestData.EXISTENCE_SONG_ID))
                .thenReturn(new SongDetailsView());

        Mockito.when(this.songExtractionService.findById(SongTestData.TEST_SONG_ID))
                .thenReturn(new SongView());

        Mockito.when(this.songExtractionService.findById(SongTestData.NON_EXISTENCE_SONG_ID))
                .thenThrow(SongNotFoundException.class);

        Mockito.when(this.songExtractionService.findById(SongTestData.EXISTENCE_SONG_ID))
                .thenReturn(new SongView());
    }

    @Test
    public void testGetUploadSongPage_shouldInvokesCategoryExtractionServiceFindAll() throws Exception {
        this.mockMvc.perform(get(SongTestData.SONGS_UPLOAD_URL)).andReturn();
        Mockito.verify(this.categoryExtractionService, Mockito.times(1))
                .findAll();
    }

    @Test
    public void testGetUploadSongPage_shouldModelHasCorrectSize() throws Exception {
        this.mockMvc.perform(get(SongTestData.SONGS_UPLOAD_URL))
                .andExpect(model().size(SongTestData.EXPECTED_UPLOAD_SONG_PAGE_MODEL_SIZE));
    }

    @Test
    public void testGetUploadSongPage_shouldReturnsCorrectView() throws Exception {
        this.mockMvc.perform(get(SongTestData.SONGS_UPLOAD_URL))
                .andExpect(status().isOk())
                .andExpect(model().attribute(Constants.TITLE, SongConstants.UPLOAD_SONG_TITLE))
                .andExpect(model().attribute(Constants.VIEW, SongConstants.UPLOAD_SONG_VIEW))
                .andExpect(view().name(Constants.BASE_LAYOUT_VIEW));
    }

    @Test
    public void testGetUploadSongPage_shouldModelHasCorrectAttributes() throws Exception {
        this.mockMvc.perform(get(SongTestData.SONGS_UPLOAD_URL))
                .andExpect(model().attribute(SongConstants.UPLOAD_SONG, isA(UploadSong.class)))
                .andExpect(model().attribute(SongConstants.VALIDATE_UPLOAD_SONG_JS_ENABLED, ""))
                .andExpect(model().attribute(CategoryConstants.CATEGORIES, emptyCollectionOf(CategoryView.class)));
    }

    @Test
    public void testGetSongDetailsPage_withSongId_shouldInvokesSongExtractionServiceGetDetailsById() throws Exception {
        this.mockMvc.perform(get(SongTestData.SONGS_DETAILS_WITH_TEST_SONG_ID_URL))
                .andReturn();
        Mockito.verify(this.songExtractionService, Mockito.times(1))
                .getDetailsById(SongTestData.TEST_SONG_ID);
    }

    @Test
    public void testGetSongDetailsPage_withNonExistenceSongId_shouldRedirectToIndex() throws Exception {
        this.mockMvc.perform(get(SongTestData.SONGS_DETAILS_WITH_NON_EXISTENCE_SONG_ID_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(HomeConstants.INDEX_ROUTE));
    }

    @Test
    public void testGetSongDetailsPage_withExistenceSongId_shouldReturnsCorrectView() throws Exception {
        this.mockMvc.perform(get(SongTestData.SONGS_DETAILS_WITH_EXISTENCE_SONG_ID_URL))
                .andExpect(status().isOk())
                .andExpect(model().attribute(Constants.TITLE, SongConstants.SONG_DETAILS_TITLE))
                .andExpect(model().attribute(Constants.VIEW, SongConstants.SONG_DETAILS_VIEW))
                .andExpect(view().name(Constants.BASE_LAYOUT_VIEW));
        ;
    }

    @Test
    public void testGetSongDetailsPage_withExistenceSongId_shouldModelHasCorrectSize() throws Exception {
        this.mockMvc.perform(get(SongTestData.SONGS_DETAILS_WITH_EXISTENCE_SONG_ID_URL))
                .andExpect(model().size(SongTestData.EXPECTED_SONG_DETAILS_PAGE_MODEL_SIZE));
    }

    @Test
    public void testGetSongDetailsPage_withExistenceSongId_shouldModelHasCorrectAttributes() throws Exception {
        this.mockMvc.perform(get(SongTestData.SONGS_DETAILS_WITH_EXISTENCE_SONG_ID_URL))
                .andExpect(model().attribute(SongConstants.AUDIO_JS_STYLE_ENABLED, ""))
                .andExpect(model().attribute(CommentConstants.POST_COMMENTS_JS_ENABLED, ""))
                .andExpect(model().attribute(SongConstants.SONG_DETAILS, isA(SongDetailsView.class)));
    }

    @Test
    public void testGetDeleteSongPage_withTestSongId_shouldInvokesSongExtractionServiceFindById() throws Exception {
        this.mockMvc.perform(get(SongTestData.DELETE_SONG_PAGE_WITH_TEST_SONG_ID_URL))
                .andReturn();
        Mockito.verify(this.songExtractionService, Mockito.times(1))
                .findById(SongTestData.TEST_SONG_ID);
    }

    @Test
    public void testGetDeleteSongPage_withNonExistenceSongId_shouldRedirectsToIndex() throws Exception {
        this.mockMvc.perform(get(SongTestData.DELETE_SONG_PAGE_WITH_NON_EXISTENCE_SONG_ID_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(HomeConstants.INDEX_ROUTE));
    }

    @Test
    public void testGetDeleteSongPage_withExistenceSongId_shouldReturnsCorrectView() throws Exception {
        this.mockMvc.perform(get(SongTestData.DELETE_SONG_PAGE_WITH_EXISTENCE_SONG_ID_URL))
                .andExpect(status().isOk())
                .andExpect(model().attribute(Constants.TITLE, SongConstants.DELETE_SONG_TITLE))
                .andExpect(model().attribute(Constants.VIEW, SongConstants.DELETE_SONG_VIEW))
                .andExpect(view().name(Constants.BASE_LAYOUT_VIEW));
    }

    @Test
    public void testGetDeleteSongPage_withExistenceSongId_shouldModelHasCorrectSize() throws Exception {
        this.mockMvc.perform(get(SongTestData.DELETE_SONG_PAGE_WITH_EXISTENCE_SONG_ID_URL))
                .andExpect(model().size(SongTestData.EXPECTED_DELETE_SONG_PAGE_MODEL_SIZE));
    }

    @Test
    public void testGetDeleteSongPage_withExistenceSongId_shouldModelHasCorrectAttributes() throws Exception {
        this.mockMvc.perform(get(SongTestData.DELETE_SONG_PAGE_WITH_EXISTENCE_SONG_ID_URL))
                .andExpect(model().attribute(SongConstants.DELETE_SONG, isA(SongView.class)));
    }

    @Test
    public void testDeleteSong_withTestSongId_shouldInvokesSongManipulationServiceDeleteById() throws Exception {
        this.mockMvc.perform(post(SongTestData.DELETE_SONG_PAGE_WITH_TEST_SONG_ID_URL).with(csrf()))
                .andReturn();
        Mockito.verify(this.songManipulationService, Mockito.times(1))
                .deleteById(SongTestData.TEST_SONG_ID);
    }

    @Test
    public void testDeleteSong_shouldRedirectsToIndex() throws Exception {
        this.mockMvc.perform(post(SongTestData.DELETE_SONG_PAGE_WITH_TEST_SONG_ID_URL).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(HomeConstants.INDEX_ROUTE));
    }

    @Test
    public void testEditSong_withEmptyTitle_shouldRedirectsToSamePage() throws Exception {
        Integer flashAttributesCount = 2;
        this.mockMvc.perform(post(SongTestData.EDIT_SONG_PAGE_WITH_EXISTENCE_SONG_ID_URL)
                .with(csrf())
                .param(SongTestData.TITLE_KEY, SongTestData.EMPTY_TITLE)
                .param(SongTestData.CATEGORY_ID_KEY, SongTestData.VALID_CATEGORY_ID.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists(SongConstants.EDIT_SONG))
                .andExpect(flash().attributeExists(SongTestData.EDIT_SONG_BINDING_RESULT_KEY))
                .andExpect(flash().attributeCount(flashAttributesCount))
                .andExpect(redirectedUrl(SongConstants.EDIT_SONG_BASE_ROUTE + SongTestData.EXISTENCE_SONG_ID));

        Mockito.verifyNoMoreInteractions(this.songManipulationService);
    }

    @Test
    public void testEditSong_withNullTitle_shouldRedirectsToSamePage() throws Exception {
        Integer flashAttributesCount = 2;
        this.mockMvc.perform(post(SongTestData.EDIT_SONG_PAGE_WITH_EXISTENCE_SONG_ID_URL)
                .with(csrf())
                .param(SongTestData.CATEGORY_ID_KEY, SongTestData.VALID_CATEGORY_ID.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists(SongConstants.EDIT_SONG))
                .andExpect(flash().attributeExists(SongTestData.EDIT_SONG_BINDING_RESULT_KEY))
                .andExpect(flash().attributeCount(flashAttributesCount))
                .andExpect(redirectedUrl(SongConstants.EDIT_SONG_BASE_ROUTE + SongTestData.EXISTENCE_SONG_ID));

        Mockito.verifyNoMoreInteractions(this.songManipulationService);
    }

    @Test
    public void testEditSong_withInvalidMinSizeTitle_shouldRedirectsToSamePage() throws Exception {
        Integer flashAttributesCount = 2;
        this.mockMvc.perform(post(SongTestData.EDIT_SONG_PAGE_WITH_EXISTENCE_SONG_ID_URL)
                .with(csrf())
                .param(SongTestData.TITLE_KEY, SongTestData.INVALID_MIN_SIZE_TITLE)
                .param(SongTestData.CATEGORY_ID_KEY, SongTestData.VALID_CATEGORY_ID.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists(SongConstants.EDIT_SONG))
                .andExpect(flash().attributeExists(SongTestData.EDIT_SONG_BINDING_RESULT_KEY))
                .andExpect(flash().attributeCount(flashAttributesCount))
                .andExpect(redirectedUrl(SongConstants.EDIT_SONG_BASE_ROUTE + SongTestData.EXISTENCE_SONG_ID));

        Mockito.verifyNoMoreInteractions(this.songManipulationService);
    }

    @Test
    public void testEditSong_withNullCategoryId_shouldRedirectsToSamePage() throws Exception {
        Integer flashAttributesCount = 2;
        this.mockMvc.perform(post(SongTestData.EDIT_SONG_PAGE_WITH_EXISTENCE_SONG_ID_URL)
                .with(csrf())
                .param(SongTestData.TITLE_KEY, SongTestData.VALID_TITLE))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists(SongConstants.EDIT_SONG))
                .andExpect(flash().attributeExists(SongTestData.EDIT_SONG_BINDING_RESULT_KEY))
                .andExpect(flash().attributeCount(flashAttributesCount))
                .andExpect(redirectedUrl(SongConstants.EDIT_SONG_BASE_ROUTE + SongTestData.EXISTENCE_SONG_ID));

        Mockito.verifyNoMoreInteractions(this.songManipulationService);
    }

    @Test
    public void testEditSong_withValidEditSongModel_shouldInvokesSongManipulationServiceEdit() throws Exception {
        this.mockMvc.perform(post(SongTestData.EDIT_SONG_PAGE_WITH_EXISTENCE_SONG_ID_URL)
                .with(csrf())
                .param(SongTestData.TITLE_KEY, SongTestData.VALID_TITLE)
                .param(SongTestData.CATEGORY_ID_KEY, SongTestData.VALID_CATEGORY_ID.toString()));

        Mockito.verify(this.songManipulationService, Mockito.times(1))
                .edit(this.editSongArgumentCaptor.capture(), Mockito.eq(SongTestData.EXISTENCE_SONG_ID));
        Mockito.verifyNoMoreInteractions(this.songManipulationService);
    }

    @Test
    public void testEditSong_withValidEditSongModel_shouldRedirectToSamePageWithSuccessfullyMessage() throws Exception {
        Integer redirectAttributesCount = 1;

        this.mockMvc.perform(post(SongTestData.EDIT_SONG_PAGE_WITH_EXISTENCE_SONG_ID_URL)
                .with(csrf())
                .param(SongTestData.TITLE_KEY, SongTestData.VALID_TITLE)
                .param(SongTestData.CATEGORY_ID_KEY, SongTestData.VALID_CATEGORY_ID.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(redirectAttributesCount))
                .andExpect(flash().attribute(Constants.INFO, Matchers.equalTo(SongConstants.SONG_EDITED_MESSAGE)))
                .andExpect(redirectedUrl(SongConstants.EDIT_SONG_BASE_ROUTE + SongTestData.EXISTENCE_SONG_ID));
    }
}
