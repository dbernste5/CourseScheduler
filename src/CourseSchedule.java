import java.util.ArrayList;

public class CourseSchedule {

	//instance variables
	private String semester;
	private int maxCredits;
	private int totalCredits=0;
	private ArrayList<Course> courses;
	
	//constructor
	public CourseSchedule(int maxCredits, String semester) 
	{
			courses = new ArrayList<Course>();
			this.maxCredits = maxCredits;
			this.semester = semester;
	}
	
	//method to add a course to the student's schedule
	public boolean addCourse(Course course)
	{
		if(approved(course))
		{
			courses.add(course);
			totalCredits+=course.getCredits(); //keep track of how many credits the student is accumulating for this semester
			return true;
		}
		
		return false; //course was not approved to be added to the schedule 
		//really it will never get to this since the methods will throw exceptions if the course didn't get approved for different reasons
	}
	
	
	private boolean approved(Course course) 
	{
		//check total credits
		if(!checkCredits(course))
		{
			throw new CreditOverflowException();
		}
		
		//check that time slots don't conflict
		if(!checkTimeslots(course))
		{
			throw new TimeslotConflictException();
		}
		
		return true; //no exceptions were thrown
		
	}

	private boolean checkTimeslots(Course course) {
		if(searchCourses(course.getTimeSlot())!=null) //course with this time slot WAS found 
		{
			return false; //the time slots conflict
			
		}
		
		return true;
	}

	private boolean checkCredits(Course course) {
		if(totalCredits+course.getCredits()>maxCredits)
		{
			//this course will cause the student to go over the credit limit
			return false;
		}
		
		return true;
	}
	
	
	private Course searchCourses(int timeslot) 
	{
		for(Course course: courses)
		{
			if(course.getTimeSlot()==timeslot)
			{
				return course;
			}
		}
		
		return null; //course was not found with this timeslot
	}
	
	//getters
	public String getSemester()
	{
		return semester;
	}
	
	public int getTotalCredits()
	{
		return totalCredits;
	}
	
	public int getMaxCredits()
	{
		return maxCredits;
	}
	
	public ArrayList<Course> getCourses()
	{
		//return a deep copy of the course list
		ArrayList<Course> courses2 = new ArrayList<Course>(courses.size());//make the new arrayList the same size as the real one
		for(Course course: courses)
		{
			courses2.add(course);
		}
		
		return courses2;
	}
	
	//to string
	@Override
	public String toString()
	{
		StringBuilder output = new StringBuilder();
		output.append(semester+":\n");
		for(Course course: courses)
		{
			output.append("\t"+course+"\n\n");
		}
		
		return output.toString();
	}

}
