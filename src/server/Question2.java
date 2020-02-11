package server;

public class Question2 implements Question{

	private int questionNumber = 2;
	private String questionDetail = "Question Number 2 of MCQ";
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
