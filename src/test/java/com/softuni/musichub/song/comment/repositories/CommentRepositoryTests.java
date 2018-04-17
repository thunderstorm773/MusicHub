package com.softuni.musichub.song.comment.repositories;

import com.softuni.musichub.song.comment.entities.Comment;
import com.softuni.musichub.song.comment.enums.CommentStatus;
import com.softuni.musichub.staticData.Constants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@ActiveProfiles(Constants.TEST_PROFILE)
@DataJpaTest
@SpringBootTest
public class CommentRepositoryTests {

    private static final int EXPECTED_PENDING_COMMENTS_COUNT = 2;
    private static final int EXPECTED_REJECTED_COMMENTS_COUNT = 1;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    private List<Comment> testComments;

    private Pageable testPageable;

    private void fillTestComments() {
        this.testComments = (List<Comment>) this.em.getEntityManager()
                .createQuery("SELECT c FROM Comment AS c").getResultList();
    }

    @Before
    public void setUp() {
        this.testComments = new ArrayList<>();
        this.fillTestComments();
    }

    @Test
    public void testFindPendingComments_withPageable_returnsOnlyPendingComments() {
        Page<Comment> commentsPage = this.commentRepository
                .findPendingComments(this.testPageable);
        List<Comment> comments = commentsPage.getContent();
        List<Comment> pendingComments = comments.stream()
                .filter(c -> c.getStatus().equals(CommentStatus.PENDING))
                .collect(Collectors.toList());

        Assert.assertEquals(pendingComments.size(), comments.size());
    }

    @Test
    public void testFindPendingComments_withPageable_returnsExpectedPendingCommentsSize() {
        Page<Comment> commentsPage = this.commentRepository
                .findPendingComments(this.testPageable);
        List<Comment> pendingComments = commentsPage.getContent();

        Assert.assertEquals(EXPECTED_PENDING_COMMENTS_COUNT, pendingComments.size());
    }

    @Test
    public void testFindPendingComments_withPageable_shouldSortDataCorrectly() {
        Page<Comment> commentsPage = this.commentRepository
                .findPendingComments(this.testPageable);
        List<Comment> pendingComments = commentsPage.getContent();
        List<Comment> expectedPendingComments = pendingComments.stream()
                .sorted((c1, c2) -> c2.getPublishedOn().compareTo(c1.getPublishedOn()))
                .collect(Collectors.toList());

        for (int i = 0; i < pendingComments.size(); i++) {
            Comment pendingComment = pendingComments.get(i);
            Comment expectedPendingComment = expectedPendingComments.get(i);
            Assert.assertEquals(expectedPendingComment.getId(), pendingComment.getId());
        }
    }

    @Test
    public void testDeleteAllRejectedComments_returnsExpectedCommentsSize() {
        int expectedCommentsSize = this.testComments.size() - EXPECTED_REJECTED_COMMENTS_COUNT;
        this.commentRepository.deleteAllRejectedComments();
        List<Comment> comments = this.commentRepository.findAll();

        Assert.assertEquals(expectedCommentsSize, comments.size());
    }

    @Test
    public void testDeleteAllRejectedComments_returnsOnlyNonRejectedComments() {
        this.commentRepository.deleteAllRejectedComments();
        List<Comment> comments = this.commentRepository.findAll();
        List<Comment> nonRejectedComments = comments.stream()
                .filter(c -> !c.getStatus().equals(CommentStatus.REJECTED))
                .collect(Collectors.toList());

        Assert.assertEquals(comments.size(), nonRejectedComments.size());
    }
}
