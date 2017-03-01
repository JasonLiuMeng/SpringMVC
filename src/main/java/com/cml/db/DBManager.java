package com.cml.db;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.cml.bean.Person;
import com.cml.common.Common;

public class DBManager {
	
	private static Logger logger = Logger.getLogger(DBManager.class);
	
	private String dbDirver = "";
	private String dbProtocol = "";
	private String dbName = "";
	private int poolSize = 1;
	private static DBManager dbManager = null;
	
	private String driver = "com.mysql.jdbc.Driver";
	private String username = System.getenv("ACCESSKEY");
	private String password = System.getenv("SECRETKEY");
	//System.getenv("MYSQL_HOST_S"); 为从库，只读
	private String dbUrl = String.format("jdbc:mysql://%s:%s/%s", System.getenv("MYSQL_HOST"), System.getenv("MYSQL_PORT"), System.getenv("MYSQL_DB"));
	 
	public Connection getConn(){
		try {
//			Class.forName(this.dbDirver);
//			connection = DriverManager.getConnection(this.dbProtocol + Common.getResourcePath(this.dbName) + ";create=true");
			Class.forName(this.driver);
			connection = DriverManager.getConnection(dbUrl, username, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	
	private DBManager(){
		initDBConfig();
	}
	
	private void initDBConfig() {
		// TODO Auto-generated method stub
		String path = Common.getResourcePath("dbpool.properties");
		try {
			FileInputStream in = new FileInputStream(path);
			Properties props = new Properties();
			props.load(in);
			this.dbDirver = props.getProperty("db.driver");
			this.dbProtocol = props.getProperty("db.protocol");
			this.dbName = props.getProperty("db.dbname");
			this.poolSize = Integer.parseInt(props.getProperty("db.poolSize"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static DBManager getInstance(){
		if(dbManager == null){
			synchronized (DBManager.class) {
				if( dbManager == null ){
					dbManager = new DBManager();
				}
			}
		}
		return dbManager;
	}
	
	private Connection connection;
	
	public int getPoolSize() {
		return poolSize;
	}
	
	
	public void executeSQL(String sql) throws Exception{
		ConnectionPool pool = null;
		pool = ConnectionPool.getInstance();
		Connection conn = pool.getConn();
		logger.info("execute sql : " + sql);
		PreparedStatement p = conn.prepareStatement(sql);
		p.executeUpdate();
		p.close();
		pool.release(conn);
	}
	
	public void executeUpdateOrInsert(String sql, Person person) throws Exception{
		ConnectionPool pool = null;
		pool = ConnectionPool.getInstance();
		Connection conn = pool.getConn();
		PreparedStatement p = conn.prepareStatement(sql);
		p.setString(1, person.getP_name());
		p.setString(2, person.getP_dining_m());
		p.setString(3, person.getP_dining_a());
		p.setDate(4, new Date(person.getP_time().getTime()));
		logger.info("execute sql : " + sql);
		p.executeUpdate();
		p.close();
		pool.release(conn);
	}

	
	public List<Person> executeQuery(String sql) throws Exception{
		List<Person> list = new ArrayList<Person>();
		ConnectionPool pool = null;
		pool = ConnectionPool.getInstance();
		Connection conn = pool.getConn();
		logger.info("execute sql : " + sql);
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(sql);
		while( rs.next() ){
			Person p = new Person(rs.getInt("id"), rs.getString("p_name"), rs.getString("p_dining_m"), rs.getString("p_dining_a"), rs.getDate("p_date"));
			list.add(p);
		}
		s.close();
		pool.release(conn);
		return list;
	}
	
	public Map<String, Integer> executeQueryNumber(String sql) throws Exception{
		Map<String, Integer> map = new HashMap<String, Integer>();
		ConnectionPool pool = null;
		pool = ConnectionPool.getInstance();
		Connection conn = pool.getConn();
		logger.info("execute sql : " + sql);
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(sql);
		String date = "";
		while( rs.next() ){
			date = Common.dateToString(Common.YYYYMMDD, rs.getDate("p_date"));
			map.put(date, rs.getInt(2));
		}
		s.close();
		pool.release(conn);
		return map;
	}
	
	public List<String> executeQueryCol(String sql) throws Exception{
		List<String> list = new ArrayList<String>();
		ConnectionPool pool = null;
		pool = ConnectionPool.getInstance();
		Connection conn = pool.getConn();
		logger.info("execute sql : " + sql);
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(sql);
		String date = "";
		while( rs.next() ){
			list.add(rs.getString(1));
		}
		s.close();
		pool.release(conn);
		return list;
	}
	
	public boolean checkTable(String tableName) throws Exception{
		ConnectionPool pool = null;
		pool = ConnectionPool.getInstance();
		Connection conn = pool.getConn();
		DatabaseMetaData meta = conn.getMetaData();  
        ResultSet res = meta.getTables(null, null, null, new String[]{"TABLE"});  
        HashSet<String> set=new HashSet<String>();  
        while (res.next()) {  
            set.add(res.getString("TABLE_NAME"));  
        }  
        logger.info(set);
        return set.contains(tableName.toUpperCase());
	}
	
	public static String CREATE_TABLE_SQL = "create table dining_table(id int not null generated always as identity (START WITH 1, INCREMENT BY 1),p_name varchar(50),p_dining_m varchar(1), p_dining_a varchar(1), p_date date, constraint pk_id primary key ( id)  )";
	public static String INSERT_TABLE_SQL = "insert into dining_table(p_name,p_dining_m,p_dining_a,p_date) values(?,?,?,?)";

}
