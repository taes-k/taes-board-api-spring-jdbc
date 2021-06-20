package com.taes.board.api.domain.comment.repository;

import com.taes.board.api.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer>
{
    List<Comment> findAllByPostSeq(int postSeq);
}
