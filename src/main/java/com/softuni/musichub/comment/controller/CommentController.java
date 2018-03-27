package com.softuni.musichub.comment.controller;

import com.google.gson.Gson;
import com.softuni.musichub.comment.models.bindingModels.PostComment;
import com.softuni.musichub.comment.models.viewModels.CommentView;
import com.softuni.musichub.comment.service.api.CommentService;
import com.softuni.musichub.comment.staticData.CommentConstants;
import com.softuni.musichub.song.service.api.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.security.Principal;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    private final SongService songService;

    private final Gson gson;

    @Autowired
    public CommentController(CommentService commentService,
                             SongService songService,
                             Gson gson) {
        this.commentService = commentService;
        this.songService = songService;
        this.gson = gson;
    }

    @PostMapping("/post")
    @ResponseBody
    public String postComment(String commentContent, Long songId,
                              Principal principal) {
        if (commentContent == null
                || commentContent.length() < CommentConstants.COMMENT_MIN_LEN) {
            return null;
        }

        boolean isSongExists = this.songService.isSongExists(songId);
        if (!isSongExists) {
            return null;
        }

        PostComment postComment = new PostComment(commentContent, songId);
        CommentView commentView = this.commentService.postComment(postComment, principal);
        return this.gson.toJson(commentView);
    }
}
