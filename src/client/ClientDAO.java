/*
 * ClientDAO acts as intermediate between the client and server so the code can be decoupled
 * It's role is to look up the RMI registry for the exam engine remote object and invoke it's remote method
 * It also catches and handles all exceptions
 * */

package client;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import server.Assessment;
import server.ExamServer;
import server.NoMatchingAssessment;
import server.UnauthorizedAccess;

public class ClientDAO 
{
	private int studentid;
	private String password;
	private static ExamServer examServer = null;
	
	public ClientDAO()
	{		
		int registryport = 20345;

        System.out.println("RMIRegistry port = " + registryport);
        
        if(System.getProperty("java.class.path").contains("harki"))
        {
        	//System.out.print("Sean");
        	System.setProperty("java.rmi.server.hostname","127.0.0.1");
        	System.setProperty("java.security.policy","file:/c:/Users/harki/workspace/DS_RMI_SeanHarkin_Assignment_1/src/client.policy");
        	System.setProperty("java.rmi.server.codebase","file:/c:/Users/harki/workspace/DS_RMI_SeanHarkin_Assignment_1/src/");
        }
        else{
        	//System.out.print("Darragh");
        	System.setProperty("java.rmi.server.hostname","127.0.0.1");
        	System.setProperty("java.security.policy","file:/c:/Users/Darragh/eclipse-workspace/DistSystemAssign1/src/client.policy");
        	System.setProperty("java.rmi.server.codebase","file:/c:/Users/Darragh/eclipse-workspace/DistSystemAssign1/src/");
        }
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "ExamServer";
            Registry registry = LocateRegistry.getRegistry(registryport);
            
            examServer = (ExamServer) registry.lookup(name);
        } catch (Exception e) {
            System.err.println("ClientDAO exception:");
            e.printStackTrace();
        }
	}
	
	// Return an access token that allows access to the server for some time period
	public int login(int studentid, String password)
	{
		int accessToken = 0;
		
		try {
			accessToken = examServer.login(studentid, password);
			
			if (accessToken != 0){
	        	System.out.println("ClientDAO - Successful login, access token for client is: " + accessToken);
	        }
	        else{
	        	System.err.println("ClientDAO - Unsuccessful login");
	        }  
			
		} catch (RemoteException e) {
			System.err.println("RemoteException - Unsuccessful login "+ e.getMessage());
			//e.printStackTrace();
		} catch (UnauthorizedAccess e) {
			System.err.println("UnauthorizedAccess - Unsuccessful login "+ e.getMessage());
			//e.printStackTrace();
		}
		
		return accessToken;
	}

	// Return a summary list of Assessments currently available for this studentid
	public List<String> getAvailableSummary(int token, int studentid)
	{
		List<String> availableAssessments = null;
		
		try {			
			System.out.println("getAvailableSummary() - Get Available Summary of Assessments ");
			availableAssessments = examServer.getAvailableSummary(token, studentid);
			
			if (availableAssessments != null)
			{
				if(!availableAssessments.isEmpty())
				{
					for (int i = 0; i < availableAssessments.size(); i++)
					{
						System.out.println("ClientDAO: "+ availableAssessments.get(i));
					}
					
					return availableAssessments;
				}
				else{
					System.out.println("ClientDAO - No available assessments");
				}
			}
			
		} catch (RemoteException e) {
			System.err.println("RemoteException - Unsuccessful login " + e.getMessage());
			//e.printStackTrace();
		} catch (UnauthorizedAccess e) {
			System.err.println("UnauthorizedAccess - Unable to getAvailableSummary " +e.getMessage());
			//e.printStackTrace();
		} catch (NoMatchingAssessment e) {
			System.err.println("NoMatchingAssessment - Unable to getAvailableSummary " + e.getMessage());
			//e.printStackTrace();
		}
		return null;
		
	}
	
	// Return an Assessment object associated with a particular course code
	public Assessment getAssessment(int token, int studentid, String courseCode)
	{
		Assessment assessment = null;
		
		try {
			assessment = examServer.getAssessment(token, studentid, courseCode);
			
			System.out.println("ClientDAO - " + assessment.getInformation());
			
		} catch (RemoteException e) {
			System.err.println("RemoteException -  Unable to getAssessment() " + e.getMessage());
			//e.printStackTrace();
		} catch (UnauthorizedAccess e) {
			System.err.println("UnauthorizedAccess -  Unable to getAssessment() " + e.getMessage());
			//e.printStackTrace();
		} catch (NoMatchingAssessment e) {
			System.err.println("NoMatchingAssessment -  Unable to getAssessment() " + e.getMessage());
			//e.printStackTrace();
		}
		return assessment;
		
	}

	// Submit a completed assessment
	public void submitAssessment(int token, int studentid, Assessment completed)
	{
		
		try {	
			examServer.submitAssessment(token, studentid, completed);
			System.out.println("ClientDAO - submitAssessment() - " + completed.getInformation());
		} 
		catch (RemoteException e) {
			System.err.println("RemoteException -  Unable to submitAssessment() " + e.getMessage());
			//e.printStackTrace();
		} catch (UnauthorizedAccess e) {
			System.err.println("UnauthorizedAccess -  Unable to submitAssessment() " + e.getMessage());
			//e.printStackTrace();
		} catch (NoMatchingAssessment e) {
			System.err.println("NoMatchingAssessment -  Unable to submitAssessment() " + e.getMessage());
			//e.printStackTrace();
		}
	}
}
