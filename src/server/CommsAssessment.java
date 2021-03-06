package server;

//Assessment object is meant to hold the student ID

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CommsAssessment implements Assessment 
{
	private static String ASSESSMENTNAME = "EE444";
	private int assessmentNumber;
	private int studentID = 0;
	private Date closingDate = null;
	private ArrayList<Question> questionList = new ArrayList<Question>();
	private HashMap<Question, Integer> answerMap = new HashMap<Question, Integer>();
	
	public CommsAssessment(int studentID, int assessmentNumber)
	{
		this.studentID = studentID;
		this.assessmentNumber = assessmentNumber;
		
		String[] answers1 = {"Bring Shovel", "Don't forget it!", "Leave shovel at home"};
		questionList.add(new QuestionComms(1, "Don't forget to bring your shovel to work", answers1));
		
		String[] answers2 = {"on", "yer wan", "the wave bro"};
		questionList.add(new QuestionComms(2, "Ride __", answers2));
	}

	public String getInformation() {
		String information = "Assessment " + Integer.toString(assessmentNumber) + ". " + ASSESSMENTNAME;
		
		return information;
	}

	public Date getClosingDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			closingDate = sdf.parse("14/02/2020");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return closingDate;
	}

	public List<Question> getQuestions() {
		return questionList;
	}

	public Question getQuestion(int questionNumber) throws InvalidQuestionNumber 
	{
		Question question = null;
		
		if(questionNumber <= questionList.size())
		{
			//if questionNumber request is greater than size of question lidt 
			question = questionList.get(questionNumber);
		}
		else 
		{
			throw new InvalidQuestionNumber(); 
		}
		
		return question;
	}

	// Answer a particular question
	public void selectAnswer(int questionNumber, int optionNumber)
			throws InvalidQuestionNumber, InvalidOptionNumber 
	{
		if(questionNumber <= questionList.size())
		{
			//if questionNumber request is greater than size of question list
			if(optionNumber <= questionList.size()){
				answerMap.put(questionList.get(questionNumber), optionNumber);
			}
			else{
				throw new InvalidQuestionNumber(); 
			}
		}
		else 
		{
			throw new InvalidQuestionNumber(); 
		}
	}

	// Return selected answer or zero if none selected yet
	public int getSelectedAnswer(int questionNumber) {
		
		int selectedAnswer = answerMap.get(questionList.get(questionNumber));
		
		if (selectedAnswer != 0)
		{
			return selectedAnswer;
		}
		else
		{
			return 0;
		}
	}

	// Return studentid associated with this assessment object
	// This will be preset on the server before object is downloaded
	public int getAssociatedID() {
		return studentID;
	}

}
