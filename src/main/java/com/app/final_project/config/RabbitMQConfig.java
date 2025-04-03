package com.app.final_project.config;

import com.rabbitmq.client.Channel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.ILoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Configuration
//@EnableConfigurationProperties(DestinationQueues.class)
@RequiredArgsConstructor
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.port}")
    private int port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;
//    public static final String QUEUE_NAME = "simpleQueue";
    public static final String EXCHANGE_NAME = "simpleExchange";
    public static final String ROUTING_KEY = "simpleRoutingKey";
    public static final String EXCHANGE_NAME_FANOUT = "fanout-exchange";

    public static final String EXCHANGE_NAME1 = "emailExchange";
    public static final String EXCHANGE_NAME2 = "teleExchange";
    public static final String QUEUE_NAME1 = "emailQueue";
    public static final String ROUTING_KEY1 = "emailRoutingKey";
    public static final String ROUTING_KEY2 = "teleRoutingKey";

    @Autowired
    private DestinationQueues destinationQueueConfig;

    @PostConstruct
    public void init() {
        System.out.println("RabbitMQ Config: " + ":" + port + ", " + username + ", " + password);
    }


//    public RabbitMQConfig(DestinationQueues destinationQueueConfig) {
//        this.destinationQueueConfig = destinationQueueConfig;
//        System.out.println("ahii");
//        System.out.println(destinationQueueConfig.getQueues());
//    }


    //    @Bean
//    public FanoutExchange fanoutExchange() {
//        return new FanoutExchange(EXCHANGE_NAME_FANOUT);
//    }
//
//    @Bean
//    public Queue queue1() {
//        return new Queue("emailQueue");
//    }

//    @Bean
//    public Queue queue2() {
//        return new Queue("smsQueue");
//    }
//
//    @Bean
//    public Queue queue3() {
//        return new Queue("logQueue");
//    }

//    @Bean
//    public Binding binding1(Queue queue1, FanoutExchange exchange) {
//        return BindingBuilder.bind(queue1).to(exchange);
//    }

//    @Bean
//    public Binding binding2(Queue queue2, FanoutExchange exchange) {
//        return BindingBuilder.bind(queue2).to(exchange);
//    }
//
//    @Bean
//    public Binding binding3(Queue queue3, FanoutExchange exchange) {
//        return BindingBuilder.bind(queue3).to(exchange);
//    }
//    @Bean
//    public Queue queue() {
//        return new Queue(QUEUE_NAME, false);
//    }
//
//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange(EXCHANGE_NAME);
//    }
//
//    @Bean
//    public Binding binding(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
//    }
    @Bean
    public SimpleRabbitListenerContainerFactory emailQueueListenerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(2); // Tương đương concurrency = "5"
        factory.setMaxConcurrentConsumers(10); // Giới hạn tối đa số consumer
        factory.setPrefetchCount(5); // Số message tối đa mỗi consumer có thể xử lý cùng lúc
        return factory;
    }
//    @Bean
//    public DirectExchange exchangeMail() {
//        return new DirectExchange(EXCHANGE_NAME1);
//    }
//
//    @Bean
//    public Queue queueMail() {
//        return new Queue(QUEUE_NAME1, true);
//    }
//
//    @Bean
//    public Binding bindingMail(Queue queueMail, DirectExchange exchangeMail) {
//        return BindingBuilder.bind(queueMail).to(exchangeMail).with(ROUTING_KEY1);
//    }
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter()); // Sử dụng Jackson để convert JSON
        return template;
    }
//    @Bean
//    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
//        return new RabbitAdmin(connectionFactory);
//    }
    @Bean()
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        initializeQueues(connectionFactory);
        return connectionFactory;
    }

    private void initializeQueues(ConnectionFactory connectionFactory){
        try (Connection connection = connectionFactory.createConnection();
             Channel channel = connection.createChannel(true)) {
            destinationQueueConfig.getQueues().forEach((key, destination) -> {
                try {
                    System.out.println(destination.getQueueName());
                    channel.queueDeclare(destination.getQueueName(), true, false, false, Map.of("x-max-length", 5000));
                    channel.exchangeDeclare(destination.getExchange(), destination.getType(), true, false, null);
                    channel.queueBind(destination.getQueueName(), destination.getExchange(), destination.getRoutingKey());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }
    }

