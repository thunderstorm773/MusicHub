package com.softuni.musichub.song.comment.services;

import com.softuni.musichub.song.comment.entities.Comment;
import com.softuni.musichub.song.comment.enums.CommentStatus;
import com.softuni.musichub.song.comment.models.bindingModels.PostComment;
import com.softuni.musichub.song.comment.models.viewModels.CommentView;
import com.softuni.musichub.song.comment.repositories.CommentRepository;
import com.softuni.musichub.song.entities.Song;
import com.softuni.musichub.song.models.viewModels.SongView;
import com.softuni.musichub.song.services.SongExtractionService;
import com.softuni.musichub.user.entities.User;
import com.softuni.musichub.user.models.viewModels.UserView;
import com.softuni.musichub.user.services.UserExtractionService;
import com.softuni.musichub.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.security.Principal;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final SongExtractionService songService;

    private final UserExtractionService userService;

    private final MapperUtil mapperUtil;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              SongExtractionService songService,
                              UserExtractionService userService, MapperUtil mapperUtil) {
        this.commentRepository = commentRepository;
        this.songService = songService;
        this.userService = userService;
        this.mapperUtil = mapperUtil;
    }

    private Comment constructComment(PostComment postComment, Principal principal) {
        Long songId = postComment.getSongId();
        SongView songView = this.songService.findById(songId);
        Song song = this.mapperUtil.getModelMapper().map(songView, Song.class);
        UserView userView = this.userService.findByUsername(principal.getName());
        User user = this.mapperUtil.getModelMapper().map(userView, User.class);
        CommentStatus defaultStatus = CommentStatus.PENDING;
        String commentContent = postComment.getContent();
        return new Comment(commentContent, user, defaultStatus, song);
    }

    @Override
    public CommentView postComment(PostComment postComment, Principal principal) {
        Comment comment = this.constructComment(postComment, principal);
        Comment postedComment = this.commentRepository.save(comment);
        return this.mapperUtil.getModelMapper()
                .map(postedComment, CommentView.class);
    }

    @Override
    public Page<CommentView> findPendingComments(Pageable pageable) {
        Page<Comment> commentPage = this.commentRepository.findPendingComments(pageable);
        return this.mapperUtil.convertToPage(pageable, commentPage, CommentView.class);
    }

    @Override
    public void approve(Long id) {
        Comment comment = this.commentRepository.findById(id).orElse(null);
        if (comment == null) {
            return;
        }

        comment.setStatus(CommentStatus.APPROVED);
    }

    @Override
    public void reject(Long id) {
        Comment comment = this.commentRepository.findById(id).orElse(null);
        if (comment == null) {
            return;
        }

        comment.setStatus(CommentStatus.REJECTED);
    }

    @Override
    public void deleteAllRejectedComments() {
        this.commentRepository.deleteAllRejectedComments();
        System.out.println("All comments with status REJECTED have been deleted!");
    }
}
