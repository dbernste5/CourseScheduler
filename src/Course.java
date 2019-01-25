


public class Course implements Comparable<Course>
{
	private Integer cRN;
	private String code;
	private String title;
	private String professor;
	private Integer timeSlot;
	private Integer credits;
	
	public Course(int cRN, String code, String title, String professor, Integer timeSlot, Integer credits)
	{
		if(code == null || title == null || professor == null || timeSlot == null || credits == null)
			throw new InvalidCourseException();
		
		this.cRN = cRN;
		this.code = code;
		this.title = title;
		this.professor = professor;
		this.timeSlot = timeSlot;
		this.credits = credits;
	}
	
	public Integer getCRN()
	{
		return this.cRN;
	}
	
	public String getCode()
	{
		return this.code;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String getProfessor()
	{
		return this.professor;
	}
	
	public Integer getTimeSlot()
	{
		return this.timeSlot;
	}
	
	public Integer getCredits()
	{
		return this.credits;
	}
	
	// equals method is based on CRN
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Course))
			return false;
		
		Course other = (Course) obj;
		if (cRN == null) 
		{
			if (other.cRN != null)
				return false;
		} 
		else if (!cRN.equals(other.cRN))
			return false;
		return true;
	}

	@Override
	public int compareTo(Course c)
	{
		if(getCode().equalsIgnoreCase(c.getCode()))
			return getTimeSlot().compareTo(c.getTimeSlot());
		return getCode().compareToIgnoreCase(c.getCode());
	}
	
	@Override
	public String toString()
	{
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("Time Slot: "+timeSlot+
				"\nCRN: " + getCRN() 
					+ "\nCode: " + getCode()
					+ "\nTitle: " + getTitle());
		return sbuild.toString();
	}
}