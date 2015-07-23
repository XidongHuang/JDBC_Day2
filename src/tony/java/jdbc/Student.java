package tony.java.jdbc;

public class Student {
	
	
	private int FlowId;
	private int Type;
	private String IDCard;
	private String ExamCard;
	private String StudentName;
	private String Location;
	private int Grade;
	public int getFlowId() {
		return FlowId;
	}
	public void setFlowId(int flowId) {
		FlowId = flowId;
	}
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	public String getIDCard() {
		return IDCard;
	}
	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}
	public String getExamCard() {
		return ExamCard;
	}
	public void setExamCard(String examCard) {
		ExamCard = examCard;
	}
	public String getStudentName() {
		return StudentName;
	}
	public void setStudentName(String studentName) {
		StudentName = studentName;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public int getGrade() {
		return Grade;
	}
	public void setGrade(int grade) {
		Grade = grade;
	}
	public Student(int flowId, int type, String iDCard, String examCard,
			String studentName, String location, int grade) {
		super();
		FlowId = flowId;
		Type = type;
		IDCard = iDCard;
		ExamCard = examCard;
		StudentName = studentName;
		Location = location;
		Grade = grade;
	}
	
	public Student(){
		
	}
	@Override
	public String toString() {
		return "Student [FlowId=" + FlowId + ", Type=" + Type + ", IDCard="
				+ IDCard + ", ExamCard=" + ExamCard + ", StudentName="
				+ StudentName + ", Location=" + Location + ", Grade=" + Grade
				+ "]";
	}
	
	
}
