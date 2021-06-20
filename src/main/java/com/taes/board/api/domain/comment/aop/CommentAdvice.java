package com.taes.board.api.domain.comment.aop;

import com.taes.board.api.domain.comment.entity.Comment;
import com.taes.board.api.domain.comment.event.CommentAlarmEvent;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j2
public class CommentAdvice
{
    private static final String MAIL_ERROR_MESSAGE = "메서드 처리중에 에러가 발생해 댓글 알람 메일을 보내지않습니다.";

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public CommentAdvice(ApplicationEventPublisher applicationEventPublisher)
    {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Around(value = "execution(* com.taes.board.api.domain.comment.service.CommentService.setComment(..))")
    public Object proceed(ProceedingJoinPoint jp) throws Throwable
    {
        try
        {
            //-----------------------------------------------------------------------------------------------------------
            Comment result = (Comment) jp.proceed();
            //-----------------------------------------------------------------------------------------------------------
            CommentAlarmEvent event = new CommentAlarmEvent(result.getSeq());
            applicationEventPublisher.publishEvent(event);

            return result;
        }
        catch (Throwable t)
        {
            log.warn(MAIL_ERROR_MESSAGE, t);
            throw t;
        }
    }
}
