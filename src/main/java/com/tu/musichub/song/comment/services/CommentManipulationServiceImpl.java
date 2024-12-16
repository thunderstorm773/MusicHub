package com.tu.musichub.song.comment.services;

import com.tu.musichub.song.comment.entities.Comment;
import com.tu.musichub.song.comment.enums.CommentStatus;
import com.tu.musichub.song.comment.models.bindingModels.PostComment;
import com.tu.musichub.song.comment.models.viewModels.CommentView;
import com.tu.musichub.song.comment.repositories.CommentRepository;
import com.tu.musichub.song.comment.staticData.CommentConstants;
import com.tu.musichub.song.entities.Song;
import com.tu.musichub.song.models.viewModels.SongView;
import com.tu.musichub.song.services.SongExtractionServiceImpl;
import com.tu.musichub.song.staticData.SongConstants;
import com.tu.musichub.user.entities.User;
import com.tu.musichub.user.models.viewModels.UserView;
import com.tu.musichub.user.services.UserExtractionService;
import com.tu.musichub.user.utils.UserUtils;
import com.tu.musichub.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class CommentManipulationServiceImpl {

    private final CommentRepository commentRepository;

    private final SongExtractionServiceImpl songExtractionService;

    private final UserExtractionService userExtractionService;

    private final MapperUtil mapperUtil;

    private final UserUtils userUtils;

    @Autowired
    public CommentManipulationServiceImpl(CommentRepository commentRepository,
                                          SongExtractionServiceImpl songExtractionService,
                                          UserExtractionService userExtractionService,
                                          MapperUtil mapperUtil,
                                          UserUtils userUtils) {
        this.commentRepository = commentRepository;
        this.songExtractionService = songExtractionService;
        this.userExtractionService = userExtractionService;
        this.mapperUtil = mapperUtil;
        this.userUtils = userUtils;
    }

    private Comment constructComment(PostComment postComment, Authentication authentication) {
        Long songId = postComment.getSongId();
        SongView songView = this.songExtractionService.findById(songId);
        Song song = this.mapperUtil.getModelMapper().map(songView, Song.class);

        String username = this.userUtils.getUsername(authentication);
        UserView userView = this.userExtractionService.findByUsername(username);

        User user = this.mapperUtil.getModelMapper().map(userView, User.class);
        CommentStatus defaultStatus = CommentStatus.PENDING;
        String commentContent = postComment.getContent();
        return new Comment(commentContent, user, defaultStatus, song);
    }

    @CacheEvict(cacheNames = SongConstants.SONGS_CACHE_NAME,
            key = "#postComment.songId")
    public CommentView postComment(PostComment postComment, Authentication authentication) {
        Comment comment = this.constructComment(postComment, authentication);
        Comment postedComment = this.commentRepository.save(comment);
        return this.mapperUtil.getModelMapper().map(postedComment, CommentView.class);
    }

    @CacheEvict(cacheNames = SongConstants.SONGS_CACHE_NAME, allEntries = true)
    public void approve(Long id) {
        Comment comment = this.commentRepository.findById(id).orElse(null);
        if (comment == null) {
            return;
        }

        comment.setStatus(CommentStatus.APPROVED);
    }

    @CacheEvict(cacheNames = SongConstants.SONGS_CACHE_NAME, allEntries = true)
    public void reject(Long id) {
        Comment comment = this.commentRepository.findById(id).orElse(null);
        if (comment == null) {
            return;
        }

        comment.setStatus(CommentStatus.REJECTED);
    }

    @CacheEvict(cacheNames = SongConstants.SONGS_CACHE_NAME, allEntries = true)
    public void deleteAllRejectedComments() {
        this.commentRepository.deleteAllRejectedComments();
        System.out.println(CommentConstants.REJECTED_COMMENTS_DELETED_MESSAGE);
    }
}
