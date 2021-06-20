package com.taes.board.api.domain.post.dto;

import com.taes.board.api.domain.user.dto.UserDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class PostDto
{
    private PostDto()
    {
        throw new IllegalStateException("outer class");
    }

    @Getter
    @Setter
    public abstract static class Base
    {
        @Size(max = 300, message = "제목이 너무 깁니다.")
        protected String title;

        protected String contents;
    }

    @ApiModel("Post 생성 Request DTO")
    @Getter
    @Setter
    public static class CreateReq extends PostDto.Base
    {
        @NotBlank
        @Size(max = 300, message = "제목이 너무 깁니다.")
        protected String title;

        @NotBlank
        protected String contents;

        @NotNull
        protected Integer boardSeq;

        @Override
        public String toString()
        {
            return "CreateReq{" +
                "title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                '}';
        }
    }

    @ApiModel("Post 수정 Request DTO")
    @Getter
    @Setter
    public static class UpdateReq extends PostDto.Base
    {
    }

    //===================================================================================================================

    @ApiModel("Post Response DTO")
    @Getter
    @Setter
    public static class Res extends PostDto.Base
    {
        protected Integer seq;

        protected Integer boardSeq;

        protected UserDto.BasicRes writer;

        protected String regDt;

        @Override
        public String toString()
        {
            return "Res{" +
                "title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", seq=" + seq +
                ", boardSeq=" + boardSeq +
                ", writer=" + writer +
                ", regDt=" + regDt +
                '}';
        }
    }

}
