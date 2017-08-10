package com.java.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class SimpleTwoTask extends QuartzJobBean {
	private static final Logger logger = LoggerFactory.getLogger(SimpleTwoTask.class);
//	@Override
//	public void execute(JobExecutionContext context) throws JobExecutionException {
//		System.out.println("in SimpleTwoTask");
//	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		Thread thread = Thread.currentThread();
		logger.debug("in Simple2Task start : " + thread.getName());
		try {
			logger.debug("in Simple2Task doing.... : " + thread.getName());
			thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.debug("in Simple2Task end : " + thread.getName());
	}

}
