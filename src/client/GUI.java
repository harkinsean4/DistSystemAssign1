  package client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;



public class GUI extends JFrame implements Serializable {
	
	private JPanel 		information, 	questions, 		loginPage, 
						assessmentPage;
	
	private JLabel 		username, 		password;
	private JTextField 	userInput;
	private JPasswordField passInput;
	private JButton 	login;
	
	private JTextArea 	assessmentInfo, questionField;
    private JLabel 		dueDate, 		studentNumber, 	option1Text,
    					option2Text, 	option3Text;
	private JButton 	submitAnswer;
	private JRadioButton option1, 		option2, 		option3;
	
	private int studentID = 12345678;
	private String studentPassword;
	
	private Font boldText = new Font("Arial", Font.BOLD, 18);
	private Font regText = new Font("Arial", Font.PLAIN, 18);
	
	public GUI() {
		loginPage();
	}
	
	public void assessmentPage() {
		JFrame fr = new JFrame("RSI Assessment");
		
		information = new JPanel(new GridBagLayout());
		GridBagConstraints b = new GridBagConstraints();
		
		b.insets = new Insets(0,0,0,0);
		
		assessmentInfo = new JTextArea("This is where the assignment \ninformation will go ");
		assessmentInfo.setFont(boldText);
		assessmentInfo.setEditable(false);
		assessmentInfo.setSelectionColor(null);
		b.gridx = 1;
		b.gridy = 1;
		b.gridheight = 3;
		information.add(assessmentInfo, b);
		
		studentNumber = new JLabel("Student Number: " + studentID);
		studentNumber.setFont(boldText);
		b.gridx = 3;
		b.gridy = 2;
		information.add(studentNumber, b);
		
		dueDate = new JLabel("Due Date: 14/02/2020");
		dueDate.setFont(boldText);
		b.gridx = 2;
		b.gridy = 4;
		information.add(dueDate, b);
		
		assessmentPage = new JPanel( new BorderLayout());
		assessmentPage.add(information, BorderLayout.NORTH);
		
		questions = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(0,0,0,0);
		
		questionField = new JTextArea("The question will go here");
		questionField.setFont(boldText);
		questionField.setEditable(false);
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
		
		submitAnswer = new JButton("Submit");
		submitAnswer.setFont(boldText);
		c.gridx = 2;
		c.gridy = 5;
		//c.gridwidth = 3;
		questions.add(submitAnswer, c);
		
		assessmentPage.add(questions, BorderLayout.CENTER);
		
		fr.getContentPane().add(assessmentPage);
		
		fr.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    fr.setSize(800, 400);
	    fr.setLocation(100, 100);
	    fr.setVisible(true);
		
		
	}
	
	public void loginPage() {
		JFrame fr = new JFrame("RSI Assessment");
		
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
	    
	    fr.getContentPane().add(loginPage);
	    
	    fr.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        fr.setSize(600, 400);
        fr.setLocation(100, 100);
        fr.setVisible(true);
	}
	
	

	public static void main(String[] args) {
		GUI gui = new GUI();
	}
}

