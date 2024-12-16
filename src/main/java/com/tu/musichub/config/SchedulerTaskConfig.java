package com.tu.musichub.config;

import com.tu.musichub.song.comment.services.CommentManipulationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerTaskConfig {

    private final CommentManipulationServiceImpl commentService;

    @Autowired
    public SchedulerTaskConfig(CommentManipulationServiceImpl commentService) {
        this.commentService = commentService;
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void deleteAllRejectedCommentsAtInterval() {
        this.commentService.deleteAllRejectedComments();
    }
}
