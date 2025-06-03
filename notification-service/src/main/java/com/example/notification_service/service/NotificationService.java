package com.example.notification_service.service;

import com.example.notification_service.order.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    @Autowired
    JavaMailSender javaMailSender;

    @KafkaListener(topics = "Order_Placed")
    public void listen(OrderPlacedEvent orderPlacedEvent){
        log.info("Got Message from Order_Placed topic {}",orderPlacedEvent);
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("btechcse2024@gmail.com");
            messageHelper.setTo(orderPlacedEvent.getEmail());
            messageHelper.setSubject(String.format("Your Order with OrderNumber %s is placed successfully", orderPlacedEvent.getOrderNumber()));
            messageHelper.setText(String.format("""
                    Hi
                    
                    Your Order with order number %s is now placed Successfully.
                    
                    Best Regards
                    Spring Shop
                    """,
                    orderPlacedEvent.getOrderNumber()));
        };
        try {
            javaMailSender.send(messagePreparator);
            log.info("Order Notification email sent !!!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail");
            throw new RuntimeException("Exception occurred when sending mail to springshop@email.com",e);
        }

    }
}
