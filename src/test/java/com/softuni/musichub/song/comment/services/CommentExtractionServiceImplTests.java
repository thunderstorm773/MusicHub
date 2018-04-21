package com.softuni.musichub.song.comment.services;

import com.softuni.musichub.song.comment.repositories.CommentRepository;
import com.softuni.musichub.util.MapperUtil;
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
    private CommentExtractionService commentExtractionService;

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