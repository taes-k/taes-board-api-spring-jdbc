package com.taes.board.api.domain.comment.service;

import com.taes.board.api.domain.comment.dto.CommentDto;
import com.taes.board.api.domain.comment.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService
{
    List<Comment> getAllComments(int postSeq);

    Optional<Comment> getComment(int commentSeq);

    Comment setComment(Comment comment);

    Comment updateComment(Comment comment, CommentDto.UpdateReq commentDto);

    void deleteComment(int givenCommentSeq);
}
