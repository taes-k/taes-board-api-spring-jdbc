package com.taes.board.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil
{
    private ObjectMapperUtil()
    {
        throw new IllegalStateException("Utility class");
    }

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getMapper()
    {
        return objectMapper;
    }
}
