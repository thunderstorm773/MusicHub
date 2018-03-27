package com.softuni.musichub.comment.service.api;

import com.softuni.musichub.comment.models.bindingModels.PostComment;
import com.softuni.musichub.comment.models.viewModels.CommentView;
import java.security.Principal;

public interface CommentService {

    CommentView postComment(PostComment postComment, Principal principal);
}
