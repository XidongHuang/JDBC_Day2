package tony.java.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;



public class ReviewTest {
	
	/**
	 * 1. ResultSet 封装 JDBC 的查询结果
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * 
	 * 
	 */
	
	@Test
	public void testResultSet() throws ClassNotFoundException, IOException, SQLException{
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try {
			//1. 获取数据库连接
			connection = getConnection();
			
			//2. 调用 Connection 对象的 createStatement() 方法获取Statement 对象
			statement = connection.createStatement();
			
			//3. 准备sql 语句
			String sql = "select * from customer";
			
			
			//4. 发送 SQL 语句: 调用 Statement 对象的 executeQuery(sql) 方法
			//得到一个结果集对象 ResultSet
			rs = statement.executeQuery(sql);
			
			
			//5. 处理结果集：
			//5.1 调用ResultSet 的next() 方法: 查看结果集的下一条记录是否有效
			//若有效则下移指针
			while(rs.next()){
				//5.2 getXxx() 方法获取具体的列的值。
				int id = rs.getInt(1);
				String name = rs.getString(2);
				String email = rs.getString(3);
				Date birthday = rs.getDate("birthday");
				
				System.out.println(id);
				System.out.println(name);
				System.out.println(email);
				System.out.println(birthday);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			//6. 关闭数据库资源
			releaseDB(rs, statement, connection);
			
		}
		
		
		
		
		
		
		
		
		
		
	
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * 1. Statement 是用于操作sql 的对象
	 * 
	 */
	
	@Test
	public void testStatement(){
		Connection connection = null; 
		Statement statement = null;
		
		
		try{
			//1. 获取数据库连接
			connection = getConnection();
			
			
			//2. 调用 Connection 对象的 createStatement() 方法获取Statement 对象
			statement = connection.createStatement();
			
			
			
			//3. 准备sql 语句
			String sql = "UPDATE customer SET name = 'Tom' WHERE id = 3";
			
			
			
			
			//4. 发送 SQL 语句: 调用 Statement 对象的 executeUpdate(sql) 方法
			statement.executeUpdate(sql);
			
			
			
			
			
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			//5. 关闭数据库资源: 由里向外关
			releaseDB(null, statement, connection);
			
			
		}
		
		
		
		
	}
	
	public void releaseDB(ResultSet rs, Statement statement, Connection conn){
		
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
		
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	
				
			
			
			
			
			
		}
		
		
		

		
	
	
	
	
	
	
	
	
	/**
	 * Connection 代表应用程序的数据库的一个连接 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * 
	 * 
	 */
	
	
	
	@Test
	public void testGetConnection2() throws Exception{
		
		Connection connection = getConnection();
		
		System.out.println(connection);
		
	
		
	}









	public Connection getConnection() throws IOException,
			ClassNotFoundException, SQLException {
		//0. 读取 jdbc.properties
		/**
		 * 1) 属性文件对应Java 中的 properties 类
		 * 2) 可以使用类加载器加载bin 目录(类路径下)的文件
		 * 
		 */
		
		
		
		//1. 准备获取连接的4个字符串： user, password,jdbcUrl, driver
		String user = null;
		String password = null;
		String jdbcUrl = null;
		String driverClass = null;
		
		Properties properties = new Properties();
		
		InputStream in = 
				ReviewTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
		
		properties.load(in);
		
		user = properties.getProperty("user");
		password = properties.getProperty("password");
		jdbcUrl = properties.getProperty("jdbcUrl");
		driverClass = properties.getProperty("driver");
		
		
		//2. 加载驱动
		Class.forName(driverClass);
		
		
		
		
		//3. 调用
		//DriverManager.getConnection(jdbcUrl,user,password)
		//获取数据库连接
		Connection connection = DriverManager.getConnection(jdbcUrl,user,password);
		return connection;
	}
	 
}
