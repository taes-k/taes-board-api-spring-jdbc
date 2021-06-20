package com.taes.board.api.domain.post.service;

import com.taes.board.api.domain.post.dto.PostDto;
import com.taes.board.api.domain.post.entity.Post;
import com.taes.board.api.domain.post.repository.PostRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class PostServiceImpl implements PostService
{
    private int pagingSize;
    private int postingDailyLimit;
    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(
        @Value("${taes-board.post.page-size:20}") int pagingSize
        , @Value("${taes-board.post.daily-limit:5}") int postingDailyLimit
        , PostRepository postRepository)
    {
        this.pagingSize = pagingSize;
        this.postingDailyLimit = postingDailyLimit;
        this.postRepository = postRepository;
    }

    public boolean isPostWriteable(String userId)
    {
        Long todayWrittenCount = postRepository.countByUserIdAndCurrentDate(userId);
        log.debug("Today writtenCount : {}", todayWrittenCount);
        return (todayWrittenCount <= postingDailyLimit);
    }

    public List<Post> getAllPostsWithPaging(int boardSeq, int page)
    {
        Pageable pageable = PageRequest.of(page, pagingSize, Sort.by(Sort.Direction.DESC, "regDt"));
        return postRepository.findAllByBoardSeq(boardSeq, pageable);
    }

    public Optional<Post> getPost(int postSeq)
    {
        return postRepository.findById(postSeq);
    }

    @Transactional
    public Post setPost(Post post)
    {
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Post post, PostDto.UpdateReq postDto)
    {
        updatePostInfo(post, postDto);
        return post;
    }

    private void updatePostInfo(Post post, PostDto.UpdateReq postDto)
    {
        if (StringUtils.isNotEmpty(postDto.getTitle()))
        {
            post.setTitle(postDto.getTitle());
        }
        if (StringUtils.isNotEmpty(postDto.getContents()))
        {
            post.setContents(postDto.getContents());
        }
    }

    @Transactional
    public void deletePost(int postSeq)
    {
        postRepository.deleteById(postSeq);
    }
}
