package com.lph.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * rabbitmq配置类-topic模式
 */
@Configuration
public class RabbitMQConfigTopic {

//    private static final String QUEUE01 = "queue_topic01";
//    private static final String QUEUE02 = "queue_topic02";
//    private static final String EXCHANGE = "topicExchange";
//    private static final String ROUTINGKEY01 = "#.queue.#";
//    private static final String ROUTINGKEY02 = "*.queue.#";

//    @Bean
//    public Queue queue(){
//        return new Queue("queue",true);
//    }
//
//    //生成队列
//    @Bean
//    public Queue queue01(){
//        return new Queue(QUEUE01);
//    }
//
//    @Bean
//    public Queue queue02(){
//        return new Queue(QUEUE02);
//    }
//    //生成交换机
//    @Bean
//    public TopicExchange TopicExchange(){
//        return new TopicExchange(EXCHANGE);
//    }
//
//    //队列绑定交换机
//    @Bean
//    public Binding binding01(){
//        return BindingBuilder.bind(queue01()).to(TopicExchange()).with(ROUTINGKEY01);
//    }
//
//    @Bean
//    public Binding binding02(){
//        return BindingBuilder.bind(queue02()).to(TopicExchange()).with(ROUTINGKEY02);
//    }

    private static final String QUEUE = "seckillQueue";
    private static final String EXCHANGE = "seckillExchange";

    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");
    }
}