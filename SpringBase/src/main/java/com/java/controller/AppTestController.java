package com.java.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.java.dao.AccountDao;
import com.java.po.Account;
import com.java.service.AccountService;

@Controller
public class AppTestController {
	
	private static final Logger logger = LoggerFactory.getLogger(AppTestController.class);
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private AccountService accountServiceImpl;
	
	private static final String DEFAULT_SPRING_SESSION_REDIS_PREFIX = "spring:session:";
	
	private String keyPrefix = DEFAULT_SPRING_SESSION_REDIS_PREFIX;
	
	String getSessionKey(String sessionId) {
		return this.keyPrefix + "sessions:" + sessionId;
	}
	
	@RequestMapping(value = "/loginPage")
	public String loginPage(){
		return "login";
	}

	@RequestMapping(value = "/home")
	public ModelAndView onlyForTest(HttpServletRequest req){
		/*
		HttpSession session = req.getSession();
		Object obj = session.getAttribute("SPRING_SECURITY_CONTEXT");
		Account a1 = new Account();
		a1.setName("a1");
		a1.setAge(10L);
		Account a2 = new Account();
		a2.setName("a2");
		a2.setAge(10L);
		try {
			accountServiceImpl.doAddReadAndWrite(a1, a2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		ModelAndView view = new ModelAndView("index");
		return view;
	}
	
	@RequestMapping(value = "/main")
	public ModelAndView mainPage(){
		ModelAndView view = new ModelAndView("template/main");
		return view;
	}
	
	@RequestMapping(value = "/updatePage")
	public ModelAndView updatePage(){
		ModelAndView view = new ModelAndView("template/edit");
		return view;
	}
	
	
	@RequestMapping(value = "/test123", method=RequestMethod.GET)
	@ResponseBody
	public List<Account> onlyForTest123(){
		Iterable<Account> all = accountDao.findAll();
		List<Account> allAccount = new ArrayList<>();
		all.forEach(allAccount::add);
		return allAccount;
	}
	
	@RequestMapping(value = "/test123/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Account getAccount(@PathVariable Long id){
		Account account = accountDao.findOne(id);
		return account;
	}
	
	@RequestMapping(value = "/test123/{id}", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> save(@RequestBody Account account){
//		list.add(account);
		accountDao.save(account.getName(), account.getAge());
		Map<String, String> map = new HashMap<>();
		map.put("success", "success");
		return map;
	}
	
	@RequestMapping(value = "/test123/{id}", method=RequestMethod.PUT)
	@ResponseBody
	public Account update(@PathVariable Long id, @RequestBody Account account){
//		list.add(account);
		accountDao.update(account);
		return account;
	}
	
	@RequestMapping(value = "/test123/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public Map<String, String> delete(@PathVariable Long id){
		accountDao.delete(id);
		Map<String, String> map = new HashMap<>();
		map.put("success", "success");
		return map;
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	        SecurityContextHolder.getContext().setAuthentication(null);
	    }
	    return "redirect:/loginPage";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
	}
}
