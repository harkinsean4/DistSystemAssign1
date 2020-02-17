package server;

public class QuestionDist implements Question 
{
	private int questionNumber;
	private String questionDetail;
	private String[] answerOptions; //= {"Yes", "No"};
	
	public QuestionDist(int questionNumber, String questionDetail, String[] answerOptions)
	{
		this.questionNumber = questionNumber; 
		this.questionDetail = questionDetail;
		this.answerOptions = answerOptions;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public String getQuestionDetail() 
	{
		questionDetail = Integer.toString(questionNumber) + ". " + questionDetail;
		
		return questionDetail;
	}

	public String[] getAnswerOptions() {
		return answerOptions;
	}
}
