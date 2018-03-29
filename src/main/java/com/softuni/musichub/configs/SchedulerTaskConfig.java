package com.softuni.musichub.configs;

import com.softuni.musichub.comment.service.api.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerTaskConfig {

    private final CommentService commentService;

    @Autowired
    public SchedulerTaskConfig(CommentService commentService) {
        this.commentService = commentService;
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void deleteAllRejectedCommentsAtInterval() {
        this.commentService.deleteAllRejectedComments();
    }
}
