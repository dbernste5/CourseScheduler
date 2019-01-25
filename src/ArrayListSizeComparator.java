import java.util.Comparator;
import java.util.ArrayList;

public class ArrayListSizeComparator implements Comparator<ArrayList<Course>>{

	public int compare(ArrayList<Course> o1, ArrayList<Course> o2)
	{
		if(o1.size()> o2.size())
		{
			return 1;
		}
		else if(o1.size()< o2.size())
		{
			return -1;
		}
		else {
			return 0;
		}
			
	}
}
