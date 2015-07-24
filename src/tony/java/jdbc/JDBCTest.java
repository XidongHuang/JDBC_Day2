package tony.java.jdbc;

import java.nio.channels.SelectableChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.junit.Test;



public class JDBCTest {

	@Test
	public void testResultSetMetaDate() {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT id,name,email,birthday"
					+ " FROM customer WHERE id = ?";

			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, 3);
			rs = preparedStatement.executeQuery();

			
			
			Map<String, Object> values = 
					new HashMap<String, Object>();
			
			
			
			//1. 得到 ResultSetMetaData 的对象
			ResultSetMetaData rsmd = rs.getMetaData();
			
			
			while(rs.next()){
				
				//2. 打印每一列的列明
				for(int i =0; i < rsmd.getColumnCount();i++){
					String columnLabel = rsmd.getColumnName(i+1);
					

					Object columnValue = rs.getObject(columnLabel);
					
					values.put(columnLabel, columnValue);
					
				}
				
				
			}
			
			System.out.println(values);
			
			Class clazz = Customer.class;
			
			Object object = clazz.newInstance();
			
			for(Map.Entry<String, Object> entry:values.entrySet()){
				String fieldName = entry.getKey();
				Object fieldValue = entry.getValue();
				
//				System.out.println(fieldName+": " + fieldValue);
				
				ReflectionUtils.setFieldValue(object, fieldName, fieldValue);
				
			}
			

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			JDBCTools.releaseDB(rs, preparedStatement, connection);

		}

	}

	@Test
	public void testGet() {

		String sql1 = "SELECT id,name,email,birthday"
				+ " FROM customer WHERE id = ?";
		System.out.println(sql1);
		Customer customer = get(Customer.class, sql1, 4);
		System.out.println(customer);

		String sql = "SELECT FlowId, Type, IDCard, ExamCard, StudentName,"
				+ "Location, Grade FROM examstudent WHERE flowid = ?";
		System.out.println(sql);
		Student stu = get(Student.class, sql, 14);
		System.out.println(stu);

	}

	public <T> T get(Class clazz, String sql, Object... args) {
		T entity = null;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			//1. 得到ResultSet 对象
			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);

			}

			rs = preparedStatement.executeQuery();
			
			
			//2. 得到ResultSetMetaData 对象
			ResultSetMetaData resultSetMetaData = rs.getMetaData();
			
			//3. 创建一个Map<String, Object> 对象，键:SQL 查询的列的别名。
			//值: 列的值
			Map<String, Object> values =
					new HashMap<String, Object>();
			
			
			//4. 处理结果集。利用 ResultSetMetaData 填充 3 对应的Map 对象
			while(rs.next()){
				for(int i = 0;i< resultSetMetaData.getColumnCount();i++){
					String columnLabel = resultSetMetaData.getColumnName(i+1);
					System.out.println("columnLabel " + columnLabel);
					Object columnValues = rs.getObject(columnLabel);
					
					
					values.put(columnLabel,columnValues);
					
				}
				
				
			}
			
			
			
			//5. 若Map不为空集，利用反射创建 clazz 对应的对象
