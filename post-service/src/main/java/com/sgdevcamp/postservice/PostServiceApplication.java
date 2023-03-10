package com.sgdevcamp.postservice;

import com.sgdevcamp.postservice.messaging.PostEventStream;
import com.sgdevcamp.postservice.messaging.UserEventStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoAuditing
@SpringBootApplication
@EnableMongoRepositories
@EnableBinding(value = {PostEventStream.class, UserEventStream.class})

public class PostServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostServiceApplication.class, args);
	}

}
