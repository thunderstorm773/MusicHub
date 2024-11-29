package com.tu.musichub.song.repositories;

import com.tu.musichub.song.entities.Song;
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

    Page<Song> findAllByCategoryName(String categoryName, Pageable pageable);

    @Query("SELECT s FROM Song AS s INNER JOIN s.tags AS t WHERE t.name = :tagName")
    Page<Song> findAllByTagName(@Param("tagName") String tagName, Pageable pageable);
}
