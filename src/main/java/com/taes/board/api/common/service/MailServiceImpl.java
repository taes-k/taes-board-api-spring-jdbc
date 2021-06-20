package com.taes.board.api.common.service;

import com.taes.board.api.common.dto.MailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService
{
    private final JavaMailSender mailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender)
    {
        this.mailSender = mailSender;
    }

    public void sendMail(MailDto mail)
    {
        mailSender.send(getConvertedMessage(mail));
    }

    private SimpleMailMessage getConvertedMessage(MailDto mail)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getReceiverAddress());
        message.setSubject(mail.getTitle());
        message.setText(mail.getContents());

        return message;
    }
}
