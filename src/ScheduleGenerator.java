import javax.swing.JFrame;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JButton;
import java.awt.Font;


public class ScheduleGenerator {

	private JFrame frame;

	//WELCOME PANEL
	private JPanel welcomePanel;
	private JTextField textFieldUserInput;
	private JLabel lblEnterCourse;
	private JLabel headingLabel1;
	private JLabel headingLabel2;
	private JButton goButton;
	private JButton doneButton;
	private ArrayList<Course> courses;					//all the courses from the database
	private InstantiateCourseObjects fillArray;
	private ArrayList<String> selectedCourseCodes;

	//LIST PANEL
	private JPanel listPanel;
	private JTable coursesTable;
	private DefaultTableModel modelTable;
	private String [] tableHeadings = {"CRN", "Course Code", "Course Name", "Professor", "TimeSlot", "Credits"};
	private JLabel lblSelectedCourses;
	private JButton showScheduleButton;
	private CourseSchedule schedule;
	String semester = "Fall 2018";

	//CHART PANEL
	private JPanel chartPanel;
	private JTable chartTable;
	private DefaultTableModel chartTableModel; 
	private String [] chartTableHeadings = {"Time", "Tuesday", "Thursday"};
	private JButton backButton;
	private JButton restartButton;
	
	private ArrayList<Course> requestedCourses;
	private JTextArea notIncludedCoursesLabel;



