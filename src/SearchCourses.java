import java.util.ArrayList;

public class SearchCourses {
	
	public static ArrayList<Course> search(ArrayList<Course> courses, String courseCode)
	{
		ArrayList<Course> selectedCourses = new ArrayList<Course>();
		
		for(Course course : courses)
		{
			if(course.getCode().equalsIgnoreCase(courseCode))
			{
				if(course.getTimeSlot()==5||course.getTimeSlot()==6 || course.getTimeSlot()==10 ||
						course.getTimeSlot()==11 || course.getTimeSlot()==16 || course.getTimeSlot()== 14)
				{
					selectedCourses.add(course);
				}
			}
		}
		
		return selectedCourses;
	}

}
