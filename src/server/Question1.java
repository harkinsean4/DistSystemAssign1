package server;

public class Question1 implements Question 
{
	private int questionNumber = 1;
	private String questionDetail = "Question Number 1 of MCQ";
	private String[] answerOptions = {"Yes", "No"};

	public int getQuestionNumber() {
		return questionNumber;
	}

	public String getQuestionDetail() {
		return questionDetail;
	}

	public String[] getAnswerOptions() {
		return answerOptions;
	}

}
