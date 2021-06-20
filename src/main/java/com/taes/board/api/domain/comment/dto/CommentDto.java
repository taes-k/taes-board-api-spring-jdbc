package com.taes.board.api.domain.comment.dto;

import com.taes.board.api.domain.user.dto.UserDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CommentDto
{
    private CommentDto()
    {
        throw new IllegalStateException("outer class");
    }

    @Getter
    @Setter
    public abstract static class Base
    {
        @Size(max = 500, message = "내용이 너무 깁니다.")
        protected String contents;
    }

    @ApiModel("Comment 생성 Request DTO")
    @Getter
    @Setter
    public static class CreateReq extends CommentDto.Base
    {
    }

    @ApiModel("Comment 수정 Request DTO")
    @Getter
    @Setter
    public static class UpdateReq extends CommentDto.Base
    {
    }

    //===================================================================================================================

    @ApiModel("Comment Response DTO")
    @Getter
    @Setter
    public static class Res extends CommentDto.Base
    {
        private Integer seq;

        private Integer postSeq;

        private UserDto.BasicRes user;

        private String regDt;

        @Override
        public String toString()
        {
            return "Res{" +
                "contents='" + contents + '\'' +
                ", seq=" + seq +
                ", postSeq=" + postSeq +
                ", user=" + user +
                ", regDt='" + regDt + '\'' +
                '}';
        }
    }

}
