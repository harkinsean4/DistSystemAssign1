/* Assessment.java 
 * Serializable interface provides methods for the retrieval of information
 * about the assessment, and the retrieval / answering of questions. It also has a method to
 * output the selected answer to each question - the answer provided to a question can then
 * be changed, if desired, prior to submission of the assessment. A completed assignment may
 * be submitted multiple times to the server up to the closing time. The last version submitted
 * would then be corrected
 * 
 * The Assessment object implements this interface that provides methods to retrieve and answer a
 * list of multiple-choice questions. 
 */
package server;

import java.util.Date;
import java.util.List;
import java.io.Serializable;

public interface Assessment extends Serializable {

	// Return information about the assessment	
	public String getInformation();

	// Return the final date / time for submission of completed assessment
	public Date getClosingDate();

	// Return a list of all questions and answer options
	public List<Question> getQuestions();

	// Return one question only with answer options
	public Question getQuestion(int questionNumber) throws 
		InvalidQuestionNumber;

	// Answer a particular question
	public void selectAnswer(int questionNumber, int optionNumber) throws
		InvalidQuestionNumber, InvalidOptionNumber;

	// Return selected answer or zero if none selected yet
	public int getSelectedAnswer(int questionNumber);

	// Return studentid associated with this assessment object
	// This will be preset on the server before object is downloaded
	public int getAssociatedID();

}



