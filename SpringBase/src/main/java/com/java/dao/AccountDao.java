package com.java.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.java.annotation.ReadWriteSplit;
import com.java.db.routing.DbContextHolder;
import com.java.po.Account;

@Repository
public interface AccountDao {

	@ReadWriteSplit(name=DbContextHolder.DbType.readDataSource)
	@Results({
    @Result(property = "id", column = "id"),
    @Result(property = "name", column = "name"),
    @Result(property = "age", column = "age")
    })
	@Select("select id, name, age from ACCOUNT where name = #{name}")
	public Account selectByName(@Param("name")String name);
	
	@ReadWriteSplit(name=DbContextHolder.DbType.writeDataSource)
	@Insert("insert into ACCOUNT (name, age) values (#{name}, #{age})")
	public void saveToWrite(@Param("name")String name, @Param("age")Long age);
	
	@ReadWriteSplit(name=DbContextHolder.DbType.readDataSource)
	@Insert("insert into ACCOUNT (name, age) values (#{name}, #{age})")
	public void saveToRead(@Param("name")String name, @Param("age")Long age);
	
	@Results({
	    @Result(property = "id", column = "id"),
	    @Result(property = "name", column = "name"),
	    @Result(property = "age", column = "age")
	    })
	@Select("select id, name, age from ACCOUNT")
	public List<Account> findAll();
	
	@Results({
	    @Result(property = "id", column = "id"),
	    @Result(property = "name", column = "name"),
	    @Result(property = "age", column = "age")
	    })
	@Select("select id, name, age from ACCOUNT where id = #{id}")
	public Account findOne(@Param("id")Long id);
	
	@Update("update ACCOUNT set name = #{account.name}, age = #{account.age} where id = #{account.id}")
	public void update(@Param("account")Account account);
	
	@Delete("delete from ACCOUNT where id = #{id}")
	public void delete(@Param("id")Long id);
	
	@Insert("insert into ACCOUNT (name, age) values (#{name}, #{age})")
	public void save(@Param("name")String name, @Param("age")Long age);
}
