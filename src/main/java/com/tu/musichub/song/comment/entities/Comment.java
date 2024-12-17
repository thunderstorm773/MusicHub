package com.tu.musichub.song.comment.entities;

import com.tu.musichub.song.comment.enums.CommentStatus;
import com.tu.musichub.song.entities.Song;
import com.tu.musichub.user.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false, updatable = false)
    private User author;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommentStatus status;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false, updatable = false)
    private Song song;

    @Column(name = "published_on", nullable = false, updatable = false)
    private Date publishedOn;

    public Comment(String content, User author, CommentStatus status, Song song) {
        this.content = content;
        this.author = author;
        this.status = status;
        this.song = song;
        this.publishedOn = new Date();
    }
}
