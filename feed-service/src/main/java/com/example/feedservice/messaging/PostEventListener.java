package com.example.feedservice.messaging;

import com.example.feedservice.model.Post;
import com.example.feedservice.payload.PostEventPayload;
import com.example.feedservice.service.FeedGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostEventListener {

    private final FeedGeneratorService feedGeneratorService;

    @StreamListener(PostEventStream.INPUT)
    public void onMessage(Message<PostEventPayload> message){

        PostEventType eventType = message.getPayload().getEventType();

        log.info("received message to process post {} for user {} eventType {}",
                message.getPayload().getId(),
                message.getPayload().getUsername(),
                eventType.name());

        Acknowledgment acknowledgment = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);

        switch(eventType){
            case CREATED:
                feedGeneratorService.addToFeed(convertTo(message.getPayload()));
                break;
            case DELETED:
                break;
        }

        if(acknowledgment != null){
            acknowledgment.acknowledge();
        }
    }

    private Post convertTo(PostEventPayload postEventPayload){
        return Post
                .builder()
                .id(postEventPayload.getId())
                .createdAt(postEventPayload.getCreatedAt())
                .username(postEventPayload.getUsername())
                .build();
    }
}
