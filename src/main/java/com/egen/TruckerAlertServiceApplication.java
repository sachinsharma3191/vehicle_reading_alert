package com.egen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.egen.service.AlertSqsListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@SpringBootApplication
public class TruckerAlertServiceApplication {

	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
		ApplicationContext context = SpringApplication.run(TruckerAlertServiceApplication.class, args);
		AlertSqsListener sqsListener =  context.getBean(AlertSqsListener.class);
		sqsListener.startListening();
		
	}

}