	public ScheduleGenerator() {
		
		//Connect to the database and get all the courses
		databaseConnection();
		stepOne();
		
		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScheduleGenerator window = new ScheduleGenerator();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	
		});
	}

	public void stepOne() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		frame.setBounds(5, 5, 650, 400);
		frame.setTitle("Course Scheduler");

		frame.getContentPane().setLayout(new CardLayout(0, 0));		//set the Card Layout to the frame

		welcomePanel = new JPanel();
		frame.getContentPane().add(welcomePanel);
		welcomePanel.setLayout(null); 		//means absolute layout!


		listPanel = new JPanel();
		frame.getContentPane().add(listPanel);
		listPanel.setLayout(null);

		chartPanel = new JPanel();
		frame.getContentPane().add(chartPanel);
		chartPanel.setLayout(null);			//absolute layout 
		
		
		welcomePanelSetup();
		listPanelSetup();
		//chartPanelSetup();		//this line makes the table empty at runtime. But in order to format it, uncomment it to see the layout in Design view. 

	}

	public void welcomePanelSetup() {

		//Initialize
		lblEnterCourse = new JLabel("Course Code:");
		textFieldUserInput = new JTextField();
		goButton = new JButton("Go!");
		doneButton = new JButton("Done");
		headingLabel1 = new JLabel("Please enter the courses that you would like to");
		headingLabel2 = new JLabel("take this semester in order of importance");
		
		//Set font and size
		lblEnterCourse.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		goButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		doneButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));


		//Location
		lblEnterCourse.setBounds(92, 174, 117, 29);
		textFieldUserInput.setBounds(219, 170, 171, 42);
		goButton.setBounds(411, 165, 75, 47);
		doneButton.setBounds(276, 278, 85, 52);
		headingLabel1.setBounds(193, 72, 293, 29);
		headingLabel2.setBounds(202, 97, 268, 16);
		
		
		//Add elements to panel
		welcomePanel.add(lblEnterCourse);
		welcomePanel.add(textFieldUserInput);
		welcomePanel.add(goButton);
		welcomePanel.add(doneButton);
		welcomePanel.add(headingLabel1);
		welcomePanel.add(headingLabel2);
		
		
		//these are the courses that the user wishes to take
		selectedCourseCodes= new ArrayList<String>(); 
		
		
		//Makes Go and Done buttons responsive
		setWelcomePanelActionListener();
		
	}

	//welcomePanel GO button 
	private void setWelcomePanelActionListener() {

		//Listens out for an enter key on the textfield. 
		//Calls to add the inputted course to the arraylist 

		textFieldUserInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					theWelcomePanelAction();
					textFieldUserInput.setText("");		//to clear field after input 
				}
			}
		});

		//listens out for a click
		goButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				theWelcomePanelAction();
				textFieldUserInput.setText("");		//to clear field after input 
			}
		});

		//Switch panels
		doneButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(selectedCourseCodes.size()>0)
				{
					//move on to the next panel
					listPanelDisplay();
					listPanel.setVisible(true);
					welcomePanel.setVisible(false);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "No courses were added.");
				}
			}
		});


	}//end setActionListener


	private void databaseConnection() {
		try //create connection
		{
			//query
			ConnectDB connectDB = new ConnectDB("jdbc:sqlserver://localhost;databaseName=CourseSchedule;integratedSecurity=true");
			fillArray = new InstantiateCourseObjects(connectDB);
			courses = fillArray.getCourses();//courses in the database to compare to
		}

		//connection failed
		catch(SQLException e) {
			System.out.println(e);
		}

	}

	/**
	 * What happens once GO or the Enter key are pressed on Welcome Panel
	 * that course code is added to the arraylist 
	 */
	private void theWelcomePanelAction() {

		String selectedCode = textFieldUserInput.getText().toUpperCase();
		
		//formats the user input properly for the course objects
		int position=selectedCode.indexOf(" ");
		int position2 =selectedCode.indexOf("-");
		//space found
		if(position!=-1)
		{
			selectedCode=(selectedCode.substring(0, position)+"-"+selectedCode.substring(position+1));
		}
		//blank was entered//or non that fits the three formats
		else if(selectedCode.length()<5)
		{
			//just so that it does not enter the next if
		}
		//no dash and not space found
		else if(position2==-1)
		{
			selectedCode=(selectedCode.substring(0,4)+"-"+selectedCode.substring(4));
		}
		//dash found keep as is
		
			//check that this course code is valid
			ArrayList <Course> returnedCourses= SearchCourses.search(courses, selectedCode);
			if (returnedCourses.size()>0)
			{
				if(!selectedCourseCodes.contains(selectedCode))
				{
					requestedCourses.addAll(returnedCourses);
					selectedCourseCodes.add(selectedCode);
					JOptionPane.showMessageDialog(null, "Course added successfully.\n\nRequested Course Count: "+selectedCourseCodes.size());
				}
				else
				{
					JOptionPane.showMessageDialog(null, "This course was already added.");
				}
			}
			else {
				//could not find a match to the course code
				JOptionPane.showMessageDialog(null, "Invalid course code.");
			}
		
	}//end theWelcomePanelAction



	public void listPanelSetup() {

		//Initialize
		lblSelectedCourses = new JLabel("Available Courses");
		showScheduleButton = new JButton("Show Schedule");
		modelTable = new DefaultTableModel(tableHeadings, 0);	//to show outline of table but no information inside yet
		
		//Font and size
		lblSelectedCourses.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		showScheduleButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		
		//Location
		lblSelectedCourses.setBounds(226, 27, 207, 37);
		showScheduleButton.setBounds(241, 278, 147, 47);
		
		
		//listPanel.setLayout(null);
		
		//The table
		coursesTable = new JTable(modelTable);
		coursesTable.setEnabled(false);	//not to let users tamper with the output
		JScrollPane scrollPane = new JScrollPane(coursesTable);
		scrollPane.setBounds(25, 76, 600, 190);

		
		//Add elements to panel
		listPanel.add(showScheduleButton);
		listPanel.add(lblSelectedCourses);
		listPanel.add(scrollPane);		//displays the top header
		
		
		listPanelDisplay();
		listPanelActionListener();
		

	}

	private void listPanelDisplay() {
		
		//requestedCourses = ScheduleAlgorithm.getRequestedCourses(selectedCourseCodes, courses);
		if(requestedCourses==null)
		{	
			requestedCourses=new ArrayList<Course>();
		}
		for (int x = 0; x < requestedCourses.size(); x++) {
			Vector<Comparable> newRow = new Vector <Comparable>();
			newRow.add(requestedCourses.get(x).getCRN());
			newRow.add(requestedCourses.get(x).getCode());
			newRow.add(requestedCourses.get(x).getTitle());
			newRow.add(requestedCourses.get(x).getProfessor());
			newRow.add(requestedCourses.get(x).getTimeSlot());
			newRow.add(requestedCourses.get(x).getCredits());
			modelTable.addRow(newRow);	
		}		
		modelTable.fireTableDataChanged();

		//to resize the columns to fit the contents 
		for (int x = 0; x < tableHeadings.length; x++) 
			packColumn(coursesTable, x, 3);	
	}
	
	private void listPanelActionListener() {
		//Switch panels
		showScheduleButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//move on to the next panel
				chartPanelSetup();
				
				//Add action listener to button
				backButtonActionListener();
				
				chartPanel.setVisible(true);
				listPanel.setVisible(false);
			}
		});
	}
	
	
	public void chartPanelSetup() {
		//now get the CourseSchedule object schedule	
		schedule =  ScheduleAlgorithm.createSchedule(selectedCourseCodes, courses, semester);
		chartTableModel = new DefaultTableModel(chartTableHeadings, 0);
		chartTable = new JTable(chartTableModel);
		chartTable.setEnabled(false); 	//user cannot edit the table

		JScrollPane scrollpane = new JScrollPane(chartTable);
		backButton = new JButton("Back");
		JScrollPane scrollpane2 = new JScrollPane();
		restartButton = new JButton("Restart");
		
		
		backButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		restartButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		
		backButton.setBounds(25, 22, 89, 36);
		scrollpane.setBounds(99, 70, 442, 126);
		scrollpane2.setBounds(99, 2234, 447, 126);
		restartButton.setBounds(524, 22, 89, 36);

		
		//Add to panel
		chartPanel.add(scrollpane);
		chartPanel.add(backButton);
		notIncludedCoursesLabel = new JTextArea("");
		notIncludedCoursesLabel.setBounds(99, 223, 447, 126);
		chartPanel.add(notIncludedCoursesLabel);
		chartPanel.add(scrollpane2);
		chartPanel.add(restartButton);
		
		restartButtonActionListener();

		//The rows of the table 
		Vector <Comparable> newRow1 = new Vector <Comparable> ();	//	3-4
		Vector <Comparable> newRow2 = new Vector <Comparable> ();	//	4-5
		Vector <Comparable> newRow3 = new Vector <Comparable> ();	// 6-8
		Vector <Comparable> newRow4 = new Vector <Comparable> ();	// 8-10
		newRow1.add("3:10-4:15");
		newRow2.add("4:25-5:30");
		newRow3.add("6:00-8:15");
		newRow4.add("8:20-10:30");
		//timeslot 5 (3 - 4) 
		for (Course c : schedule.getCourses()) {
			if (c.getTimeSlot() == 5) {
				newRow1.add(c.getTitle());
				newRow1.add(c.getTitle());
				break;
			}
		}
		chartTableModel.addRow(newRow1);

		//timeslot 6 (4 - 5) 
		for (Course c : schedule.getCourses()) {
			if (c.getTimeSlot() == 6) {
				newRow2.add(c.getTitle());
				newRow2.add(c.getTitle());
				break;
			}
		}
		chartTableModel.addRow(newRow2);

		//timeslot 10 and 14 (6 - 8) 
		for (Course c : schedule.getCourses()) {
			if (c.getTimeSlot() == 10) {
				newRow3.add(c.getTitle());
				break;
			}
		}
		for (Course c : schedule.getCourses()) {
			if (c.getTimeSlot() == 14) {
				if(newRow3.size()!=2)
				{
					newRow3.add("");
				}
				newRow3.add(c.getTitle());
				break;
			}
		}
		chartTableModel.addRow(newRow3);

		//timeslot 11 and 16 (8 - 10) 
		for (Course c : schedule.getCourses()) {
			if (c.getTimeSlot() == 11) {
				newRow4.add(c.getTitle());
				break;
			}
		}
		for (Course c : schedule.getCourses()) {
			if (c.getTimeSlot() == 16) {
				if(newRow4.size()!=2)
				{
					newRow4.add("");
				}
				newRow4.add(c.getTitle());
				break;
			}
		}
		chartTableModel.addRow(newRow4);


		//to resize the columns to fit the contents 
		for (int x = 0; x < chartTableHeadings.length; x++) {
			packColumn(chartTable, x, 3);	//3 is the margin
		}
		
		if (selectedCourseCodes.size() > 0) {
			String unusedCourses = "";
			for (int s = 0; s < selectedCourseCodes.size(); s++)
				unusedCourses += selectedCourseCodes.get(s) + "  ";
			
			notIncludedCoursesLabel.setText("The following course(s) could not be added to your schedule\n due to a time-slot conflict with another course or an overflow of the credit limit:\n "
					+ "\t"+unusedCourses 
					+ "\nTo generate a different schedule, please RESTART. \n(It may help to enter the course in a different order to achieve\n your desired results)"
					);
		}
		else
		{
			notIncludedCoursesLabel.setText("Thank you for using our program!\n Your courses were successfully added to the schedule!");
		}
	}
	
	private void backButtonActionListener()
	{
		//Switch panels
		backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				listPanel.setVisible(true);
				chartPanel.setVisible(false);
			}
		});
	}//end backButtonActionListener
	
	private void restartButtonActionListener() {
		//Switch panels
		restartButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				frame.setVisible(false);
				
				//recalled the main
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							ScheduleGenerator window = new ScheduleGenerator();
							window.frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}





	//Credits to this method to Spot35 https://stackoverflow.com/questions/5820238/how-to-resize-jtable-column-to-string-length
	/**
	 * Sets the preferred width of the visible column specified by vColIndex. The column
	 * will be just wide enough to show the column head and the widest cell in the column.
	 * margin pixels are added to the left and right
	 * (resulting in an additional width of 2*margin pixels).
	 */ 
	private void packColumn(JTable table, int vColIndex, int margin) {
		DefaultTableColumnModel colModel = (DefaultTableColumnModel)table.getColumnModel();
		TableColumn col = colModel.getColumn(vColIndex);
		int width = 0;
	
		// Get width of column header
		TableCellRenderer renderer = col.getHeaderRenderer();
		if (renderer == null) {
			renderer = table.getTableHeader().getDefaultRenderer();
		}
		java.awt.Component comp = renderer.getTableCellRendererComponent(
				table, col.getHeaderValue(), false, false, 0, 0);
		width = comp.getPreferredSize().width;
	
		// Get maximum width of column data
		for (int r=0; r<table.getRowCount(); r++) {
			renderer = table.getCellRenderer(r, vColIndex);
			comp = renderer.getTableCellRendererComponent(
					table, table.getValueAt(r, vColIndex), false, false, r, vColIndex);
			width = Math.max(width, comp.getPreferredSize().width);
		}
	
		// Add margin
		width += 2 * margin;
	
		// Set the width
		col.setPreferredWidth(width);
	}
}