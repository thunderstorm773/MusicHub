package com.tu.musichub.song.entities;

import com.tu.musichub.admin.category.entities.Category;
import com.tu.musichub.song.comment.entities.Comment;
import com.tu.musichub.song.tag.entities.Tag;
import com.tu.musichub.user.entities.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "songs")
@Getter
@Setter
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "uploader", nullable = false, updatable = false)
    private User uploader;

    @Column(name = "song_partial_url", nullable = false)
    private String songPartialUrl;

    @Column(name = "uploaded_on", nullable = false, updatable = false)
    private Date uploadedOn;

    @ManyToMany
    @JoinTable(name = "songs_tags", joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    @OneToMany(mappedBy = "song", cascade = CascadeType.REMOVE)
    private Set<Comment> comments;

    public Song() {
        this.uploadedOn = new Date();
    }
}
