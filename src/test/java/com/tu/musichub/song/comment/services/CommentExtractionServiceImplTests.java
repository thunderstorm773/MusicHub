package com.tu.musichub.song.comment.services;

import com.tu.musichub.song.comment.repositories.CommentRepository;
import com.tu.musichub.util.MapperUtil;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentExtractionServiceImplTests {

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private CommentExtractionServiceImpl commentExtractionService;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @MockBean
    private JavaMailSender mailSender;

    @Mock
    private Pageable pageable;

    @Before
    public void setUp() {
        Mockito.when(this.commentRepository.findPendingComments(this.pageable))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
    }

    @Test
    public void testFindPendingComments_withPageable_shouldInvokesCommentRepositoryFindPendingComments() {
        this.commentExtractionService.findPendingComments(this.pageable);
        Mockito.verify(this.commentRepository, Mockito.times(1))
                .findPendingComments(this.pageable);
    }
}