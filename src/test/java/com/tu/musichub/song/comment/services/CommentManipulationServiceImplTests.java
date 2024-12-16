package com.tu.musichub.song.comment.services;

import com.tu.musichub.song.comment.entities.Comment;
import com.tu.musichub.song.comment.enums.CommentStatus;
import com.tu.musichub.song.comment.models.bindingModels.PostComment;
import com.tu.musichub.song.comment.models.viewModels.CommentView;
import com.tu.musichub.song.comment.repositories.CommentRepository;
import com.tu.musichub.song.models.viewModels.SongView;
import com.tu.musichub.song.services.SongExtractionServiceImpl;
import com.tu.musichub.user.entities.User;
import com.tu.musichub.user.models.viewModels.UserView;
import com.tu.musichub.user.services.UserExtractionService;
import com.tu.musichub.util.MapperUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentManipulationServiceImplTests {

    private static final Long EXPECTED_SONG_ID = 2L;
    private static final String EXPECTED_CONTENT = "Content";
    private static final String EXPECTED_USERNAME = "username";
    private static final String EXPECTED_SONG_CATEGORY_NAME = "Pop";
    private static final String EXPECTED_SONG_TITLE = "Title";
    private static final String EXPECTED_USER_ID = "d480ef58-3258-4b7b-936e-619f97cf605d";
    private static final String EXPECTED_USER_EMAIL = "test@abv.bg";
    private static final Date EXPECTED_SONG_UPLOADED_ON = new Date();
    private static final Date EXPECTED_COMMENT_PUBLISHED_ON = new Date();
    private static final Long EXPECTED_COMMENT_ID = 5L;
    private static final Long TEST_COMMENT_ID = 10L;
    private static final Long EXISTENCE_COMMENT_ID = 20L;

    private PostComment testPostComment;
    private SongView testSongView;
    private UserView testUserView;
    private Comment testComment;
    private User testUser;

    @Mock
    private Authentication authentication;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private SongExtractionServiceImpl songExtractionService;

    @MockBean
    private UserExtractionService userExtractionService;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @MockBean
    private JavaMailSender mailSender;

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private CommentManipulationServiceImpl commentManipulationService;

    @Captor
    private ArgumentCaptor<Comment> commentArgumentCaptor;

    private void fillTestPostComment() {
        this.testPostComment = new PostComment();
        this.testPostComment.setContent(EXPECTED_CONTENT);
        this.testPostComment.setSongId(EXPECTED_SONG_ID);
    }

    private void fillTestSongView() {
        this.testSongView = new SongView();
        this.testSongView.setId(EXPECTED_SONG_ID);
        this.testSongView.setCategoryName(EXPECTED_SONG_CATEGORY_NAME);
        this.testSongView.setTitle(EXPECTED_SONG_TITLE);
        this.testSongView.setUploadedOn(EXPECTED_SONG_UPLOADED_ON);
        this.testSongView.setUploaderUsername(EXPECTED_USERNAME);
    }

    private void fillTestUserView() {
        this.testUserView = new UserView();
        this.testUserView.setId(EXPECTED_USER_ID);
        this.testUserView.setUsername(EXPECTED_USERNAME);
        this.testUserView.setEmail(EXPECTED_USER_EMAIL);
    }

    private void fillTestComment() {
        this.testComment = new Comment();
        this.testComment.setId(EXPECTED_COMMENT_ID);
        User author = new User();
        author.setUsername(EXPECTED_USERNAME);
        this.testComment.setAuthor(author);
        this.testComment.setStatus(CommentStatus.PENDING);
        this.testComment.setContent(EXPECTED_CONTENT);
        this.testComment.setPublishedOn(EXPECTED_COMMENT_PUBLISHED_ON);
    }

    private void fillTestAuthentication() {
        this.testUser = new User();
        this.testUser.setUsername(EXPECTED_USERNAME);
    }

    @Before
    public void setUp() {
        this.fillTestPostComment();
        this.fillTestSongView();
        this.fillTestUserView();
        this.fillTestComment();
        this.fillTestAuthentication();

        Mockito.when(this.authentication.getName())
                .thenReturn(EXPECTED_USERNAME);

        Mockito.when(this.songExtractionService.findById(EXPECTED_SONG_ID))
                .thenReturn(this.testSongView);

        Mockito.when(this.userExtractionService.findByUsername(EXPECTED_USERNAME))
                .thenReturn(this.testUserView);

        Mockito.when(this.commentRepository.save(this.commentArgumentCaptor.capture()))
                .thenReturn(this.testComment);

        Mockito.when(this.commentRepository.findById(EXISTENCE_COMMENT_ID))
                .thenReturn(Optional.of(this.testComment));

        Mockito.when(authentication.getPrincipal())
                .thenReturn(this.testUser);
    }

    @Test
    public void testPostComment_shouldInvokesSongExtractionServiceFindById() {
        this.commentManipulationService.postComment(this.testPostComment, this.authentication);
        Mockito.verify(this.songExtractionService, Mockito.times(1))
                .findById(EXPECTED_SONG_ID);
    }

    @Test
    public void testPostComment_shouldInvokesUserExtractionServiceFindByUsername() {
        this.commentManipulationService.postComment(this.testPostComment, this.authentication);
        Mockito.verify(this.userExtractionService, Mockito.times(1))
                .findByUsername(EXPECTED_USERNAME);

    }

    @Test
    public void testPostComment_shouldInvokesCommentRepositorySave() {
        this.commentManipulationService.postComment(this.testPostComment, this.authentication);
        Mockito.verify(this.commentRepository, Mockito.times(1))
                .save(this.commentArgumentCaptor.capture());
    }

    @Test
    public void testPostComment_returnsCorrectlyMappedCommentView() {
        CommentView commentView = this.commentManipulationService
                .postComment(this.testPostComment, this.authentication);

        Assert.assertEquals(EXPECTED_COMMENT_ID, commentView.getId());
        Assert.assertEquals(EXPECTED_CONTENT, commentView.getContent());
        Assert.assertEquals(EXPECTED_USERNAME, commentView.getAuthorUsername());
        Assert.assertEquals(CommentStatus.PENDING, commentView.getStatus());
        Assert.assertEquals(EXPECTED_COMMENT_PUBLISHED_ON, commentView.getPublishedOn());
    }

    @Test
    public void testApprove_withCommentId_shouldInvokesCommentRepositoryFindById() {
        this.commentManipulationService.approve(TEST_COMMENT_ID);
        Mockito.verify(this.commentRepository, Mockito.times(1))
                .findById(TEST_COMMENT_ID);
    }

    @Test
    public void testReject_withCommentId_shouldInvokesCommentRepositoryFindById() {
        this.commentManipulationService.reject(TEST_COMMENT_ID);
        Mockito.verify(this.commentRepository, Mockito.times(1))
                .findById(TEST_COMMENT_ID);
    }

    @Test
    public void testDeleteAllRejectedComments_shouldInvokesCommentRepositoryDeleteAllRejectedComments() {
        this.commentManipulationService.deleteAllRejectedComments();
        Mockito.verify(this.commentRepository, Mockito.times(1))
                .deleteAllRejectedComments();
    }
}