package com.java.db.routing;

public class DbContextHolder {
	public enum DbType {
		readDataSource,
		writeDataSource
    }

    private static final ThreadLocal<DbType> contextHolder = new ThreadLocal<>();

    public static void setDbType(DbType dbType) {
        if(dbType == null){
            throw new NullPointerException();
        }
        contextHolder.set(dbType);
    }

    public static DbType getDbType() {
        return contextHolder.get() == null ? DbType.readDataSource : contextHolder.get();
    }

    public static void clearDbType() {
        contextHolder.remove();
    }
}
