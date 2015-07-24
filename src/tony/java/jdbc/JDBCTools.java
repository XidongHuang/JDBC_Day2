package tony.java.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTools {

	/**
	 * 执行SQL 语句，使用PreparedStatement
	 * 
	 * 
	 * @param sql
	 * @param args
	 */
	public static void update(String sql, Object ... args){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			
			
			for(int i = 0;i< args.length;i++ ){
				preparedStatement.setObject(i+1, args[i]);
				
			}
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			releaseDB(null, preparedStatement, connection);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * 执行sql的方法
	 * 
	 * 
	 * @param sql: 包括create, update, delete, 不包括select
	 */
	
	
	public static void update(String sql){
		Connection connection = null; 
		Statement statement = null;
		
		
		try{
			//1. 获取数据库连接
			connection = getConnection();
			
			
			//2. 调用 Connection 对象的 createStatement() 方法获取Statement 对象
			statement = connection.createStatement();
			
			
			
			//4. 发送 SQL 语句: 调用 Statement 对象的 executeUpdate(sql) 方法
			statement.executeUpdate(sql);
		
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			//5. 关闭数据库资源: 由里向外关
			releaseDB(null, statement, connection);
			
			
		}
		
		
	}
	
	
	
	
	/**
	 * 释放数据库资源的方法
	 * 
	 * @param rs
	 * @param statement
	 * @param conn
	 */
	
	public static void releaseDB(ResultSet rs, Statement statement, Connection connection){
		
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	
				
			
			
			
			
			
		}
		
	
	
	
	
	
	
	
	
	/**
	 * 获取数据库连接的方法
	 * 
	 * 
	 */

	public static Connection getConnection() throws IOException,
			ClassNotFoundException, SQLException {
		// 0. 读取 jdbc.properties
		/**
		 * 1) 属性文件对应Java 中的 properties 类 2) 可以使用类加载器加载bin 目录(类路径下)的文件
		 * 
		 */

		// 1. 准备获取连接的4个字符串： user, password,jdbcUrl, driver
		String user = null;
		String password = null;
		String jdbcUrl = null;
		String driverClass = null;

		Properties properties = new Properties();

		InputStream in = ReviewTest.class.getClassLoader().getResourceAsStream(
				"jdbc.properties");

		properties.load(in);

		user = properties.getProperty("user");
		password = properties.getProperty("password");
		jdbcUrl = properties.getProperty("jdbcUrl");
		driverClass = properties.getProperty("driver");

		// 2. 加载驱动
		Class.forName(driverClass);

		// 3. 调用
		// DriverManager.getConnection(jdbcUrl,user,password)
		// 获取数据库连接
		Connection connection = DriverManager.getConnection(jdbcUrl, user,
				password);
		return connection;
	}

}
