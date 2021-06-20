package com.taes.board.api.common.dto;

import lombok.Getter;

@Getter
public class MailDto
{
    private String receiverAddress;
    private String title;
    private String contents;

    public MailDto(String receiverAddress, String title, String contents)
    {
        this.receiverAddress = receiverAddress;
        this.title = title;
        this.contents = contents;
    }
}
