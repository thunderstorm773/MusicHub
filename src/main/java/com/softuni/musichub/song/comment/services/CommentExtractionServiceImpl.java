package com.softuni.musichub.song.comment.services;

import com.softuni.musichub.song.comment.entities.Comment;
import com.softuni.musichub.song.comment.models.viewModels.CommentView;
import com.softuni.musichub.song.comment.repositories.CommentRepository;
import com.softuni.musichub.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class CommentExtractionServiceImpl implements CommentExtractionService{

    private final CommentRepository commentRepository;

    private final MapperUtil mapperUtil;

    @Autowired
    public CommentExtractionServiceImpl(CommentRepository commentRepository,
                                        MapperUtil mapperUtil) {
        this.commentRepository = commentRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public Page<CommentView> findPendingComments(Pageable pageable) {
        Page<Comment> commentPage = this.commentRepository.findPendingComments(pageable);
        return this.mapperUtil.convertToPage(pageable, commentPage, CommentView.class);
    }
}
