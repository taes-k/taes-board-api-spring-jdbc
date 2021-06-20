package com.taes.board.api.util;

public class RegexUtil
{
    private RegexUtil()
    {
        throw new IllegalStateException("Utility class");
    }

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    public static final String BAN_URL_REGEX = "naver\\.com|daum\\.net";
}
