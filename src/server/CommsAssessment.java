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
	private int studentID = 0;
	private Date closingDate = null;
	private ArrayList<Question> questionList = new ArrayList<Question>();
	private HashMap<Question, Integer> answerMap = new HashMap<Question, Integer>();
	
	public CommsAssessment(int studentID)
	{
		this.studentID = studentID;
		questionList.add(new Question1());
		questionList.add(new Question2());
	}

	public String getInformation() {
		String information = ASSESSMENTNAME;
		
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
		return questionList.get(questionNumber);
	}

	// Answer a particular question
	public void selectAnswer(int questionNumber, int optionNumber)
			throws InvalidQuestionNumber, InvalidOptionNumber 
	{
		answerMap.put(questionList.get(questionNumber), optionNumber);
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
