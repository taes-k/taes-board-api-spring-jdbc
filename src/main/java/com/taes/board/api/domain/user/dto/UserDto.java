package com.taes.board.api.domain.user.dto;

import com.taes.board.api.util.RegexUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
public class UserDto
{
    private UserDto()
    {
        throw new IllegalStateException("outer class");
    }

    @Getter
    @Setter
    public abstract static class Base
    {
        @Size(max = 40, message = "ID가 너무 깁니다.")
        @NotBlank
        @ApiModelProperty(value = "User ID", required = true)
        protected String id;

        @Size(max = 20, message = "이름이 너무 깁니다.")
        @ApiModelProperty(value = "User 이름")
        protected String name;

        @Size(max = 200, message = "이메일 길이가 너무 깁니다.")
        @Pattern(regexp = RegexUtil.EMAIL_REGEX, message = "이메일 형식이 올바르지 않습니다.")
        @ApiModelProperty(value = "User 이메일")
        protected String email;
    }

    @ApiModel("User 생성 Request DTO")
    @Getter
    @Setter
    public static class CreateReq extends Base
    {
        @NotBlank
        @ApiModelProperty(value = "User 패스워드", required = true)
        private String password;

        @NotBlank
        @ApiModelProperty(value = "User 이름", required = true)
        protected String name;

        @Size(max = 200, message = "이메일 길이가 너무 깁니다.")
        @Pattern(regexp = RegexUtil.EMAIL_REGEX, message = "이메일 형식이 올바르지 않습니다.")
        @NotBlank
        @ApiModelProperty(value = "User 이메일", required = true)
        protected String email;

        @ApiModelProperty(value = "User Role", required = true, example = "USER")
        private List<String> roles;
    }

    @ApiModel("User 수정 Request DTO")
    @Getter
    @Setter
    public static class UpdateReq extends Base
    {
    }

    @ApiModel("User 삭제 Request DTO")
    @Getter
    @Setter
    public static class DeleteReq
    {
        @NotBlank
        @ApiModelProperty(value = "User ID", required = true)
        private String id;
    }

    //===================================================================================================================

    @ApiModel("User Response DTO")
    @Getter
    @Setter
    public static class Res extends Base
    {
        private LocalDateTime regDt;

        private List<String> roles;

        private Integer rank;

        @Override
        public String toString()
        {
            return "Res{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", regDt=" + regDt +
                '}';
        }
    }

    @ApiModel("User Basic Response DTO")
    @Getter
    @NoArgsConstructor
    public static class BasicRes
    {
        private String id;

        private Integer rank;

        public BasicRes(String id)
        {
            this.id = id;
        }

        public BasicRes(String id, Integer rank)
        {
            this.id = id;
            this.rank = rank;
        }

        @Override
        public String toString()
        {
            return "BasicRes{" +
                "id='" + id + '\'' +
                ", rank=" + rank +
                '}';
        }
    }
}
