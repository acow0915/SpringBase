package com.java.config;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.java.service.security.CustomAuthenticationProvider;
import com.java.service.security.RESTAuthenticationEntryPoint;

@Configuration
@EnableRedisHttpSession
@EnableWebSecurity//创建一个Spring Bean，Bean的类型是Filter，名字为springSecurityFilterChain
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private AuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
	@Autowired
	private AuthenticationEntryPoint rESTAuthenticationEntryPoint;
	
	@Bean
	public AuthenticationProvider customAuthenticationProvider() {
		return new CustomAuthenticationProvider();
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		
        http
            .authorizeRequests()
            	.antMatchers("/", "/resources/**", "/h2-console/**").permitAll()
//                .antMatchers("/home").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic().authenticationEntryPoint(rESTAuthenticationEntryPoint)
                .and()
            .formLogin()
                .loginPage("/loginPage").permitAll()
//                .successHandler(customAuthenticationSuccessHandler)
                .successForwardUrl("/home")
                .failureUrl("/loginPage")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
            .logout()
                .permitAll()
                .and()
                .headers().frameOptions().disable();
        http.csrf().disable();
        http.sessionManagement().maximumSessions(10)//允许同时多少个用户同时登陆
        .and().sessionCreationPolicy(SessionCreationPolicy.NEVER);
//        http.sessionManagement();
        
        /*
        HttpSessionEventPublisher             监听session创建和销毁 
        ConcurrentSessionFilter                  每次有http请求时校验，看你当前的session是否是过期的 
        SessionRegistryImpl                       存放session中的信息，并做处理 
        ConcurrentSessionControllerImpl     用户登入登出的控制 
        SessionInformation       存储session中信息的model 
        */
    }
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("a").password("a").roles("USER");
        auth.authenticationProvider(customAuthenticationProvider());
    }
	
//	@Bean//redis
//    public HttpSessionStrategy httpSessionStrategy() {
//        return new HeaderHttpSessionStrategy();
//    }
}
