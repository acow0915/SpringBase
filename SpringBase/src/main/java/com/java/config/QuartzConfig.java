package com.java.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.java.task.SimpleTask;
import com.java.task.SimpleTwoTask;

@Configuration
public class QuartzConfig {
	
	@Autowired  
    private ResourceLoader resourceLoader;  
	
	@Resource(name="quartzDataSource")
	private DataSource quartzDataSource;

	// 配置中设定了
    // ① targetMethod: 指定需要定时执行scheduleInfoAction中的simpleJobTest()方法
    // ② concurrent：对于相同的JobDetail，当指定多个Trigger时, 很可能第一个job完成之前，
    // 第二个job就开始了。指定concurrent设为false，多个job不会并发运行，第二个job将不会在第一个job完成之前开始。
    // ③ cronExpression：0/10 * * * * ?表示每10秒执行一次，具体可参考附表。
    // ④ triggers：通过再添加其他的ref元素可在list中放置多个触发器。 scheduleInfoAction中的simpleJobTest()方法
	/** 使用此種方式 不可  繼承 QuartzJobBean*/
//    @Bean(name = "simpleFactoryBean")
//    public MethodInvokingJobDetailFactoryBean simpleFactoryBean(){
//        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean ();
//        bean.setTargetObject (simpleTask);
//        bean.setTargetMethod ("execute");
//        bean.setConcurrent (false);
//        return bean;
//    }
    
	/** 使用此種方式 需要繼承 QuartzJobBean*/
	@Bean(name = "simpleFactoryBean")
    public JobDetailFactoryBean simpleJobDetailFactoryBean(){
    	JobDetailFactoryBean bean = new JobDetailFactoryBean();
    	bean.setJobClass(SimpleTask.class);
    	return bean;
    }

    @Bean(name = "simpleCronTriggerBean")
    public CronTriggerFactoryBean simpleCronTriggerBean(){
    	CronTriggerFactoryBean tigger = new CronTriggerFactoryBean ();
        tigger.setJobDetail (simpleJobDetailFactoryBean().getObject ());
        try {
            tigger.setCronExpression ("0/20 * * * * ? ");//每5秒执行一次
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return tigger;
    }
    
    /** 使用此種方式 不可  繼承 QuartzJobBean*/
//    @Bean(name = "simpleTwoFactoryBean")
//    public MethodInvokingJobDetailFactoryBean simpleTwoFactoryBean(){
//        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean ();
//        bean.setTargetObject (simpleTwoTask);
//        bean.setTargetMethod ("execute");
//        bean.setConcurrent (false);
//        return bean;
//    }
    
    /** 使用此種方式 需要繼承 QuartzJobBean*/
    @Bean(name = "simpleTwoFactoryBean")
    public JobDetailFactoryBean simpleTwoJobDetailFactoryBean(){
    	JobDetailFactoryBean bean = new JobDetailFactoryBean();
    	bean.setJobClass(SimpleTwoTask.class);
    	return bean;
    }

    @Bean(name = "simpleTwoCronTriggerBean")
    public CronTriggerFactoryBean simpleTwoCronTriggerBean(){
    	CronTriggerFactoryBean tigger = new CronTriggerFactoryBean ();
        tigger.setJobDetail (simpleTwoJobDetailFactoryBean().getObject ());
        try {
            tigger.setCronExpression ("0/30 * * * * ? ");//每5秒执行一次
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return tigger;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactory(){
        SchedulerFactoryBean bean = new SchedulerFactoryBean ();
        bean.setDataSource(quartzDataSource);
        final org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:quartz.properties");  
        bean.setConfigLocation(resource);
        List<CronTriggerFactoryBean> cronTriggerFactoryBeans = new ArrayList<>();
        cronTriggerFactoryBeans.add(simpleCronTriggerBean());
        cronTriggerFactoryBeans.add(simpleTwoCronTriggerBean());
        List<Trigger> cronTriggers = cronTriggerFactoryBeans.stream().map(x -> x.getObject()).collect(Collectors.toList());
        bean.setTriggers(cronTriggers.toArray(new Trigger[cronTriggers.size()]));
        return bean;
    }
}
