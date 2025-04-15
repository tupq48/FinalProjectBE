package com.app.final_project.producer;

import com.app.final_project.config.RabbitMQConfig;
import com.app.final_project.product.EmailMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message);
        System.out.println("Sent message: " + message);
    }
    public void sendEmailNotification(String email, String subject, String content) {
        EmailMessage message = new EmailMessage(email, subject, content);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME1, RabbitMQConfig.ROUTING_KEY1, message);
        System.out.println("📤 Sent email notification to queue: " + email);
    }

    public String sendMessageFanout() {
        for (int i = 1; i <= 10; i++) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME_FANOUT, "", "Message " + i);
            System.out.println("Sent: Message " + i);
        }
        return "Messages sent!";
    }
    public void sendMessageToQueue(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME2, RabbitMQConfig.ROUTING_KEY2, message);
        System.out.println("Sent message to RabbitMQ: " + message);
    }
}

