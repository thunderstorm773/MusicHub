package com.tu.musichub.song.comment.controllers;

import com.google.gson.Gson;
import com.tu.musichub.controller.BaseController;
import com.tu.musichub.song.comment.models.bindingModels.PostComment;
import com.tu.musichub.song.comment.models.viewModels.CommentView;
import com.tu.musichub.song.comment.services.CommentExtractionService;
import com.tu.musichub.song.comment.services.CommentManipulationService;
import com.tu.musichub.song.comment.staticData.CommentConstants;
import com.tu.musichub.song.services.SongExtractionService;
import com.tu.musichub.staticData.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/comments")
public class CommentController extends BaseController {

    private final CommentExtractionService commentExtractionService;

    private final CommentManipulationService commentManipulationService;

    private final SongExtractionService songExtractionService;

    private final Gson gson;

    @Autowired
    public CommentController(CommentExtractionService commentExtractionService,
                             CommentManipulationService commentManipulationService,
                             SongExtractionService songExtractionService,
                             Gson gson) {
        this.commentExtractionService = commentExtractionService;
        this.commentManipulationService = commentManipulationService;
        this.songExtractionService = songExtractionService;
        this.gson = gson;
    }

    private boolean isCommentValid(String commentContent, Long songId) {
        if (commentContent == null
                || commentContent.length() < CommentConstants.COMMENT_MIN_LEN) {
            return false;
        }

        return this.songExtractionService.isSongExists(songId);
    }

    @PostMapping("/post")
    @ResponseBody
    public String postComment(String commentContent, Long songId,
                              Authentication authentication) {
        if (!isCommentValid(commentContent, songId)) {
            return null;
        }

        PostComment postComment = new PostComment(commentContent, songId);
        CommentView commentView = this.commentManipulationService.postComment(postComment, authentication);
        return this.gson.toJson(commentView);
    }

    @GetMapping("/pending")
    public ModelAndView getPendingCommentsPage(@PageableDefault(CommentConstants.COMMENTS_PER_PAGE) Pageable pageable) {
        Page<CommentView> commentViewPage = this.commentExtractionService
                .findPendingComments(pageable);
        Map<String, Object> objectByKey = new HashMap<>();
        objectByKey.put(Constants.PAGE, commentViewPage);
        return this.view(CommentConstants.PENDING_COMMENTS_TITLE,
                CommentConstants.PENDING_COMMENTS_VIEW, objectByKey);
    }

    @PostMapping("/approve/{id}")
    public ModelAndView approveComment(@PathVariable Long id) {
        this.commentManipulationService.approve(id);
        return this.redirect(CommentConstants.PENDING_COMMENTS_ROUTE);
    }

    @PostMapping("/reject/{id}")
    public ModelAndView rejectComment(@PathVariable Long id) {
        this.commentManipulationService.reject(id);
       return this.redirect(CommentConstants.PENDING_COMMENTS_ROUTE);
    }
}
