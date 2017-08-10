package com.java.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import com.java.db.routing.DbContextHolder;
import com.java.db.routing.ReadWriteSplitRoutingDataSource;

import redis.clients.jedis.JedisShardInfo;

@Configuration
@MapperScan(annotationClass=org.springframework.stereotype.Repository.class, 
basePackages={"com.java.dao"})
@PropertySource("classpath:application-dao.properties")
public class DaoConfig {

	@Value("${jdbc.driverClass.read}")
    private String driverClassNameRead;
	
	@Value("${url.read}")
    private String urlRead;
	
	@Value("${username.read}")
    private String usernameRead;
	
	@Value("${password.read}")
    private String passwordRead;
	
	@Value("${jdbc.driverClass.write}")
    private String driverClassNameWrite;
	
	@Value("${url.write}")
    private String urlWrite;
	
	@Value("${username.write}")
    private String usernameWrite;
	
	@Value("${password.write}")
    private String passwordWrite;
	
	@Value("${jdbc.driverClass.quartz}")
    private String driverClassNameQuartz;
	
	@Value("${url.quartz}")
    private String urlQuartz;
	
	@Value("${username.quartz}")
    private String usernameQuartz;
	
	@Value("${password.quartz}")
    private String passwordQuartz;
	
	@Bean(name="dynamicDataSource")
	@Primary
	public AbstractRoutingDataSource dynamicDataSource() {
		AbstractRoutingDataSource ds = new ReadWriteSplitRoutingDataSource();
//		ds.setDefaultTargetDataSource(createReadDataSource());
		Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
		targetDataSources.put(DbContextHolder.DbType.readDataSource, createReadDataSource());
		targetDataSources.put(DbContextHolder.DbType.writeDataSource, createWriteDataSource());
		ds.setTargetDataSources(targetDataSources);
		return ds;
	}
	
	@Bean(name="readDataSource")
	public DataSource createReadDataSource(){
		JdbcDataSource xaDatasource = new JdbcDataSource();
		xaDatasource.setUrl(urlRead);
		xaDatasource.setUser(usernameRead);
		xaDatasource.setPassword(passwordRead);
        
        JdbcTemplate jdbc = new JdbcTemplate(xaDatasource);
        jdbc.execute("create table ACCOUNT ( id INTEGER auto_increment PRIMARY KEY , name VARCHAR(30), age  INTEGER )");
        //ds.setPassword(password);
        AtomikosDataSourceBean atomikoDataSource = new AtomikosDataSourceBean();
        atomikoDataSource.setXaDataSource(xaDatasource);
        atomikoDataSource.setUniqueResourceName("readDataSource");
        
		return atomikoDataSource;
	}
	
	@Bean(name="writeDataSource")
	public DataSource createWriteDataSource(){
		JdbcDataSource xaDatasource = new JdbcDataSource();
		xaDatasource.setUrl(urlWrite);
		xaDatasource.setUser(usernameWrite);
		xaDatasource.setPassword(passwordWrite);
		
		final JdbcTemplate jdbc = new JdbcTemplate(xaDatasource);
        jdbc.execute("create table ACCOUNT ( id INTEGER auto_increment PRIMARY KEY , name VARCHAR(30), age  INTEGER )");
		
        AtomikosDataSourceBean atomikoDataSource = new AtomikosDataSourceBean();
        atomikoDataSource.setXaDataSource(xaDatasource);
        atomikoDataSource.setUniqueResourceName("writeDataSource");
        
        //ds.setPassword(password);
		return atomikoDataSource;
	}
	
	@Bean(name="quartzDataSource")
	public DataSource createQuartzDataSource(){
		
		
		DriverManagerDataSource datasource = new DriverManagerDataSource();
//		datasource.setUrl(urlQuartz);
//		datasource.setUser(usernameQuartz);
//		datasource.setPassword(passwordQuartz);
		datasource.setDriverClassName(driverClassNameQuartz);
		datasource.setUrl(urlQuartz);
		datasource.setUsername(usernameQuartz);
		datasource.setPassword(passwordQuartz);
		
		final JdbcTemplate jdbc = new JdbcTemplate(datasource);
        createQuartzTable().stream().forEach(s -> jdbc.execute(s));
		return datasource;
	}
	
	/*
	@Bean(name="readDataSource")
	public DataSource createReadDataSource(){
		
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(driverClassNameRead);
        ds.setUrl(urlRead);
        ds.setUsername(usernameRead);
        
        JdbcTemplate jdbc = new JdbcTemplate(ds);
        jdbc.execute("create table ACCOUNT ( id INTEGER auto_increment PRIMARY KEY , name VARCHAR(30), age  INTEGER )");
        //ds.setPassword(password);
        
		return ds;
	}
	
	@Bean(name="writeDataSource")
	public DataSource createWriteDataSource(){
		
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(driverClassNameWrite);
        ds.setUrl(urlWrite);
        ds.setUsername(usernameWrite);
        JdbcTemplate jdbc = new JdbcTemplate(ds);
        jdbc.execute("create table ACCOUNT ( id INTEGER auto_increment PRIMARY KEY , name VARCHAR(30), age  INTEGER )");
        //ds.setPassword(password);
		return ds;
	}
	*/
	
