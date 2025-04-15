package com.app.final_project.consumer;

import com.app.final_project.config.RabbitMQConfig;
import com.app.final_project.product.EmailMessage;
import com.app.final_project.product.EmailService;
import com.app.final_project.product.TelegramLogService;
import jakarta.mail.MessagingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MessageConsumer {
    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.chat.id}")
    private String chatId;
    private final EmailService emailService;
    @Autowired
    private TelegramLogService telegramLogService;

    public MessageConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

//    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
//    public void receiveMessage(String message) {
//        System.out.println("Received message: " + message);
//    }

//    @RabbitListener(queues = "emailQueue", containerFactory = "emailQueueListenerFactory")
//    public void receiveEmailQueue(String message) throws InterruptedException {
////        Thread.sleep(5000);
//        System.out.println("📧 [Email Service] Nhận được tin nhắn: " + message);
//    }

//    @RabbitListener(queues = "smsQueue",concurrency = "5")
//    public void receiveSmsQueue(String message) {
//        System.out.println("📱 [SMS Service] Nhận được tin nhắn: " + message);
//    }
//
//    @RabbitListener(queues = "logQueue",concurrency = "5")
//    public void receiveLogQueue(String message) {
//        System.out.println("📜 [Log Service] Ghi log: " + message);
//    }
//    @RabbitListener(queues = "emailQueue")
//    public void receiveEmailNotification(EmailMessage message) {
//        System.out.println("📩 Received email notification: " + message);
//        sendEmail(message);
//    }
    @RabbitListener(queues = "emailQueue")
    public void receiveEmail(EmailMessage emailMessage) {
        System.out.println("📧 Received Email: " + emailMessage);

        try {
            emailService.sendEmail(
                    emailMessage.getTo(),
                    emailMessage.getSubject(),
                    emailMessage.getBody()
            );
        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
        }
    }
    @RabbitListener(queues = "teleQueue")
    public void consumeMessage(String message) {
        System.out.println("Received message: " + message);

        // Gửi log Telegram
        telegramLogService.sendLogToTelegram("📩 Received message from RabbitMQ: " + message);
    }


}

