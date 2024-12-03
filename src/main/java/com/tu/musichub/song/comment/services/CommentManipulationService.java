package com.tu.musichub.song.comment.services;

import com.tu.musichub.song.comment.models.bindingModels.PostComment;
import com.tu.musichub.song.comment.models.viewModels.CommentView;
import org.springframework.security.core.Authentication;

import java.security.Principal;

public interface CommentManipulationService {

    CommentView postComment(PostComment postComment, Authentication authentication);

    void approve(Long id);

    void reject(Long id);

    void deleteAllRejectedComments();
}
