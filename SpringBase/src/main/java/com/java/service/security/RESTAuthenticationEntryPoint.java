package com.java.service.security;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.java.controller.AppTestController;

@Component
public class RESTAuthenticationEntryPoint implements AuthenticationEntryPoint{
	private static final Logger logger = LoggerFactory.getLogger(RESTAuthenticationEntryPoint.class);
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		String token = request.getHeader("x-auth-token");
		if(StringUtils.hasLength(token)){
			String userInfo = redisTemplate.opsForValue().get(token);
			String username = userInfo.split("||")[0];
			String password = userInfo.split("||")[1];
			
			request.setAttribute("username", username);
			request.setAttribute("password", password);
			
			StringBuffer sb = request.getRequestURL();
			String realPath = request.getRequestURI().replaceAll(request.getContextPath(), "");
			logger.debug(realPath);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher(realPath);
			dispatcher.forward(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
//		response.sendRedirect(request.getRequestURI());
//		redirectStrategy.sendRedirect(request, response, "/loginPage");
	}

}
