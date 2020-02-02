package com.geekbrains.septembermarket.services;


import com.geekbrains.septembermarket.entities.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MailService {
    private JavaMailSender sender;
    private MailMessageBuilder messageBuilder;

    private Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    public void setSender(JavaMailSender sender) {
        this.sender = sender;
    }

    @Autowired
    public void setMessageBuilder(MailMessageBuilder messageBuilder) {
        this.messageBuilder = messageBuilder;
    }

    private void sendMail(String email, String subject, String text) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(email);
        helper.setText(text, true);
        helper.setSubject(subject);
        sender.send(message);
    }

    public void sendOrderMail(Order order) {
        try {
            sendMail(order.getUser().getEmail(), String.format("Заказ %d%n отправлен в обработку", order.getId()), messageBuilder.buildOrderEmail(order));
        } catch (MessagingException e) {
            logger.warn("Unable to create order mail message for order: " + order.getId());
        } catch (MailException e) {
            logger.warn("Unable to send order mail message for order: " + order.getId());
        }
    }
}