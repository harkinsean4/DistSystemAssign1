  package client;

import client.ClientDAO;
import server.Assessment;
import server.CommsAssessment;
import server.DistAssessment;
import server.InvalidOptionNumber;
import server.InvalidQuestionNumber;
import server.Question;

import java.util.List;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.security.auth.spi.LoginModule;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



public class GUI extends JFrame implements Serializable {
	
	private JPanel 		information, 	questions, 		loginPage, 
						assessmentPage,	sideView, 		west,
						east;
	
	private JLabel 		username, 		password;
	private JTextField 	userInput;
	private JPasswordField passInput;
	private JButton 	login;
	private ButtonModel loginMo;
	
	private JTextArea 	assessmentInfo, questionField;
    private JLabel 		dueDate, 		studentNumber, 	option1Text,
    					option2Text, 	option3Text, 	assignHeader;
	private JButton 	submitAnswer, 	next;
	private JRadioButton option1, 		option2, 		option3;
	private ButtonGroup optionGroup;
	private JList 		availableAssessments;
	
	private ButtonModel submitAnswerMo, nextMo;
	private DefaultListModel model;
	
	private ArrayList<String> assessmentNames;
	private ArrayList<Assessment> assessments;
	
	private int studentID;
	private String studentPassword;
	private Date due;
	
	private int token;
	
	private Font boldText = new Font("Arial", Font.BOLD, 18);
	private Font regText = new Font("Arial", Font.PLAIN, 18);
	
	//--------------------------Test Strings----------------------------------//
	private List<Question> qs;
	private String[] as;
	private int selectedAns = -1;
	private int ptr;
	private int qPtr;
	
	private ClientDAO clientDAO;
	
	public GUI() {
		
		clientDAO = new ClientDAO();
		
		JFrame fr = new JFrame("RSI Assessment");
		loginPage(fr);
	}
	
