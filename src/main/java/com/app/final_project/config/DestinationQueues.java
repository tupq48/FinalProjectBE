package com.app.final_project.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Getter
@Setter
public class DestinationQueues {
    private Map<String, QueueProperties> queues;
    public DestinationQueues() {
        System.out.println("🐰 DestinationQueues constructor called!");
    }
    @PostConstruct
    public void init() {
        System.out.println("🐰 DestinationQueues loaded: " + queues);
        if (queues != null) {
            queues.forEach((key, value) ->
                    System.out.println("📌 Queue: " + key + " -> " + value));
        } else {
            System.out.println("❌ queues is NULL!");
        }
    }

    @Getter
    @Setter
    public static class QueueProperties {
        private String exchange;
        private String type;
        private String queueName;
        private String routingKey;
    }
}
