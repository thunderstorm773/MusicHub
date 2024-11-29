package com.tu.musichub.admin.category.entities;

import com.tu.musichub.song.entities.Song;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {

    private Long id;

    private String name;

    private Set<Song> songs;

    public Category() {
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

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }
}
