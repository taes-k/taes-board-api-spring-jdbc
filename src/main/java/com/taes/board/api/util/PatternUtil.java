package com.taes.board.api.util;

import java.util.regex.Pattern;

public class PatternUtil
{
    private PatternUtil()
    {
        throw new IllegalStateException("Utility class");
    }

    public final static Pattern BAN_URL_PATTERN = Pattern.compile(RegexUtil.BAN_URL_REGEX);
}
