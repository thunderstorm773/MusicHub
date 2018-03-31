package com.softuni.musichub.song.entities;

import com.softuni.musichub.admin.category.entities.Category;
import com.softuni.musichub.song.comment.entities.Comment;
import com.softuni.musichub.song.tag.entities.Tag;
import com.softuni.musichub.user.entities.User;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "songs")
public class Song {

    private Long id;

    private String title;

    private Category category;

    private User uploader;

    private String songPartialUrl;

    private Date uploadedOn;

    private Set<Tag> tags;

    private Set<Comment> comments;

    public Song() {
        this.uploadedOn = new Date();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @ManyToOne
    @JoinColumn(name = "uploader", nullable = false, updatable = false)
    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }

    @Column(name = "song_partial_url", nullable = false)
    public String getSongPartialUrl() {
        return songPartialUrl;
    }

    public void setSongPartialUrl(String songPartialUrl) {
        this.songPartialUrl = songPartialUrl;
    }

    @Column(name = "uploaded_on", nullable = false, updatable = false)
    public Date getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(Date uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    @ManyToMany
    @JoinTable(name = "songs_tags", joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @OneToMany(mappedBy = "song", cascade = CascadeType.REMOVE)
    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
