package tony.java.jdbc;

import java.nio.channels.SelectableChannel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.EmptyStackException;
import java.util.Scanner;

import org.junit.Test;


public class JDBCTest {

	
	
	@Test
	public void testGetStudent(){
		//1. 得到查询的类型
		int searchType = getSearchTypeFromConsole();
		
		
		//2. 具体查询学生信息
		Student student = searchStudent(searchType);
		
		
		//3. 打印学生信息
		printStudent(student);
		
	}
	
	/**
	 * 打印学生信息: 若学生存在则打印其具体信息，若不存在：打印查无此人
	 * 
	 * @param student
	 */
	
	
	private void printStudent(Student student) {
			if(student != null){
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
		
		String sql = "Select FlowID, Type, IDCard, ExamCard," +
					 " StudentName, Location, Grade " +
					 "FROM yidi.examstudent "+
					 "where"
					 		;
		
		Scanner scanner = new Scanner(System.in);
		
		//1. 根据输入的searchType, 提示用户输入信息:
		//1.1 若searchType 为1， 提示: 输入身份证号。 若为2提示: 请输入准考证号
		if(searchType == 1){
			System.out.println("Please enter IDCard: ");
			String idCard = scanner.next();
			sql = sql + " IDCard = '"+ idCard + "'";
			System.out.println(sql);
		} else {
			System.out.println("Please enter ExamCard: ");
			String examCard = scanner.next();
			sql = sql + " ExamCard = '"+ examCard + "'";
			System.out.println(sql);
		}
		
		
		//2. 根据searchType 确定 SQL
		
		
		
		//3. 执行查询
		
		Student student = getStduent(sql);
		
		
		//4. 若存在查询结果，把查询结果封装为一个Student 对象
		
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

		try{
			
			connection = JDBCTools.getConnection();
			statement =  connection.createStatement();
			rs = statement.executeQuery(sql);
			
			if(rs.next()){
				stu = new Student(	rs.getInt(1),
									rs.getInt(2), 
									rs.getString(3), 
									rs.getString(4), 
									rs.getString("StudentName"),
									rs.getString("Location"), 
									rs.getInt("Grade"));
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
		
		if(type != 1 && type != 2){
			System.out.println("Enter wrong!");
			throw new RuntimeException();
			
			
			
		}
		
		
		return type;
	}







	@Test
	public void testAddNewStudent(){
		Student student = getStudentConsole();
		addNewStudent(student);
		
	}
	
	/**
	 * 从控制台输入学生的信息
	 *  
	 * 
	 * @return
	 */
	private Student getStudentConsole(){
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
	
	
	
	public void addNewStudent(Student student){
		
		
			//1.准备一条SQL 语句：
			 	String sql = "INSERT INTO yidi.examstudent VALUES("
			 				+ student.getFlowId() 		+ "," 
			 				+ student.getType()			+ ",'"
			 				+ student.getIDCard()		+ "','" 
			 				+ student.getExamCard()		+ "','" 
			 				+ student.getStudentName()	+ "','" 
			 				+ student.getLocation()		+ "',"
			 				+ student.getGrade()		+")";
			//2. 调用JDBCTools 类的update(sql) 方法执行插入操作
			 	JDBCTools.update(sql);
			 	
//			 	System.out.println(sql);
	
	
	
	}
	
	
}






/*
1. 向数据表中插入一条Student记录

在测试方法 testAddStudent() 中
//1. 获取从控制台输入的 student 对象： Student student = getStudentFromConsole();

//2. 调用addStudent(Student stu) 方法执行插入操作

1.1 新建一个方法: void addStudent(Student student)
把参数Student 对象插入到数据库中。

addStudent(Student student){
	//1.准备一条SQL 语句：
	 	String sql = "INSERT INTO examstudent " +
	 	"VALUES(" + student.getFlowId() +"','" + student.getType()+"','"+ student.getIdCard+"','"+student.getExamCard()+"','"+student.getStudentName+"','"+student.getLocation+"','"+student.getGrade()+)";
	//2. 调用JDBCTools 类的update(sql) 方法执行插入操作
	  

}


1.2 新建一个Student, 对应examstudents 数据表

class Student{

int flowId;
int type;
String idCard;
String examCard;
String studentName;
String location;
int grade;

}


*/