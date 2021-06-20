package com.taes.board.api.domain.post.service;

import com.taes.board.api.domain.post.dto.PostDto;
import com.taes.board.api.domain.post.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostService
{
    boolean isPostWriteable(String userId);

    List<Post> getAllPostsWithPaging(int boardSeq, int page);

    Optional<Post> getPost(int postSeq);

    Post setPost(Post post);

    Post updatePost(Post post, PostDto.UpdateReq postDto);

    void deletePost(int postSeq);
}
