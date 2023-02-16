package com.sgdevcamp.membershipservice;

import com.sgdevcamp.membershipservice.messaging.UserEventStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableBinding(UserEventStream.class)
public class MembershipServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MembershipServiceApplication.class, args);
	}

}
