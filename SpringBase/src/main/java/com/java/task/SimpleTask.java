package com.java.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class SimpleTask extends QuartzJobBean {
	private static final Logger logger = LoggerFactory.getLogger(SimpleTask.class);
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		Thread thread = Thread.currentThread();
		logger.debug("in SimpleTask start : " + thread.getName());
		try {
			logger.debug("in SimpleTask doing.... : " + thread.getName());
			thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.debug("in SimpleTask end : " + thread.getName());
	}
}
