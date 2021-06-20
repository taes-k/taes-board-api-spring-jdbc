package com.taes.board.api.domain.comment.service;

import com.taes.board.api.domain.comment.dto.CommentDto;
import com.taes.board.api.domain.comment.entity.Comment;
import com.taes.board.api.domain.comment.repository.CommentRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService
{
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository)
    {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllComments(int postSeq)
    {
        return commentRepository.findAllByPostSeq(postSeq);
    }

    public Optional<Comment> getComment(int commentSeq)
    {
        return commentRepository.findById(commentSeq);
    }

    @Transactional
    public Comment setComment(Comment comment)
    {
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(Comment comment, CommentDto.UpdateReq commentDto)
    {
        updateCommentInfo(comment, commentDto);
        return comment;
    }

    private void updateCommentInfo(Comment comment, CommentDto.UpdateReq commentDto)
    {
        if (StringUtils.isNotEmpty(commentDto.getContents()))
        {
            comment.setContents(commentDto.getContents());
        }
    }

    @Transactional
    public void deleteComment(int commentSeq)
    {
        commentRepository.deleteById(commentSeq);
    }
}
