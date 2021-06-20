package com.taes.board.api.domain.post.controller;

import com.taes.board.api.domain.post.dto.PostDto;
import com.taes.board.api.domain.post.entity.Post;
import com.taes.board.api.domain.post.service.PostService;
import com.taes.board.api.domain.post.validator.ContentsValidator;
import com.taes.board.api.domain.post.validator.PacadeContentsValidator;
import com.taes.board.api.domain.user.dto.UserDto;
import com.taes.board.api.domain.user.enums.Roles;
import com.taes.board.api.domain.user.service.UserService;
import com.taes.board.api.util.AuthUtil;
import com.taes.board.api.util.ConstantsUtil;
import com.taes.board.api.util.DateTimeUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
public class PostController
{
    private final PostService postService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ContentsValidator contentsValidator;

    @Autowired
    public PostController(PostService postService
        , UserService userService
        , ModelMapper modelMapper
        , PacadeContentsValidator contentsValidator)
    {
        this.postService = postService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.contentsValidator = contentsValidator;
    }

    @GetMapping("/boards/{boardSeq}/posts")
    public List<PostDto.Res> getAllPostsWithPaging(
        @PathVariable("boardSeq") int boardSeq
        , @RequestParam(value = "page", required = false, defaultValue = "0") int page)
    {
        return postService.getAllPostsWithPaging(boardSeq, page).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/posts/{postSeq}")
    public PostDto.Res getPost(
        @PathVariable("postSeq") int postSeq)
    {
        return postService.getPost(postSeq)
            .map(this::convertToDto)
            .orElseThrow(() -> new IllegalArgumentException(ConstantsUtil.NOT_FOUND_POST_EXCEPTION_MSG));
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/posts/check/writeable")
    public boolean isPostWriteable()
    {
        return postService.isPostWriteable(AuthUtil.getUserId());
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/posts")
    public PostDto.Res setPost(
        @RequestBody @Valid PostDto.CreateReq postDto)
    {
        if (!postService.isPostWriteable(AuthUtil.getUserId()))
        {
            throw new IllegalStateException(ConstantsUtil.EXCEEDED_LIMIT_POST_WRITE_EXCEPTION_MSG);
        }

        Post post = convertToEntity(postDto);
        contentsValidator.validate(postDto.getContents());

        return convertToDto(postService.setPost(post));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/posts/{postSeq}")
    public PostDto.Res updatePost(
        @PathVariable("postSeq") int postSeq
        , @RequestBody @Valid PostDto.UpdateReq postDto)
    {
        Post post = postService.getPost(postSeq)
            .orElseThrow(() -> new IllegalArgumentException(ConstantsUtil.NOT_FOUND_POST_EXCEPTION_MSG));

        contentsValidator.validate(postDto.getContents());

        if (isNotSelfWrittenPost(post))
            throw new IllegalStateException(ConstantsUtil.UNAUTHORIZED_USER_EXCEPTION_MSG);

        post = postService.updatePost(post, postDto);
        return convertToDto(post);
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @DeleteMapping("/posts/{postSeq}")
    public void deletePost(
        @PathVariable("postSeq") int postSeq)
    {
        Post post = postService.getPost(postSeq)
            .orElseThrow(() -> new IllegalArgumentException(ConstantsUtil.NOT_FOUND_POST_EXCEPTION_MSG));

        if (isNotAdminUser() && isNotSelfWrittenPost(post))
            throw new IllegalStateException(ConstantsUtil.UNAUTHORIZED_USER_EXCEPTION_MSG);

        postService.deletePost(postSeq);
    }

    private boolean isNotAdminUser()
    {
        return !CollectionUtils.containsAny(AuthUtil.getUserAuthorities(), Roles.ADMIN.name());
    }

    private boolean isNotSelfWrittenPost(Post post)
    {
        return !StringUtils.equals(post.getUserId(), AuthUtil.getUserId());
    }

    private Post convertToEntity(PostDto.CreateReq postDto)
    {
        log.debug("postDto : {}", postDto);
        Post post = modelMapper.typeMap(PostDto.CreateReq.class, Post.class)
            .addMappings(mapper -> mapper.map(src -> AuthUtil.getUserId(), Post::setUserId))
            .map(postDto);

        log.debug("entity : {}", postDto);
        return post;
    }

    private PostDto.Res convertToDto(Post post)
    {
        PostDto.Res postDto = modelMapper.typeMap(Post.class, PostDto.Res.class)
            .map(post);

        postDto.setRegDt(DateTimeUtil.convertDateStringFormat(post.getRegDt()));

        UserDto.BasicRes user = userService.getUser(post.getUserId())
            .map(u -> new UserDto.BasicRes(u.getId(), u.getRanking()))
            .orElseGet(() -> new UserDto.BasicRes(post.getUserId()));

        postDto.setWriter(user);

        log.debug("dto : {}", postDto);
        return postDto;
    }

}