//			if(values!=null){
//				
//				Object object  = clazz.newInstance();
//				
//				for(Map.Entry<String, Object> entry:values.entrySet()){
//					String fieldName = entry.getKey();
//					Object value = entry.getValue();
//					
//					ReflectionUtils.setFieldValue(object, fieldName, value);
//				}
//				
//			}
			
			
			
			
			//6。 遍历 Map对象，利用反射为 Class 对象的对应的属性赋值
			
			
			if (values.size()>0) {

				// 利用反射创建对象
				entity = (T) clazz.newInstance();

				// 通过解析SQL 语句来判断到底选择了哪些列，以及需要为 entity 对象
				// 的哪些属性赋值
				for(Map.Entry<String, Object> entry: values.entrySet()){
					String fieldName = entry.getKey();
					Object value = entry.getValue();
					ReflectionUtils.setFieldValue(entity, fieldName, value);
					
				}
				
				
				
				
				
				

			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			JDBCTools.releaseDB(rs, preparedStatement, connection);

		}

		return entity;
	}

	public Customer getCustomer(String sql, Object... args) {

		Customer cus = null;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);

			}

			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				cus = new Customer();
				cus.setId(rs.getInt(1));
				cus.setId(rs.getInt(2));
				cus.setEmail(rs.getString(3));
				cus.setBirthday(rs.getDate(4));

			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			JDBCTools.releaseDB(rs, preparedStatement, connection);

		}

		return cus;

	}

	public Student getStudent2(String sql, Object... args) {

		Student stu = null;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);

			}

			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				stu = new Student();
				stu.setFlowId(rs.getInt(1));
				stu.setType(rs.getInt(2));
				stu.setIDCard(rs.getString(3));
				stu.setExamCard(rs.getString(4));
				stu.setStudentName(rs.getString("StudentName"));
				stu.setLocation(rs.getString("Location"));
				stu.setGrade(rs.getInt("Grade"));
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			JDBCTools.releaseDB(rs, preparedStatement, connection);

		}

		return stu;

	}

	/**
	 * 使用 PreparedStatement 将有效的解决SQL 注入的问题
	 * 
	 */
	@Test
	public void testSQLinjection2() {

		String username = "a' OR password = ";
		String password = " OR '1' = '1";

		String sql = "SELECT * FROM SQLInjuction WHERE username =? "
				+ " AND password = ?";

		Connection connection = null;
		PreparedStatement preparedstatement = null;
		ResultSet rs = null;

		try {
			connection = JDBCTools.getConnection();
			preparedstatement = connection.prepareStatement(sql);

			preparedstatement.setString(1, username);
			preparedstatement.setString(2, password);

			rs = preparedstatement.executeQuery();

			if (rs.next()) {
				System.out.println("Log in successful");
			} else {
				System.out.println("Wrong username or password");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(rs, preparedstatement, connection);
		}

	}

	/**
	 * SQL注入
	 * 
	 * 
	 */

	@Test
	public void testSQLInjection() {

		String username = "a' OR password = ";
		String password = " OR '1' = '1";

		String sql = "SELECT * FROM SQLInjuction WHERE username = '"
				+ username + "' AND" + " password = '" + password + "'";

		System.out.println(sql);

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {
			connection = JDBCTools.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			if (rs.next()) {
				System.out.println("Log in successful");
			} else {
				System.out.println("Wrong username or password");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(rs, statement, connection);
		}

	}

	@Test
	public void testPreparedStatement() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JDBCTools.getConnection();
			String sql = "INSERT INTO customer (name,email,birthday)"
					+ "VALUES(?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, "Jerry");
			preparedStatement.setString(2, "6768asdf@go.com");
			preparedStatement.setDate(3,
					new java.sql.Date(new java.util.Date().getTime()));

			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, preparedStatement, connection);

		}

	}

	@Test
	public void testGetStudent() {
		// 1. 得到查询的类型
		int searchType = getSearchTypeFromConsole();

		// 2. 具体查询学生信息
		Student student = searchStudent(searchType);

		// 3. 打印学生信息
		printStudent(student);

	}

	/**
	 * 打印学生信息: 若学生存在则打印其具体信息，若不存在：打印查无此人
	 * 
	 * @param student
	 */

	private void printStudent(Student student) {
		if (student != null) {
			System.out.println(student);
		} else {
			System.out.println("No such person");
		}

	}

	/**
	 * 具体查询学生信息的。返回一个Student 对象，若不存在，则返回 null
	 * 
	 * @param searchType
	 * @return
	 */

	private Student searchStudent(int searchType) {

		String sql = "Select FlowID, Type, IDCard, ExamCard,"
				+ " StudentName, Location, Grade " + "FROM examstudent "
				+ "where";

		Scanner scanner = new Scanner(System.in);

		// 1. 根据输入的searchType, 提示用户输入信息:
		// 1.1 若searchType 为1， 提示: 输入身份证号。 若为2提示: 请输入准考证号
		// 2. 根据searchType 确定 SQL
		if (searchType == 1) {
			System.out.println("Please enter IDCard: ");
			String idCard = scanner.next();
			sql = sql + " IDCard = '" + idCard + "'";
			System.out.println(sql);
		} else {
			System.out.println("Please enter ExamCard: ");
			String examCard = scanner.next();
			sql = sql + " ExamCard = '" + examCard + "'";
			System.out.println(sql);
		}

		// 3. 执行查询

		Student student = getStduent(sql);

		// 4. 若存在查询结果，把查询结果封装为一个Student 对象

		return student;
	}

	/**
	 * 根据传入的SQL 返回 Student 对象
	 * 
	 * @param sql
	 * @return
	 */

	private Student getStduent(String sql) {

		Student stu = null;

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			connection = JDBCTools.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			if (rs.next()) {
				stu = new Student(rs.getInt(1), rs.getInt(2), rs.getString(3),
						rs.getString(4), rs.getString("StudentName"),
						rs.getString("Location"), rs.getInt("Grade"));
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			JDBCTools.releaseDB(rs, statement, connection);

		}

		return stu;
	}

	/**
	 * 从控制台读入一个整数，确定要查询的类型
	 * 
	 * 
	 * @return: 1. 用身份证查询 2. 用准考证号查询 其他的无效。并提示清用户重新输入。
	 */

	private int getSearchTypeFromConsole() {

		System.out.println("Please enter type:1. ID  2.ExamID ");

		Scanner scanner = new Scanner(System.in);
		int type = scanner.nextInt();

		if (type != 1 && type != 2) {
			System.out.println("Enter wrong!");
			throw new RuntimeException();

		}

		return type;
	}

	@Test
	public void testAddNewStudent() {
		Student student = getStudentConsole();
		// addNewStudent(student);
		addNewStudent2(student);

	}

	/**
	 * 从控制台输入学生的信息
	 * 
	 * 
	 * @return
	 */
	private Student getStudentConsole() {
		Scanner scanner = new Scanner(System.in);

		Student student = new Student();

		System.out.print("FlowID: ");
		student.setFlowId(scanner.nextInt());
		System.out.print("Type(4/6): ");
		student.setType(scanner.nextInt());
		System.out.print("IDCard: ");
		student.setIDCard(scanner.next());
		System.out.print("ExamCard: ");
		student.setExamCard(scanner.next());
		System.out.print("Student Name: ");
		student.setStudentName(scanner.next());
		System.out.print("Location: ");
		student.setLocation(scanner.next());
		System.out.println("Greade: ");
		student.setGrade(scanner.nextInt());

		return student;
	}

	public void addNewStudent2(Student student) {

		String sql = "INSERT INTO examstudent(FlowID,Type,IDCard,ExamCard,"
				+ "StudentName,Location,Grade) VALUES(?,?,?,?,?,?,?)";
		JDBCTools.update(sql, student.getFlowId(), student.getType(),
				student.getIDCard(), student.getExamCard(),
				student.getStudentName(), student.getLocation(),
				student.getGrade());

	}

	public void addNewStudent(Student student) {

		// 1.准备一条SQL 语句：
		String sql = "INSERT INTO examstudent VALUES(Flod"
				+ student.getFlowId() + "," + student.getType() + ",'"
				+ student.getIDCard() + "','" + student.getExamCard() + "','"
				+ student.getStudentName() + "','" + student.getLocation()
				+ "'," + student.getGrade() + ")";

		JDBCTools.update(sql);

		// Connection connection = null;
		// PreparedStatement preparedStatement= null;
		//
		//
		// try{
		// connection = JDBCTools.getConnection();
		//
		// String sql =
		// "INSERT INTO examstudent(FlowID,Type,IDCard,ExamCard,"
		// +"StudentName,Location,Grade) VALUES(?,?,?,?,?,?,?)";
		// preparedStatement = connection.prepareStatement(sql);
		//
		// preparedStatement.setInt(1, student.getFlowId());
		// preparedStatement.setInt(2, student.getType());
		// preparedStatement.setString(3, student.getIDCard());
		// preparedStatement.setString(4, student.getExamCard());
		// preparedStatement.setString(5, student.getStudentName());
		// preparedStatement.setString(6, student.getLocation());
		// preparedStatement.setInt(7, student.getGrade());
		//
		// preparedStatement.executeUpdate();
		//
		// } catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		//
		// } finally {
		// JDBCTools.releaseDB(null, preparedStatement, connection);
		// }
		// 2. 调用JDBCTools 类的update(sql) 方法执行插入操作

		// System.out.println(sql);

	}

}

