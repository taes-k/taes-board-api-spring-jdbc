package com.taes.board.api.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil
{
    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DateTimeUtil()
    {
        throw new IllegalStateException("Utility class");
    }

    public static String convertDateStringFormat(LocalDateTime localDateTime)
    {
        return localDateTime.format(format);
    }
}
