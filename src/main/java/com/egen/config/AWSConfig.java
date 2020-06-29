package com.egen.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

@Configuration
public class AWSConfig {

	@Value("${cloud.aws.credentials.accessKey}")
	public String accessKey;

	@Value("${cloud.aws.credentials.secretKey}")
	public String secretKey;

	/**
	 * private Region region = Region.US_EAST_1;
	 * 
	 * @Bean("snsClient") public SnsClient snsClient() { return
	 * SnsClient.builder().region(region).credentialsProvider(EnvironmentVariableCredentialsProvider.create())
	 * .build(); }
	 * 
	 * @Bean("sqsClient") public SqsClient sqsClient() { return
	 * SqsClient.builder().region(region).credentialsProvider(c.create()) .build();
	 * }
	 */


	@Bean
	public AmazonSQS sqs() {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
		return AmazonSQSClientBuilder.standard().withRegion(com.amazonaws.regions.Regions.US_EAST_2)
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
	}
}
