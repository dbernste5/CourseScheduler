import static org.junit.Assert.*;
import org.junit.*;

public class Course_TESTS 
{
	Course course;
	Course course2;
	
	@Before
	public void setup()
	{
		course = new Course(10617, "HISN 220", "Survey Mod History I", "Singer Toba", 1, 3);
	}
	
	// instantiation tests
	@Test(expected = InvalidCourseException.class)
	public void NullValuesForCourseInstantiationThrowsException()
	{
		course2 = new Course(10617, null, null, null, null, null);
	}
	
	// tests for the equals method
	@Test
	public void CourseWithSameValuesReturnsEqual()
	{
		course2 = new Course(10617, "HISN 220", "Survey Mod History I", "Singer Toba", 1, 3);
		assertTrue(course.equals(course2));
	}
	
	@Test
	public void CourseWithOnlySameCRNReturnsEqual()
	{
		course2 = new Course(10617, "CourseCode", "CourseTitle", "Professor", 0, 5);
		assertTrue(course.equals(course2));
	}
	
	@Test
	public void CourseWithDifferentCRNReturnsNotEqual()
	{
		course2 = new Course(10000, "HISN 220", "Survey Mod History I", "Singer Toba", 1, 3);
		assertFalse(course.equals(course2));
	}
	
	// tests for the compareTo method
	@Test
	public void CompareToReturnsZeroOnlyWithSameCodeAndSlot()
	{
		course2 = new Course(10000, "HISN 220", "CourseTitle", "Professor", 1, 5);
		assertEquals(0, course.compareTo(course2));
	}
	
	@Test
	public void CompareToReturnsNotZeroWhenOnlyTimeSlotIsDifferent()
	{
		course2 = new Course(10617, "HISN 220", "Survey Mod History I", "Singer Toba", 2, 3);
		assertEquals(-1, course.compareTo(course2));
	}
	
	@Test(expected = NullPointerException.class)
	public void CompareToThrowsExceptionWhenPassedNullValue()
	{
		course.compareTo(course2);
	}
}