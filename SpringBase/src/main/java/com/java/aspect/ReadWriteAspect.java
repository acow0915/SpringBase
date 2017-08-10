package com.java.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.java.annotation.ReadWriteSplit;
import com.java.controller.AppTestController;
import com.java.db.routing.DbContextHolder;

@Component
@Aspect
public class ReadWriteAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(ReadWriteAspect.class);
	
	@Pointcut("execution(* com.java.dao.*Dao.*(..))")
    public void pointCutMethod() {
        // do nothing
    }
	
	@Before("execution(* com.java.dao.*Dao.*(..))")
	public void before(JoinPoint joinPoint){
		String name = joinPoint.getSignature().getName();
//		System.out.println("----------before-----------------");
//		System.out.println("name = " + name);
	}
	
	
	@After("execution(* com.java.dao.*Dao.*(..))")
	public void after(JoinPoint joinPoint){
		String name = joinPoint.getSignature().getName();
//		System.out.println("----------after-----------------");
//		System.out.println("name = " + name);
	}

	@Around("execution(* com.java.dao.*Dao.*(..))")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
        	MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
    		Method method = signature.getMethod();
    		logger.debug("name = " + method.getName());
    		ReadWriteSplit annon = method.getAnnotation(ReadWriteSplit.class);
    		
    		if(null == annon){
    			DbContextHolder.setDbType(DbContextHolder.DbType.readDataSource);
    		} else {
    			DbContextHolder.setDbType(annon.name());
    		}
    		
            Object result = proceedingJoinPoint.proceed();
            return result;
        } finally {
            DbContextHolder.clearDbType();
        }
    }
}
