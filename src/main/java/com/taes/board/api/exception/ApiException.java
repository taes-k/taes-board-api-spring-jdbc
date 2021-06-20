package com.taes.board.api.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException
{
    public ApiException(String message)
    {
        super(message);
    }
}
