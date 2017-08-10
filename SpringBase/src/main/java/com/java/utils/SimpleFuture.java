package com.java.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class SimpleFuture<T> implements Future<T>{
	
	private CompletableFuture<T> cf;
	
	public SimpleFuture(){}
	
	public SimpleFuture(CompletableFuture<T> cf){
		super();
		Objects.requireNonNull(cf, "cf");
		this.cf = cf;
	}
	
	public static <T> SimpleFuture<Void> addAll(List<SimpleFuture<T>> futures) {
		List<CompletableFuture<T>> cfutures = futures.stream().map(f -> f.cf).collect(Collectors.toList());
		return new SimpleFuture<>(CompletableFuture.allOf(cfutures.toArray( new CompletableFuture[cfutures.size()]) ));
	}
	
	public static SimpleFuture<Void> run(Runnable runnable){
		return new SimpleFuture<>(CompletableFuture.runAsync(runnable));
	}
	
	public static SimpleFuture<Void> run(Runnable runnable,
            Executor executor) {
		return new SimpleFuture<>(CompletableFuture.runAsync(runnable, executor));
	}
	
	public T join() {
		return cf.join();
	}
	
	
	public static void main(String[] args){
		List<SimpleFuture<Void>> futures = new ArrayList<>();
		SimpleFuture<Void> f1 = SimpleFuture.run(() -> {
			Thread t1 = Thread.currentThread();
			try {
				t1.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("---------------1------------");
		});
		
		SimpleFuture<Void> f2 = SimpleFuture.run(() -> {
			Thread t1 = Thread.currentThread();
			try {
				t1.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("---------------2------------");
		});
		
		futures.add(f1);
		futures.add(f2);
		
		try {
			SimpleFuture.addAll(futures).join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return cf.cancel(mayInterruptIfRunning);
	}


	@Override
	public boolean isCancelled() {
		return cf.isCancelled();
	}


	@Override
	public boolean isDone() {
		return cf.isDone();
	}


	@Override
	public T get() throws InterruptedException, ExecutionException {
		return cf.get();
	}


	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return cf.get(timeout, unit);
	}
}
