package com.softuni.musichub.song.comment.controllers;

import com.google.gson.Gson;
import com.softuni.musichub.song.comment.models.bindingModels.PostComment;
import com.softuni.musichub.song.comment.models.viewModels.CommentView;
import com.softuni.musichub.song.comment.services.CommentService;
import com.softuni.musichub.song.comment.staticData.CommentConstants;
import com.softuni.musichub.song.services.SongExtractionService;
import com.softuni.musichub.staticData.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    private final SongExtractionService songService;

    private final Gson gson;

    @Autowired
    public CommentController(CommentService commentService,
                             SongExtractionService songService,
                             Gson gson) {
        this.commentService = commentService;
        this.songService = songService;
        this.gson = gson;
    }

    private boolean isCommentValid(String commentContent, Long songId) {
        if (commentContent == null
                || commentContent.length() < CommentConstants.COMMENT_MIN_LEN) {
            return false;
        }

        boolean isSongExists = this.songService.isSongExists(songId);
        if (!isSongExists) {
            return false;
        }

        return true;
    }

    @PostMapping("/post")
    @ResponseBody
    public String postComment(String commentContent, Long songId,
                              Principal principal) {
        if (!isCommentValid(commentContent, songId)) {
            return null;
        }

        PostComment postComment = new PostComment(commentContent, songId);
        CommentView commentView = this.commentService.postComment(postComment, principal);
        return this.gson.toJson(commentView);
    }

    @GetMapping("/pending")
    public ModelAndView getPendingCommentsPage(ModelAndView modelAndView,
                                               @PageableDefault(CommentConstants.COMMENTS_PER_PAGE) Pageable pageable) {
        Page<CommentView> commentViewPage = this.commentService.findPendingComments(pageable);
        modelAndView.addObject(Constants.PAGE, commentViewPage);
        modelAndView.addObject(Constants.TITLE, CommentConstants.PENDING_COMMENTS_TITLE);
        modelAndView.addObject(Constants.VIEW, CommentConstants.PENDING_COMMENTS_VIEW);
        modelAndView.setViewName(Constants.BASE_LAYOUT_VIEW);
        return modelAndView;
    }

    @PostMapping("/approve/{id}")
    public ModelAndView approveComment(ModelAndView modelAndView,
                                       @PathVariable Long id) {
        this.commentService.approve(id);
        modelAndView.setViewName("redirect:/comments/pending");
        return modelAndView;
    }

    @PostMapping("/reject/{id}")
    public ModelAndView rejectComment(ModelAndView modelAndView,
                                      @PathVariable Long id) {
        this.commentService.reject(id);
        modelAndView.setViewName("redirect:/comments/pending");
        return modelAndView;
    }
}
