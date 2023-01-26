package com.sgdevcamp.membershipservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MembershipServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MembershipServiceApplication.class, args);
	}

}