/*
 * 
 * 
 * 3. ResultSetMetaData
 * 
 * 3.1 What:是描述ResultSet 的元数据对象。即从中可以获取到结果集中有多少列，列名是什么...
 * 
 * 3.2 How: a) 得到ResultSetMetaData 对象：调用ResultSet 的getMetaDate()方法
 * 
 * > int getColumnCount(): SQL 语句中包含哪些列
 * 
 * > String getColumnLabel(int column): 获取指定的列的别名，其中索引从 1 开始
 * 
 * 
 * 
 * 
 * 2. 使用 PreparedStatement
 * 
 * 2.1 why? 使用Statement 需要进行拼写SQL语句，很辛苦，而且容易出错
 * 
 * 
 * >> 有效禁止SQL注入。
 * 
 * String username = "a' OR password = "; String password = " OR '1' = '1";
 * 
 * String sql = "SELECT * FROM SQLInjuction WHERE username = '" + username
 * + "' AND" + " password = '" + password + "'";
 * 
 * 
 * >>>
 * 
 * PreparedStatement： 是Statement 的子接口，可以传入带占位符的SQL语句。 而且提供了补充占位符变量的方法
 * 
 * 2.2 使用PreparedStatement
 * 
 * a) 创建PreparedStatement:
 * 
 * String sql = "INSERT INTO examstudent VALUES(?,?,?,?,?,?,?)"
 * 
 * PreparedStatement ps = conn.prepareStatement(sql);
 * 
 * b) 调用PreparedStatement 的setXxx(int index, Object val) 设置占位符的值 index 值从 1 开始
 * 
 * c) 执行SQL语句: executeQuery() 或 executeUpdate(). 注意：执行时不再需要传入SQL 语句。
 * 
 * 
 * 
 * 
 * 
 * 1. 向数据表中插入一条Student记录
 * 
 * 在测试方法 testAddStudent() 中
 * 
 * //1. 获取从控制台输入的 student 对象： Student student = getStudentFromConsole();
 * 
 * //2. 调用addStudent(Student stu) 方法执行插入操作
 * 
 * 1.1 新建一个方法: void addStudent(Student student) 把参数Student 对象插入到数据库中。
 * 
 * addStudent(Student student){ //1.准备一条SQL 语句： String sql =
 * "INSERT INTO examstudent " + "VALUES(" + student.getFlowId() +"','" +
 * student.getType()+"','"+
 * student.getIdCard+"','"+student.getExamCard()+"','"+student
 * .getStudentName+"','"+student.getLocation+"','"+student.getGrade()+)"; //2.
 * 调用JDBCTools 类的update(sql) 方法执行插入操作
 * 
 * 
 * }
 * 
 * 
 * 1.2 新建一个Student, 对应examstudents 数据表
 * 
 * class Student{
 * 
 * int flowId; int type; String idCard; String examCard; String studentName;
 * String location; int grade;
 * 
 * }
 */