	//========================mybatis==========================
	//先注解掉  測試atomiko...
//	@Bean
//    public DataSourceTransactionManager transactionManager() {
//        return new DataSourceTransactionManager(dynamicDataSource());
//    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dynamicDataSource());
        //sessionFactory.setTypeAliasesPackage("org.lanyonm.playground.domain");
        return sessionFactory;
    }
    
    //========================redis==========================
    public RedisConnectionFactory lettuceConnectionFactory(){
    	JedisConnectionFactory factory = new JedisConnectionFactory();
    	factory.setHostName("localhost");
    	factory.setPort(6379);
    	JedisShardInfo shardInfo = new JedisShardInfo("localhost", 6379);
    	factory.setShardInfo(shardInfo);
    	return factory;
    	
//    	LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory("localhost", 6379);
//    	return lettuceConnectionFactory;
    }
    
    @Bean
    public RedisTemplate<Object, Object> redisTemplate() throws Exception {
    	RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
    	redisTemplate.setConnectionFactory(lettuceConnectionFactory());
    	StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    	redisTemplate.setKeySerializer(stringRedisSerializer);
    	redisTemplate.setValueSerializer(stringRedisSerializer);
    	return redisTemplate;
    }
    
    private List<String> createQuartzTable(){
    	List<String> createStrs = new ArrayList<String>();
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" CREATE TABLE QRTZ_CALENDARS ( ")
    			.append(" SCHED_NAME VARCHAR(120) NOT NULL, ")
    			.append(" CALENDAR_NAME VARCHAR (200)  NOT NULL , ")
    			.append(" CALENDAR IMAGE NOT NULL ) ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append(" CREATE TABLE QRTZ_CRON_TRIGGERS ( ")
    	.append(" SCHED_NAME VARCHAR(120) NOT NULL, ")
    	.append(" TRIGGER_NAME VARCHAR (200)  NOT NULL , ")
    	.append(" TRIGGER_GROUP VARCHAR (200)  NOT NULL , ")
    	.append(" CRON_EXPRESSION VARCHAR (120)  NOT NULL , ")
    	.append(" TIME_ZONE_ID VARCHAR (80)  ")
    	.append(" ) ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append(" CREATE TABLE QRTZ_FIRED_TRIGGERS ( ")
    	.append(" SCHED_NAME VARCHAR(120) NOT NULL, ")
    	.append(" SCHED_TIME BIGINT NULL , ")
    	.append(" ENTRY_ID VARCHAR (95)  NOT NULL , ")
    	.append(" TRIGGER_NAME VARCHAR (200)  NOT NULL , ")
    	.append(" TRIGGER_GROUP VARCHAR (200)  NOT NULL , ")
    	.append(" INSTANCE_NAME VARCHAR (200)  NOT NULL , ")
    	.append(" FIRED_TIME BIGINT NOT NULL , ")
    	.append(" PRIORITY INTEGER NOT NULL , ")
    	.append(" STATE VARCHAR (16)  NOT NULL, ")
    	.append(" JOB_NAME VARCHAR (200)  NULL , ")
    	.append(" JOB_GROUP VARCHAR (200)  NULL , ")
    	.append(" IS_NONCONCURRENT BOOLEAN  NULL , ")
    	.append(" REQUESTS_RECOVERY BOOLEAN  NULL  ")
    	.append(" ) ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append(" CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS ( ")
    	.append(" SCHED_NAME VARCHAR(120) NOT NULL, ")
    	.append(" TRIGGER_GROUP VARCHAR (200)  NOT NULL  ")
    	.append(" ) ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append(" CREATE TABLE QRTZ_SCHEDULER_STATE ( ")
    	.append(" SCHED_NAME VARCHAR(120) NOT NULL, ")
    	.append(" INSTANCE_NAME VARCHAR (200)  NOT NULL , ")
    	.append(" LAST_CHECKIN_TIME BIGINT NOT NULL , ")
    	.append(" CHECKIN_INTERVAL BIGINT NOT NULL ")
    	.append(" ) ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append(" CREATE TABLE QRTZ_LOCKS ( ")
    	.append(" SCHED_NAME VARCHAR(120) NOT NULL, ")
    	.append(" LOCK_NAME VARCHAR (40)  NOT NULL  ")
    	.append(" ) ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append(" CREATE TABLE QRTZ_JOB_DETAILS ( ")
    	.append(" SCHED_NAME VARCHAR(120) NOT NULL, ")
    	.append(" JOB_NAME VARCHAR (200)  NOT NULL , ")
    	.append(" JOB_GROUP VARCHAR (200)  NOT NULL , ")
    	.append(" DESCRIPTION VARCHAR (250) NULL , ")
    	.append(" JOB_CLASS_NAME VARCHAR (250)  NOT NULL , ")
    	.append(" IS_DURABLE BOOLEAN  NOT NULL , ")
    	.append(" IS_NONCONCURRENT BOOLEAN  NOT NULL , ")
    	.append(" IS_UPDATE_DATA BOOLEAN  NOT NULL , ")
    	.append(" REQUESTS_RECOVERY BOOLEAN  NOT NULL , ")
    	.append(" JOB_DATA IMAGE NULL ")
    	.append(" ) ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append(" CREATE TABLE QRTZ_SIMPLE_TRIGGERS ( ")
    	.append(" SCHED_NAME VARCHAR(120) NOT NULL, ")
    	.append(" TRIGGER_NAME VARCHAR (200)  NOT NULL , ")
    	.append(" TRIGGER_GROUP VARCHAR (200)  NOT NULL , ")
    	.append(" REPEAT_COUNT BIGINT NOT NULL , ")
    	.append(" REPEAT_INTERVAL BIGINT NOT NULL , ")
    	.append(" TIMES_TRIGGERED BIGINT NOT NULL ")
    	.append(" ) ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append(" CREATE TABLE qrtz_simprop_triggers ( ")
    	.append(" SCHED_NAME VARCHAR(120) NOT NULL, ")
    	.append(" TRIGGER_NAME VARCHAR(200) NOT NULL, ")
    	.append(" TRIGGER_GROUP VARCHAR(200) NOT NULL, ")
    	.append(" STR_PROP_1 VARCHAR(512) NULL, ")
    	.append(" STR_PROP_2 VARCHAR(512) NULL, ")
    	.append(" STR_PROP_3 VARCHAR(512) NULL, ")
    	.append(" INT_PROP_1 INTEGER NULL, ")
    	.append(" INT_PROP_2 INTEGER NULL, ")
    	.append(" LONG_PROP_1 BIGINT NULL, ")
    	.append(" LONG_PROP_2 BIGINT NULL, ")
    	.append(" DEC_PROP_1 NUMERIC(13,4) NULL, ")
    	.append(" DEC_PROP_2 NUMERIC(13,4) NULL, ")
    	.append(" BOOL_PROP_1 BOOLEAN NULL, ")
    	.append(" BOOL_PROP_2 BOOLEAN NULL, ")
    	.append(" ) ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append(" CREATE TABLE QRTZ_BLOB_TRIGGERS ( ")
    	.append(" SCHED_NAME VARCHAR(120) NOT NULL, ")
    	.append(" TRIGGER_NAME VARCHAR (200)  NOT NULL , ")
    	.append(" TRIGGER_GROUP VARCHAR (200)  NOT NULL , ")
    	.append(" BLOB_DATA IMAGE NULL ")
    	.append(" ) ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append(" CREATE TABLE QRTZ_TRIGGERS ( ")
    	.append(" SCHED_NAME VARCHAR(120) NOT NULL, ")
    	.append(" TRIGGER_NAME VARCHAR (200)  NOT NULL , ")
    	.append(" TRIGGER_GROUP VARCHAR (200)  NOT NULL , ")
    	.append(" JOB_NAME VARCHAR (200)  NOT NULL , ")
    	.append(" JOB_GROUP VARCHAR (200)  NOT NULL , ")
    	.append(" DESCRIPTION VARCHAR (250) NULL , ")
    	.append(" NEXT_FIRE_TIME BIGINT NULL , ")
    	.append(" PREV_FIRE_TIME BIGINT NULL , ")
    	.append(" PRIORITY INTEGER NULL , ")
    	.append(" TRIGGER_STATE VARCHAR (16)  NOT NULL , ")
    	.append(" TRIGGER_TYPE VARCHAR (8)  NOT NULL , ")
    	.append(" START_TIME BIGINT NOT NULL , ")
    	.append(" END_TIME BIGINT NULL , ")
    	.append(" CALENDAR_NAME VARCHAR (200)  NULL , ")
    	.append(" MISFIRE_INSTR SMALLINT NULL , ")
    	.append(" JOB_DATA IMAGE NULL ")
    	.append(" ) ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append(" ALTER TABLE QRTZ_CALENDARS  ADD ")
    	.append(" CONSTRAINT PK_QRTZ_CALENDARS PRIMARY KEY   ")
    	.append(" ( ")
    	.append(" SCHED_NAME, ")
    	.append(" CALENDAR_NAME ")
    	.append(" ) ");
    	createStrs.add(sb.toString());
    	/*
    	sb = new StringBuffer();
    	sb.append("  ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append("  ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append("  ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append("  ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append("  ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append("  ");
    	createStrs.add(sb.toString());
    	
    	sb = new StringBuffer();
    	sb.append("  ");
    	createStrs.add(sb.toString());
    	*/
    	return createStrs;
    }
}
