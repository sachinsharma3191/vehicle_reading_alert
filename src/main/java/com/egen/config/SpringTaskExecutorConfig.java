package com.egen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.support.ExecutorServiceAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

@Configuration
@EnableAsync
public class SpringTaskExecutorConfig {

	@Bean(value = "taskExecutor")
	public ExecutorServiceAdapter concurrentTaskExecutor() {
		return new ExecutorServiceAdapter(new ConcurrentTaskExecutor());
	}

}
