/*ExamServer.java
 * Remote interface provides methods for user authentication, download of
 * assessments and the submission of completed assessments. Assessments can only be
 * downloaded and submitted during certain time intervals while they are available on the
 * server
*/
package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ExamServer extends Remote {

	// Return an access token that allows access to the server for some time period
	public int login(int studentid, String password) throws 
		UnauthorizedAccess, RemoteException;

	// Return a summary list of Assessments currently available for this studentid
	public List<String> getAvailableSummary(int token, int studentid) throws
		UnauthorizedAccess, NoMatchingAssessment, RemoteException;

	// Return an Assessment object associated with a particular course code
	public Assessment getAssessment(int token, int studentid, String courseCode) throws
		UnauthorizedAccess, NoMatchingAssessment, RemoteException;

	// Submit a completed assessment
	public void submitAssessment(int token, int studentid, Assessment completed) throws 
		UnauthorizedAccess, NoMatchingAssessment, RemoteException;

}

