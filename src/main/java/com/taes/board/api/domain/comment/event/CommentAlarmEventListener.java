package com.taes.board.api.domain.comment.event;

import com.taes.board.api.common.dto.MailDto;
import com.taes.board.api.common.service.MailService;
import com.taes.board.api.common.service.MailServiceImpl;
import com.taes.board.api.domain.comment.entity.Comment;
import com.taes.board.api.domain.comment.service.CommentService;
import com.taes.board.api.domain.post.entity.Post;
import com.taes.board.api.domain.post.service.PostService;
import com.taes.board.api.domain.post.service.PostServiceImpl;
import com.taes.board.api.domain.user.entity.User;
import com.taes.board.api.domain.user.service.UserService;
import com.taes.board.api.domain.user.service.UserServiceImpl;
import com.taes.board.api.exception.ApiException;
import com.taes.board.api.util.DateTimeUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class CommentAlarmEventListener
{
    private static final String COMMENT_ALARM_EXCEPTION_MSG = "댓글 알람 설정에 실패했습니다.";
    private final MailService mailService;
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public CommentAlarmEventListener(
        MailServiceImpl mailService
        , CommentService commentService
        , PostServiceImpl postService
        , UserServiceImpl userService)
    {
        this.mailService = mailService;
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @Async("CommentAlarmPool")
    @EventListener
    public void handleEvent(CommentAlarmEvent event)
    {
        MailDto mail = convertToMail(event);
        mailService.sendMail(mail);
    }

    private MailDto convertToMail(CommentAlarmEvent event)
    {
        Integer commentSeq = event.getCommentSeq();

        Comment comment = commentService.getComment(commentSeq).orElseThrow(
            () -> new ApiException(COMMENT_ALARM_EXCEPTION_MSG));

        Post post = postService.getPost(comment.getPostSeq()).orElseThrow(
            () -> new ApiException(COMMENT_ALARM_EXCEPTION_MSG));

        User postingUser = userService.getUser(post.getUserId()).orElseThrow(
            () -> new ApiException(COMMENT_ALARM_EXCEPTION_MSG));

        User commentingUser = userService.getUser(comment.getUserId()).orElseThrow(
            () -> new ApiException(COMMENT_ALARM_EXCEPTION_MSG));

        String contractPostTitle = (post.getTitle().length() > 10)
            ? StringUtils.substring(post.getTitle(), 0, 10) + "..."
            : post.getTitle();

        String title = String.format(
            "%s 게시물에 %s 님이 댓글을 달았습니다."
            , contractPostTitle
            , commentingUser.getName());

        String contents = String.format(
            "%s (%s) : %s"
            , commentingUser.getName()
            , DateTimeUtil.convertDateStringFormat(comment.getRegDt())
            , comment.getContents()
        );

        return new MailDto(postingUser.getEmail(), title, contents);
    }
}
