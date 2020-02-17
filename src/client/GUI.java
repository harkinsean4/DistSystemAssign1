  package client;

import client.ClientDAO;
import server.Assessment;
import server.CommsAssessment;
import server.DistAssessment;
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
	
	private ButtonModel submitAnswerMo;
	
	private ArrayList<String> assessmentNames;
	private ArrayList<Assessment> assessments;
	
	private int studentID;
	private String studentPassword;
	private Date due;
	
	private int token;
	
	private Font boldText = new Font("Arial", Font.BOLD, 18);
	private Font regText = new Font("Arial", Font.PLAIN, 18);
	
	//--------------------------Test Strings----------------------------------//
	private String[] questionsString = {"Can a duck swim", "How high can you jump", "Can a circle go in the square hole"};
	
	private String[] answers1 = {"Yes", "No", "Maybe"};
	private String[] answers2 = {"High", "Low", "Picolo"};
	
	private ClientDAO clientDAO;
	
	public GUI() {
		
		JFrame fr = new JFrame("RSI Assessment");
		loginPage(fr);
		
		
		clientDAO = new ClientDAO();
		
	}
	
	public void assessmentPage(JFrame frame) {
		
		frame.remove(loginPage);
		
		assessmentNames = new ArrayList<String>();
		
		for (int i = 0; i < assessments.size(); i++)
			assessmentNames.add(assessments.get(i).getInformation());
				
		information = new JPanel(new GridBagLayout());
		GridBagConstraints b = new GridBagConstraints();
		
		b.insets = new Insets(0,0,0,0);
		
		assessmentInfo = new JTextArea(assessments.get(0).getInformation());
		assessmentInfo.setFont(boldText);
		assessmentInfo.setEditable(false);
		assessmentInfo.setSelectionColor(null);
		b.gridx = 1;
		b.gridy = 1;
		information.add(assessmentInfo, b);
		
		studentNumber = new JLabel("ID: " + assessments.get(0).getAssociatedID());
		studentNumber.setFont(boldText);
		b.gridx = 1;
		b.gridy = 2;
		information.add(studentNumber, b);
		
		due = assessments.get(0).getClosingDate();
		
		dueDate = new JLabel(due.toString());
		dueDate.setFont(boldText);
		b.gridx = 2;
		b.gridy = 1;
		information.add(dueDate, b);
		
		east = new JPanel( new BorderLayout());
		east.add(information, BorderLayout.NORTH);
		
		questions = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(0,0,0,0);
		
		questionField = new JTextArea("The question will go here");
		questionField.setFont(boldText);
		questionField.setEditable(false);
		questionField.setSize(800, 30);
		c.gridx = 2;
		c.gridy = 1;
		//c.gridwidth = 4;
		questions.add(questionField, c);
		
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
		
		RadioButtonHandler rHandler = new RadioButtonHandler();
		option1.addItemListener(rHandler);
		option2.addItemListener(rHandler);
		option3.addItemListener(rHandler);
		
		
		
		option1Text = new JLabel("Option 1");
		option1Text.setFont(regText);
		c.gridx = 2;
		c.gridy = 2;
		questions.add(option1Text, c);
		
		option2Text = new JLabel("Option 2");
		option2Text.setFont(regText);
		c.gridx = 2;
		c.gridy = 3;
		questions.add(option2Text, c);
		
		option3Text = new JLabel("Option 3");
		option3Text.setFont(regText);
		c.gridx = 2;
		c.gridy = 4;
		questions.add(option3Text, c);
		
		next = new JButton("Next");
		next.setFont(boldText);
		c.gridx = 1;
		c.gridy = 5;
		//c.gridwidth = 3;
		questions.add(next, c);
		
		submitAnswer = new JButton("Submit");
		submitAnswer.setFont(boldText);
		c.gridx = 2;
		c.gridy = 5;
		//c.gridwidth = 3;
		questions.add(submitAnswer, c);
		
		east.add(questions, BorderLayout.CENTER);
		
		//assessmentPage.add(questions, BorderLayout.CENTER);
		
		sideView = new JPanel(new GridBagLayout());
		GridBagConstraints d = new GridBagConstraints();
		
		d.insets = new Insets(0,0,0,0);
		
		assignHeader = new JLabel("Assignments Available");
		assignHeader.setFont(boldText);
		d.gridx = 1;
		d.gridy = 1;
		sideView.add(assignHeader, d);
		
		availableAssessments = new JList(assessmentNames.toArray());
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
						int ptr;
						if(availableAssessments.getSelectedIndex() == 0 ) {	
							ptr = 0;
							assessmentInfo.setText(assessmentNames.get(ptr));
							
							due = assessments.get(ptr).getClosingDate();
							dueDate.setText(due.toString());
							questionField.setText(questionsString[availableAssessments.getSelectedIndex()]);
							option1Text.setText(answers1[availableAssessments.getSelectedIndex()]);
							option2Text.setText(answers1[availableAssessments.getSelectedIndex() + 1]);
							option3Text.setText(answers1[availableAssessments.getSelectedIndex() + 2]);
						}
						if(availableAssessments.getSelectedIndex() == 1 ) {
							ptr = 1;
							assessmentInfo.setText(assessmentNames.get(ptr));

							due = assessments.get(ptr).getClosingDate();
							dueDate.setText(due.toString());
							questionField.setText(questionsString[availableAssessments.getSelectedIndex()]);
							option1Text.setText(answers2[availableAssessments.getSelectedIndex()- 1 ]);
							option2Text.setText(answers2[availableAssessments.getSelectedIndex()    ]);
							option3Text.setText(answers2[availableAssessments.getSelectedIndex() + 1]);
						}
//						if(availableAssessments.getSelectedIndex() == 2 ) {
//							assessmentInfo.setText("This is Assignment 3: \nSpeech Processing for EE444");
//							dueDate.setText("22/03/2020 11:59.59");
//							questionField.setText(questionsString[availableAssessments.getSelectedIndex()]);
//							option1Text.setText(answers1[availableAssessments.getSelectedIndex() - 2]);
//							option2Text.setText(answers1[availableAssessments.getSelectedIndex() - 1]);
//							option3Text.setText(answers1[availableAssessments.getSelectedIndex()]);
//						}
					}
				}
		);
		
		west = new JPanel(new BorderLayout());
		west.add(sideView, BorderLayout.WEST);
		
		assessmentPage = new JPanel( new BorderLayout());
		
		assessmentPage.add(east, BorderLayout.EAST);
		assessmentPage.add(west, BorderLayout.WEST);
		//assessmentPage.add(east, BorderLayout.EAST);
		
		frame.getContentPane().add(assessmentPage);
		
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    frame.setSize(650, 400);
	    frame.setLocation(100, 100);
	    frame.setVisible(true);
		
	}
	
	public void loginPage(JFrame frame) {
//		frame = new JFrame("RSI Assessment");
		
		assessments = new ArrayList<Assessment>();
		
		loginPage = new JPanel(new GridBagLayout());
		GridBagConstraints a = new GridBagConstraints();
		
		a.insets = new Insets(0,0,0,0);
//		a.gridheight = 3;
//		a.gridwidth = 2;
		
		username = new JLabel ("Username: ");
		username.setFont(boldText);
		a.gridx = 1;
		a.gridy = 1;
		loginPage.add(username, a);
		
		userInput = new JTextField("Student Name");
		userInput.setFont(regText);
		userInput.setSize(200, 50);
	    a.gridx = 2;
	    a.gridy = 1;
	    loginPage.add(userInput, a);
	    
	    password = new JLabel ("Password: ");
		password.setFont(boldText);
		a.gridx = 1;
		a.gridy = 2;
		loginPage.add(password, a);
		
		passInput = new JPasswordField("122345678");
		passInput.setFont(regText);
		passInput.setSize(200, 50);
	    a.gridx = 2;
	    a.gridy = 2;
	    loginPage.add(passInput, a);
	    
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
					
					//assessments.add(clientDAO.getAssessment(token, studentID, "CT414"));
					// assessments.add(clientDAO.getAssessment(token, studentID, "EE444"));
					
					
					if(token != 0) {
						assessmentPage(frame);
					}
				}
				
			}
	    });
	    
	    
	    frame.getContentPane().add(loginPage);
	    
	    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize(400, 400);
        frame.setLocation(100, 100);
        frame.setVisible(true);
	}
	
	

	public static void main(String[] args) {
		GUI gui = new GUI();
	}
	
	private class RadioButtonHandler implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if( e.getSource() == option1 ) 
			{
//				option2.
			}
			if (e.getSource() == option2 )
			{
	  
			}
			if (e.getSource() == option3 ) 
			{
				
			}
			
		}
	}
}

