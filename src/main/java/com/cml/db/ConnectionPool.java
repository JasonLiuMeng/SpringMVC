package com.cml.db;

import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

public class ConnectionPool {
	
	private static Logger logger = Logger.getLogger(ConnectionPool.class);

	private Vector<Connection> pool;
	private static ConnectionPool instance;
	private int poolSize =  DBManager.getInstance().getPoolSize();
	
	public static ConnectionPool getInstance(){
		if( instance == null ){
			synchronized (ConnectionPool.class) {
				if( instance == null ){
					instance = new ConnectionPool();
				}
			}
		}
		return instance;
	}
	
	private ConnectionPool(){
		init();
	}
	
	private void init(){
		logger.info("init connection pool...");
		pool = new Vector<Connection>();
		initConnectionPool();
	}

	private void initConnectionPool() {
		// TODO Auto-generated method stub
		logger.info("init pool, size = " + poolSize);
		for(int i = 0; i < poolSize; i++){
			Connection conn = DBManager.getInstance().getConn();
			pool.add(conn);
		}
	}
	
	 //返回连接到连接池中
    public synchronized void release(Connection coon){
    	pool.add(coon);
    }
	
	public synchronized Connection getConn(){
		if( null == pool || pool.size() == 0 ){
			logger.info("no connection in connection pool...");
			return null;
		}
		Connection conn = pool.get(0);
		pool.remove(conn);
		return conn;
	}
	
	public synchronized void closePool(){
		logger.info("close connection pool...");
		for( Connection conn : pool ){
			try {
				if( !conn.isClosed() ){
					conn.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		pool.clear();
	}

}
