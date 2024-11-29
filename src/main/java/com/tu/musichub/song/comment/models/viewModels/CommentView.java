package com.tu.musichub.song.comment.models.viewModels;

import com.tu.musichub.song.comment.enums.CommentStatus;
import java.util.Date;

public class CommentView {

    private Long id;

    private String content;

    private String authorUsername;

    private CommentStatus status;

    private Date publishedOn;

    public CommentView() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public CommentStatus getStatus() {
        return status;
    }

    public void setStatus(CommentStatus status) {
        this.status = status;
    }

    public Date getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(Date publishedOn) {
        this.publishedOn = publishedOn;
    }
}
