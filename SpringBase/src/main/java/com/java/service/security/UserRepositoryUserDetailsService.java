package com.java.service.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

@Service
public class UserRepositoryUserDetailsService implements UserDetailsService{
	
	private static final String DEFAULT_SPRING_SESSION_REDIS_PREFIX = "spring:session:";
	
	private String keyPrefix = DEFAULT_SPRING_SESSION_REDIS_PREFIX;
	
	String getSessionKey(String sessionId) {
		return this.keyPrefix + "sessions:" + sessionId;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String id = RequestContextHolder.currentRequestAttributes().getSessionId();
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		ValueOperations<String, Object> operations = redisTemplate.opsForValue();
		Object obj = operations.get(getSessionKey(id));
		return null;
	}

}
