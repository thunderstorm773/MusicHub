package com.tu.musichub.song.comment.models.viewModels;

import com.tu.musichub.song.comment.enums.CommentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class CommentView {

    private Long id;

    private String content;

    private String authorUsername;

    private CommentStatus status;

    private Date publishedOn;
}
