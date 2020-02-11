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
import javax.swing.JTextField;



public class GUI extends JFrame implements Serializable {
	
	private JLabel username, password;
	private JTextField userInput;
	private JPasswordField passInput;
	private JButton login;
	
	private JTextField assessmentInfo, studentNumber, questionField;
    private JLabel dueDate;
	private JButton submitAnswer;
	private JRadioButton option1, option2, option3;
	private JPanel information, assessment, loginPage;
	
	private int studentID;
	private String studentPassword;
	
	private Font boldText = new Font("Arial", Font.BOLD, 18);
	private Font regText = new Font("Arial", Font.PLAIN, 18);
	
	public GUI() {
		loginPage();
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
		
		userInput = new JTextField("Student ID");
		userInput.setFont(regText);
	    a.gridx = 2;
	    a.gridy = 1;
	    loginPage.add(userInput, a);
	    
	    password = new JLabel ("Password: ");
		password.setFont(boldText);
		a.gridx = 1;
		a.gridy = 2;
		loginPage.add(password, a);
		
		passInput = new JPasswordField();
		passInput.setFont(regText);
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

