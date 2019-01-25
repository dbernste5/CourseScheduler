import java.util.ArrayList;
import java.util.Collections;

public class ScheduleAlgorithm {

	//requested coures arrayList
		//private static ArrayList<Course> requestedCourses = new ArrayList<Course>();
	// instantiated to avoid null getters
	private static ArrayList<String> reqCourses = new ArrayList<String>(); // changed requestedCourses to be accessible
	/*-match based on course code
     -put in all codes (optional times)
     - if times is selected or only one option grab that course
    - else
          -put in any that are the only one for a specific time slot
if still more to put in and all equally important
          -count how many each coursecode comes up
          -pick the one that comes up least 
	 */
	
	public  static CourseSchedule createSchedule(ArrayList<String> requestedCourses, ArrayList<Course> courses, String semester )
	{
		reqCourses = requestedCourses;
			
		ArrayList<ArrayList<Course>> allCourses = new ArrayList<ArrayList<Course>>();
		//this will hold the arrayLists of all
				//the arraylists of the courses that match the users requested courses
		
		//get an individual array of all the options available for each requested course code
		for(int i=0; i< reqCourses.size(); i++)
		{	
			reqCourses.set(i, reqCourses.get(i).toUpperCase());
			ArrayList<Course> courseOptions = SearchCourses.search(courses, reqCourses.get(i));
			allCourses.add(courseOptions);
		}
		
		//sort the array so we will have the list with the course thats offered the least times first
		//this is not efficient but it seems like the only way to accomplish what we need 
		//since otherwise (if we keep selecting the smallest size array) we will not know which courses were taken care of yet
		//unless we remove them but them the loop will be messed up.....
		Collections.sort(allCourses, new ArrayListSizeComparator());
		//actual schedule for the user
		CourseSchedule courseSchedule = new CourseSchedule(18, semester);
		
		ArrayList<Course> coursesToAdd;
		
		//boolean courseAdded;
		
		//we will go through and keep on trying to add the courses until the arrayList is empty
		for(int i =0; i< allCourses.size();i++) 
		{	
			coursesToAdd = allCourses.get(i); //get the first arraylist of the courses that were selected for this user to choose from
						
			//loop through the individual array to try to add the course to the schedule.
			//once its been added successfully, break out of loop
			for(int j = 0; j< coursesToAdd.size();j++)
			{	
				try {
					courseSchedule.addCourse(coursesToAdd.get(j));
					reqCourses.remove(coursesToAdd.get(j).getCode());
					break;
				}catch(TimeslotConflictException e)
				{
					//now we will do nothing, as the loop will continue
				}catch(CreditOverflowException e )
				{
					//nothin to do
				}
				
			}
	
		}
		return courseSchedule;
		
		
	}
	
}
