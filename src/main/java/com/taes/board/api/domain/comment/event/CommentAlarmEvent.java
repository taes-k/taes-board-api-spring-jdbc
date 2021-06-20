package com.taes.board.api.domain.comment.event;

import lombok.Getter;

@Getter
public class CommentAlarmEvent
{
    private Integer commentSeq;

    public CommentAlarmEvent(Integer commentSeq)
    {
        this.commentSeq = commentSeq;
    }
}
