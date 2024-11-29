package com.tu.musichub.song.comment.entities;

import com.tu.musichub.song.comment.enums.CommentStatus;
import com.tu.musichub.song.entities.Song;
import com.tu.musichub.user.entities.User;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {

    private Long id;

    private String content;

    private User author;

    private CommentStatus status;

    private Song song;

    private Date publishedOn;

    public Comment() {
    }

    public Comment(String content, User author, CommentStatus status, Song song) {
        this.content = content;
        this.author = author;
        this.status = status;
        this.song = song;
        this.publishedOn = new Date();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false, updatable = false)
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public CommentStatus getStatus() {
        return status;
    }

    public void setStatus(CommentStatus status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false, updatable = false)
    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    @Column(name = "published_on", nullable = false, updatable = false)
    public Date getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(Date publishedOn) {
        this.publishedOn = publishedOn;
    }
}
