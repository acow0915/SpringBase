package com.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.dao.AccountDao;
import com.java.po.Account;

@Service
public class AccountServiceImpl implements AccountService{

	@Autowired
	private AccountDao accountDao;
	
	@Transactional
	public void doAddReadAndWrite(Account a1, Account a2) throws Exception{
		accountDao.saveToRead(a1.getName(), a1.getAge());
		accountDao.saveToRead(a2.getName(), a2.getAge());
		accountDao.saveToWrite(a1.getName(), a1.getAge());
		accountDao.saveToWrite(a2.getName(), a2.getAge());
		throw new Exception("test");
	}
}
