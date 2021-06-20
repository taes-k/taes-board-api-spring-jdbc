package com.taes.board.api.domain.board.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
public class BoardDto
{
    private BoardDto()
    {
        throw new IllegalStateException("outer class");
    }

    @Getter
    @Setter
    public abstract static class Base
    {
        protected Integer seq;

        @Size(max = 100, message = "제목이 너무 깁니다.")
        protected String title;

        @Size(max = 200, message = "설명이 너무 깁니다.")
        protected String contents;
    }

    @ApiModel("Board 생성 Request DTO")
    @Getter
    @Setter
    public static class CreateReq extends Base
    {
        @Size(max = 100, message = "제목이 너무 깁니다.")
        @NotBlank
        protected String title;
    }

    @ApiModel("Board 수정 Request DTO")
    @Getter
    @Setter
    public static class UpdateReq extends Base
    {
        @Override
        public String toString()
        {
            return "UpdateReq{" +
                "seq=" + seq +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                '}';
        }
    }

    //===================================================================================================================

    @ApiModel("Board Response DTO")
    @Getter
    @Setter
    public static class Res extends Base
    {
        @Override
        public String toString()
        {
            return "Res{" +
                "seq=" + seq +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                '}';
        }
    }

}