	public void assessmentPage(JFrame frame) throws InvalidQuestionNumber {
		
		frame.remove(loginPage);
		
		
		//Populating names for JList
		assessmentNames = new ArrayList<String>();
		
		for (int i = 0; i < assessments.size(); i++)
			assessmentNames.add(assessments.get(i).getInformation());
		
		//Creating information panel holding assignment info

		information = new JPanel(new GridBagLayout());
		GridBagConstraints b = new GridBagConstraints();
		
		b.insets = new Insets(0,0,0,0);
		
		
		//Adding assessment info text area to panel
		assessmentInfo = new JTextArea(assessments.get(0).getInformation());
		assessmentInfo.setFont(boldText);
		assessmentInfo.setEditable(false);
		assessmentInfo.setSelectionColor(null);
		b.gridx = 1;
		b.gridy = 1;
		information.add(assessmentInfo, b);
		
		// Adding student bumer label
		studentNumber = new JLabel("ID: " + assessments.get(0).getAssociatedID());
		studentNumber.setFont(boldText);
		b.gridx = 1;
		b.gridy = 2;
		information.add(studentNumber, b);
		
		
		// Making local date object to initially populate date textfield
		due = assessments.get(0).getClosingDate();
		dueDate = new JLabel(due.toString());
		dueDate.setFont(boldText);
		b.gridx = 2;
		b.gridy = 1;
		information.add(dueDate, b);
		
		
		// Initialising panel to be placed at east of frame
		east = new JPanel( new BorderLayout());
		east.add(information, BorderLayout.NORTH);
		
		// Initialising Questions panel to handle buttons etc
		questions = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(0,0,0,0);
		
		// Initialising text area
		questionField = new JTextArea(assessments.get(0).getQuestion(0).getQuestionDetail());
		questionField.setFont(boldText);
		questionField.setEditable(false);
		questionField.setSize(800, 30);
		c.gridx = 2;
		c.gridy = 1;
		//c.gridwidth = 4;
		questions.add(questionField, c);
		
		
		// Initialising option radio buttons and adding them to logical button group
		option1 = new JRadioButton();
		c.gridx = 1;
		c.gridy = 2;
		questions.add(option1, c);
		
		option2 = new JRadioButton();
		c.gridx = 1;
		c.gridy = 3;
		questions.add(option2, c);
		
		option3 = new JRadioButton();
		c.gridx = 1;
		c.gridy = 4;
		questions.add(option3, c);
		
		optionGroup = new ButtonGroup();
		optionGroup.add(option1);
		optionGroup.add(option2);
		optionGroup.add(option3);
		
		// Creating Radio button handler for all radio buttons
		RadioButtonHandler rHandler = new RadioButtonHandler();
		option1.addItemListener(rHandler);
		option2.addItemListener(rHandler);
		option3.addItemListener(rHandler);
		
		// Creating string array to initially populate options, along with assigning values to text boxes
		String[] answers = assessments.get(0).getQuestion(0).getAnswerOptions();
		
		option1Text = new JLabel(answers[0]);
		option1Text.setFont(regText);
		c.gridx = 2;
		c.gridy = 2;
		questions.add(option1Text, c);
		
		option2Text = new JLabel(answers[1]);
		option2Text.setFont(regText);
		c.gridx = 2;
		c.gridy = 3;
		questions.add(option2Text, c);
		
		option3Text = new JLabel(answers[2]);
		option3Text.setFont(regText);
		c.gridx = 2;
		c.gridy = 4;
		questions.add(option3Text, c);
		
		// Creating next & submit buttons
		next = new JButton("Next");
		next.setFont(boldText);
		c.gridx = 1;
		c.gridy = 5;
		//c.gridwidth = 3;
		questions.add(next, c);
		
		submitAnswer = new JButton("Submit");
		submitAnswer.setFont(boldText);
		submitAnswer.setEnabled(false);
		c.gridx = 2;
		c.gridy = 5;
		//c.gridwidth = 3;
		questions.add(submitAnswer, c);
		
		
		// Adding models to buttons to allow functionality
		nextMo = next.getModel();
		submitAnswerMo = submitAnswer.getModel();
		
		nextMo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("GUI: Next was pressed");
				// If selected answer value is -1, next button does nothing. Otherwise, it takes the 
				// assigned value for selected answer and calls the clientDAO.selectAnswer() method.
				// This then increments the question pointer qPnt and calls the nextQuestion method.
				// If the pointer is the same value as the questions arraylist size, it enables the 
				// submit button.
				if(selectedAns != -1) {
					try {
						assessments.get(ptr).selectAnswer(qPtr, selectedAns);
						System.out.println("GUI: selected answers added");
					} catch (InvalidQuestionNumber | InvalidOptionNumber e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
					
					qPtr++;
					System.out.println("GUI: question pointer: " + qPtr);
					nextQuestion(qPtr);
					availableAssessments.setEnabled(false);
					optionGroup.clearSelection();
					if (qPtr == qs.size()-1) {
						next.setEnabled(false);
						submitAnswer.setEnabled(true);
						System.out.println("GUI: Last question reached");
					}
				}
			}
		});

		submitAnswerMo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// If pressed, the assessment is submitted using the clientDAO.submitAssessment method.
				// Following, it repopulates the JList housing assessment objects, resets all pointers
				// and resets GUI to initial login setup. If all assessments have been completed, the
				// GUI is cleared and all buttons are disabled
				
				System.out.println("GUI: Submit was pressed");
				clientDAO.submitAssessment(token, studentID, assessments.get(ptr));
				
				assessments.remove(ptr);
				model.clear();
				if(assessments.size()!= 0) {
					for (int i = 0; i < assessments.size(); i++) {
						assessmentNames.add(assessments.get(i).getInformation());
						model.add(i, assessmentNames.get(i));
					}
					ptr = 0;
					qPtr = 0;
					next.setEnabled(true);
					availableAssessments.setEnabled(true);
					submitAnswer.setEnabled(false);
					optionGroup.clearSelection();
					selectedAns = -1;
					nextQuestion(qPtr);
				}
				else {
					assessmentInfo.setText("");
					
					questionField.setText("Yay! Looks like you're on top of\nyour work!");
					option1Text.setText("");
					option2Text.setText("");
					option3Text.setText("");
					
					submitAnswer.setEnabled(false);
					option1.setEnabled(false);
					option2.setEnabled(false);
					option3.setEnabled(false);
				}
			}
			
		});
		east.add(questions, BorderLayout.CENTER);
		

		// Initialising sideview panel for JList
		sideView = new JPanel(new GridBagLayout());
		GridBagConstraints d = new GridBagConstraints();
		
		d.insets = new Insets(0,0,0,0);
		
		// Initialising head label
		assignHeader = new JLabel("Assignments Available");
		assignHeader.setFont(boldText);
		d.gridx = 1;
		d.gridy = 1;
		sideView.add(assignHeader, d);
		
		
		// Initialising list and model to edit list parmameters, and adding values
		availableAssessments = new JList();
		
		model = new DefaultListModel();
		availableAssessments.setModel(model);
		for(int i = 0; i < assessmentNames.size(); i++) 
		{
			model.add(i, assessmentNames.get(i));
		}
		availableAssessments.setVisibleRowCount(3);
		availableAssessments.setFont(regText);
		d.gridx = 1;
		d.gridy = 2;
		sideView.add(availableAssessments,d);
		
		availableAssessments.setSelectionMode ( ListSelectionModel.SINGLE_SELECTION);
		
		availableAssessments.addListSelectionListener(
		
				new ListSelectionListener() {
					@Override
					public void valueChanged( ListSelectionEvent event ) 
					{
						// If value changes, assessment pointer ptr is changed to current value selected
						// and date is updated
						ptr = availableAssessments.getSelectedIndex();
						dueDate.setText(due.toString());
						if(ptr != -1) {
							due = assessments.get(ptr).getClosingDate();
							qPtr = 0;
							System.out.println("GUI: Assessment Pointer: " + ptr);
							nextQuestion(qPtr);
						}
					}
				}
		);
		
		// Panel initialised to be placed at west of frame
		west = new JPanel(new BorderLayout());
		west.add(sideView, BorderLayout.WEST);
		
		
		// Main panel
		assessmentPage = new JPanel( new BorderLayout());
		
		assessmentPage.add(east, BorderLayout.EAST);
		assessmentPage.add(west, BorderLayout.WEST);
		//assessmentPage.add(east, BorderLayout.EAST);
		
		
		// Adding components to main frame
		frame.getContentPane().add(assessmentPage);
		
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    frame.setSize(650, 400);
	    frame.setLocation(100, 100);
	    frame.setVisible(true);
		
	}
	
	public void loginPage(JFrame frame) {

		// Initialising assessment arraylist
		assessments = new ArrayList<Assessment>();
		
		// Login Page panel
		loginPage = new JPanel(new GridBagLayout());
		GridBagConstraints a = new GridBagConstraints();
		
		a.insets = new Insets(0,0,0,0);
//		a.gridheight = 3;
//		a.gridwidth = 2;
		
		// Username JLabel
		username = new JLabel ("Username: ");
		username.setFont(boldText);
		a.gridx = 1;
		a.gridy = 1;
		loginPage.add(username, a);
		
		
		// Username input field
		userInput = new JTextField("16484724");
		userInput.setFont(regText);
		userInput.setSize(200, 50);
	    a.gridx = 2;
	    a.gridy = 1;
	    loginPage.add(userInput, a);
	    
	    // Password Label
	    password = new JLabel ("Password: ");
		password.setFont(boldText);
		a.gridx = 1;
		a.gridy = 2;
		loginPage.add(password, a);
		
		// Password field
		passInput = new JPasswordField("fergy");
		passInput.setFont(regText);
		passInput.setSize(200, 50);
	    a.gridx = 2;
	    a.gridy = 2;
	    loginPage.add(passInput, a);
	    
	    
	    // Login button
	    login = new JButton("Login");
	    login.setFont(boldText);
	    a.gridx = 1;
	    a.gridy = 3;
	    a.gridwidth = 2;
	    loginPage.add(login, a);
	    
	    loginMo = login.getModel();
	    
	    loginMo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Uses clientDAO.login() method to login to server, and retrieves login token. 
				// It finds all assessments available to the user, and adds them to a string list.
				// then, these items are added to the assessments array initialised earlier
				if(e.getSource() == loginMo) {
					//System.out.println("login pressed");
					
					studentID = Integer.parseInt(userInput.getText());
					token = clientDAO.login(studentID, passInput.getText());
					
					List<String> availableAssessmentStrings = clientDAO.getAvailableSummary(token, studentID);
					
					for(int i =0; i < availableAssessmentStrings.size(); i ++)
					{
						System.out.println("GUI: " + availableAssessmentStrings.get(i));
						Assessment ass = clientDAO.getAssessment(token, studentID, availableAssessmentStrings.get(i));
						if(ass != null) {
							assessments.add(ass);
						}
					}
					// If the token is not 0, the assessmentPage method is called					
					if(token != 0) {
						try {
							assessmentPage(frame);
						} catch (InvalidQuestionNumber e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							frame.remove(loginPage);
							frame.add(loginPage);
						}
					}
				}
				
			}
	    });
	    
	    
	    // Adding panel to frame
	    frame.getContentPane().add(loginPage);
	    
	    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize(400, 400);
        frame.setLocation(100, 100);
        frame.setVisible(true);
	}
	
	// Method to cycle through questions
	public void nextQuestion(int q) {
		qs = assessments.get(ptr).getQuestions();
		as = qs.get(q).getAnswerOptions();
		selectedAns = -1;
		
		assessmentInfo.setText(assessmentNames.get(ptr));
			
		questionField.setText(qs.get(q).getQuestionDetail());
		option1Text.setText(as[0]);
		option2Text.setText(as[1]);
		option3Text.setText(as[2]);
	}
	
	// Handler to change value for selected answer
	private class RadioButtonHandler implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if( e.getSource() == option1 ) 
			{
				selectedAns = 0;
				System.out.println("GUI: Selected answer: 0");
			}
			if (e.getSource() == option2 )
			{
				selectedAns = 1;
				System.out.println("GUI: Selected answer: 1");
			}
			if (e.getSource() == option3 ) 
			{
				selectedAns = 2;
				System.out.println("GUI: Selected answer: 2");
			}
		}
	}
	
	
	// Main
	public static void main(String[] args) {
		GUI gui = new GUI();
	}
}

