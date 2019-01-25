

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class InstantiateCourseObjects {

	private ConnectDB connectDB;
	private ArrayList<Course> courses; 

	//constructor
	public InstantiateCourseObjects(ConnectDB connectDB) throws SQLException
	{
		this.connectDB=connectDB;
		courses= new ArrayList<Course>();
		Connection conn = connectDB.getConnection();

		//query the db

		Statement stmnt = conn.createStatement();
		String query = "SELECT * from CourseSchedule";
		ResultSet result = stmnt.executeQuery(query);
		
		int crn, timeSlot, credits;
		String code, title, professor;
		
		while ( result.next() ) {
			
			//get the info for the individual courses from each record
			timeSlot = result.getInt(1);
			crn = result.getInt(2);
			code = result.getString(3);
			
				//remove extra spaces from the code
				int position=code.indexOf(" ");
				if(position!=-1)
				{
					code=(code.substring(0, position)+"-"+code.substring(position+1));
				}
				else
				{
					code=(code.substring(0,4)+"-"+code.substring(5));
				}

			title = result.getString(4);
			professor = result.getString(5);
			credits = result.getInt(6);

			Course course = new Course(crn, code, title, professor, timeSlot, credits); 
			courses.add(course);
		}

		//close connections
		result.close(); 
		stmnt.close(); 
		conn.close();
	}
	
	
	//getter
	public ArrayList<Course> getCourses()
	{
		return courses;
	}

}











