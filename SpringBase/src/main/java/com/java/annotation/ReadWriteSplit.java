package com.java.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.java.db.routing.DbContextHolder;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadWriteSplit {

	DbContextHolder.DbType name();
}
