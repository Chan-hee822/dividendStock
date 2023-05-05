package com.example.dividendstock.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;


/*
	스케줄러에서 우리가 만든 스레드풀 사용할 수 있게
	기본적으로 스프링 부트 스레드 하나 가지고 처리
	여러 스레드 필요할 때 스레드 풀 사용
	cpu 많이 -> 코어수 + 1, I/O 많이 -> 코어수 * 2
 */
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler treadPool = new ThreadPoolTaskScheduler();

		// 코어수
		int core_n = Runtime.getRuntime().availableProcessors();

		treadPool.setPoolSize(core_n + 1);
		treadPool.initialize();

		taskRegistrar.setTaskScheduler(treadPool);
	}

}
