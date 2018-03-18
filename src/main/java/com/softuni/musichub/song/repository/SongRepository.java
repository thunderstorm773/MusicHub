package com.softuni.musichub.song.repository;

import com.softuni.musichub.song.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends PagingAndSortingRepository<Song, Long>{

    @Query("SELECT s FROM Song AS s WHERE s.title LIKE %:title%")
    Page<Song> findAllByTitle(@Param("title") String title, Pageable pageable);
}
