package com.chirag.review_ms.messaging;


import com.chirag.review_ms.entity.Review;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewMessageProducer
{
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ReviewMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Review review)
    {
        rabbitTemplate.convertAndSend("companyRatingQueue",review);
    }
}
