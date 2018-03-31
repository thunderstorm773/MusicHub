package com.softuni.musichub.song.comment.repositories;

import com.softuni.musichub.song.comment.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment AS c WHERE c.status = 'PENDING' ORDER BY c.publishedOn DESC")
    Page<Comment> findPendingComments(Pageable pageable);

    @Modifying
    @Query("DELETE FROM Comment AS c WHERE c.status = 'REJECTED'")
    void deleteAllRejectedComments();
}
