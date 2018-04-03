package com.softuni.musichub.song.comment.services;

import com.softuni.musichub.song.comment.models.bindingModels.PostComment;
import com.softuni.musichub.song.comment.models.viewModels.CommentView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.security.Principal;

public interface CommentManipulationService {

    CommentView postComment(PostComment postComment, Principal principal);

    void approve(Long id);

    void reject(Long id);

    void deleteAllRejectedComments();
}
