package com.softuni.musichub.comment.service.api;

import com.softuni.musichub.comment.models.bindingModels.PostComment;
import com.softuni.musichub.comment.models.viewModels.CommentView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.security.Principal;

public interface CommentService {

    CommentView postComment(PostComment postComment, Principal principal);

    Page<CommentView> findPendingComments(Pageable pageable);

    void approve(Long id);

    void reject(Long id);
}
