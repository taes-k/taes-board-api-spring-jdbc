package com.taes.board.api.domain.comment.controller;

import com.taes.board.api.domain.comment.dto.CommentDto;
import com.taes.board.api.domain.comment.entity.Comment;
import com.taes.board.api.domain.comment.service.CommentService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
public class CommentController
{
    private final CommentService commentService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public CommentController(
        CommentService commentService
        , UserService userService
        , ModelMapper modelMapper)
    {
        this.commentService = commentService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/posts/{postSeq}/comments")
    public List<CommentDto.Res> getAllComments(
        @PathVariable("postSeq") int postSeq)
    {
        return commentService.getAllComments(postSeq).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/posts/{postSeq}/comments")
    public CommentDto.Res setComment(
        @PathVariable("postSeq") int postSeq
        , @RequestBody @Valid CommentDto.CreateReq commentDto)
    {
        Comment comment = convertToEntity(commentDto);
        comment.setPostSeq(postSeq);

        return convertToDto(commentService.setComment(comment));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/posts/{postSeq}/comments/{commentSeq}")
    public CommentDto.Res updatePost(
        @PathVariable("postSeq") int postSeq
        , @PathVariable("commentSeq") int commentSeq
        , @RequestBody @Valid CommentDto.UpdateReq commentDto)
    {
        Comment comment = commentService.getComment(commentSeq)
            .orElseThrow(() -> new IllegalArgumentException(ConstantsUtil.NOT_FOUND_POST_EXCEPTION_MSG));

        comment.setPostSeq(postSeq);

        if (isNotSelfWrittenComment(comment))
            throw new IllegalStateException(ConstantsUtil.UNAUTHORIZED_USER_EXCEPTION_MSG);

        comment = commentService.updateComment(comment, commentDto);
        return convertToDto(comment);
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @DeleteMapping("/posts/{postSeq}/comments/{commentSeq}")
    public void deletePost(
        @PathVariable("postSeq") int postSeq
        , @PathVariable("commentSeq") int commentSeq)
    {
        Comment comment = commentService.getComment(commentSeq)
            .orElseThrow(() -> new IllegalArgumentException(ConstantsUtil.NOT_FOUND_POST_EXCEPTION_MSG));

        if (isNotAdminUser() && isNotSelfWrittenComment(comment))
            throw new IllegalStateException(ConstantsUtil.UNAUTHORIZED_USER_EXCEPTION_MSG);

        commentService.deleteComment(commentSeq);
    }

    private boolean isNotAdminUser()
    {
        return !CollectionUtils.containsAny(AuthUtil.getUserAuthorities(), Roles.ADMIN.name());
    }

    private boolean isNotSelfWrittenComment(Comment comment)
    {
        return !StringUtils.equals(comment.getUserId(), AuthUtil.getUserId());
    }

    private Comment convertToEntity(CommentDto.CreateReq commentDto)
    {
        log.debug("postDto : {}", commentDto);
        Comment comment = modelMapper.typeMap(CommentDto.CreateReq.class, Comment.class)
            .addMappings(mapper -> mapper.map(src -> AuthUtil.getUserId(), Comment::setUserId))
            .map(commentDto);

        log.debug("entity : {}", commentDto);
        return comment;
    }

    private CommentDto.Res convertToDto(Comment comment)
    {
        CommentDto.Res commentDto = modelMapper.typeMap(Comment.class, CommentDto.Res.class)
            .addMappings(mapper -> mapper.skip(CommentDto.Res::setUser))
            .map(comment);

        UserDto.BasicRes user = userService.getUser(comment.getUserId())
            .map(u -> new UserDto.BasicRes(u.getId(), u.getRanking()))
            .orElseGet(() -> new UserDto.BasicRes(comment.getUserId()));

        commentDto.setUser(user);
        commentDto.setRegDt(DateTimeUtil.convertDateStringFormat(comment.getRegDt()));

        log.debug("dto : {}", commentDto);
        return commentDto;
    }
}
