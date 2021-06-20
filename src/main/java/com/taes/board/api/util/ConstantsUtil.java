package com.taes.board.api.util;

public class ConstantsUtil
{
    private ConstantsUtil()
    {
        throw new IllegalStateException("Utility class");
    }

    public static final String DUPLICATE_USER_EXCEPTION_MSG = "이미 존재하는 사용자 ID 입니다." ;

    public static final String NOT_FOUND_USER_EXCEPTION_MSG = "존재하지 않는 사용자 입니다." ;
    public static final String NOT_FOUND_BOARD_EXCEPTION_MSG = "존재하지 않는 게시판 입니다." ;
    public static final String NOT_FOUND_POST_EXCEPTION_MSG = "존재하지 않는 게시물 입니다." ;

    public static final String UNAUTHORIZED_USER_EXCEPTION_MSG = "권한이 없는 사용자입니다." ;

    public static final String EXCEEDED_LIMIT_POST_WRITE_EXCEPTION_MSG = "일일 게시물 작성 제한 갯수가 초과되었습니다." ;
    public static final String PROHIBITIED_WORDS_EXCEPTION_MSG = "금칙어가 포함되어 있습니다." ;
    public static final String PROHIBITIED_URLS_EXCEPTION_MSG = "금지된 링크가 포함되어 있습니다." ;
}
