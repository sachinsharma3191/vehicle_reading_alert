package com.egen.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.egen.entity.SQSMessage;
import com.egen.entity.VehicleAlert;
import com.egen.exception.AlertServicException;
import com.egen.repository.AlertRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AlertSqsListener {

	Logger log = Logger.getLogger("AlertSqsListener");

	@Value("${SQS_URL}")
	String sqsUrl;

	@Autowired
	AmazonSQS sqs;

	@Autowired
	AlertRepo repo;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	AlertService alertService;

	public void startListening() {

		while (true) {
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsUrl).withWaitTimeSeconds(10)
					.withMaxNumberOfMessages(10);

			List<Message> sqsMessages = sqs.receiveMessage(receiveMessageRequest).getMessages();

			try {
				for (Message msgObject : sqsMessages) {
					String message = msgObject.getBody();
					SQSMessage sqsMessage = objectMapper.readValue(message, SQSMessage.class);
					VehicleAlert vehicleAlert = objectMapper.readValue(sqsMessage.getMessage(), VehicleAlert.class);
					log.info(vehicleAlert.toString());

					VehicleAlert saved = repo.save(vehicleAlert);
					log.info("Alert created for " + saved.getVin());
					deleteMessage(msgObject);

				}
			} catch (JsonProcessingException e) {
				throw new AlertServicException("Failed to save alerts", e.getCause());
			}
		}

	}

	public void deleteMessage(Message msgObj) {

		final String receipient = msgObj.getReceiptHandle();

		sqs.deleteMessage(new DeleteMessageRequest(sqsUrl, receipient));
	}
}
