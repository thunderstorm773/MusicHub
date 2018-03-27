package com.softuni.musichub.comment.service.impl;

import com.softuni.musichub.comment.entity.Comment;
import com.softuni.musichub.comment.enums.CommentStatus;
import com.softuni.musichub.comment.models.bindingModels.PostComment;
import com.softuni.musichub.comment.models.viewModels.CommentView;
import com.softuni.musichub.comment.repository.CommentRepository;
import com.softuni.musichub.comment.service.api.CommentService;
import com.softuni.musichub.song.entity.Song;
import com.softuni.musichub.song.model.viewModel.SongView;
import com.softuni.musichub.song.service.api.SongService;
import com.softuni.musichub.user.entity.User;
import com.softuni.musichub.user.model.viewModel.UserView;
import com.softuni.musichub.user.service.api.UserService;
import com.softuni.musichub.utils.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.security.Principal;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final SongService songService;

    private final UserService userService;

    private final MapperUtil mapperUtil;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              SongService songService,
                              UserService userService, MapperUtil mapperUtil) {
        this.commentRepository = commentRepository;
        this.songService = songService;
        this.userService = userService;
        this.mapperUtil = mapperUtil;
    }

    private Comment constructComment(String commentContent, Principal principal,
                                     SongView songView) {
        Song song = this.mapperUtil.getModelMapper().map(songView, Song.class);
        UserView userView = this.userService.findByUsername(principal.getName());
        User user = this.mapperUtil.getModelMapper().map(userView, User.class);
        CommentStatus defaultStatus = CommentStatus.PENDING;
        return new Comment(commentContent, user, defaultStatus, song);
    }

    @Override
    public CommentView postComment(PostComment postComment, Principal principal) {
        Long songId = postComment.getSongId();
        SongView songView = this.songService.findById(songId);
        String commentContent = postComment.getContent();
        Comment comment = this.constructComment(commentContent, principal, songView);
        Comment postedComment = this.commentRepository.save(comment);
        return this.mapperUtil.getModelMapper()
                .map(postedComment, CommentView.class);
    }
}
