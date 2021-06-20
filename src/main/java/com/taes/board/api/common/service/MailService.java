package com.taes.board.api.common.service;

import com.taes.board.api.common.dto.MailDto;

public interface MailService
{
    void sendMail(MailDto mail);
}
