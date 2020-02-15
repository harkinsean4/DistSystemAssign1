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
	private static int accessToken;
	private static ArrayList<Assessment> clientAssessmentList = new ArrayList<Assessment>();
	
	public ClientDAO()
	{		
		int registryport = 20345;

        System.out.println("RMIRegistry port = " + registryport);
        
        if(System.getProperty("java.class.path").contains("harki"))
        {
        	System.out.print("Sean");
        	System.setProperty("java.rmi.server.hostname","127.0.0.1");
        	System.setProperty("java.security.policy","file:/c:/Users/harki/workspace/DS_RMI_SeanHarkin_Assignment_1/src/client.policy");
        	System.setProperty("java.rmi.server.codebase","file:/c:/Users/harki/workspace/DS_RMI_SeanHarkin_Assignment_1/src/");
        }
        else{
        	System.out.print("Darragh");
        	System.setProperty("java.rmi.server.hostname","127.0.0.1");
        	System.setProperty("java.security.policy","file:/c:/Users/harki/workspace/DS_RMI_SeanHarkin_Assignment_1/src/client.policy");
        	System.setProperty("java.rmi.server.codebase","file:/c:/Users/harki/workspace/DS_RMI_SeanHarkin_Assignment_1/src/");
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
		try {
			accessToken = examServer.login(16316271, "fearghal");
			
			if (accessToken != 0){
	        	System.out.println("Successful login, access token for client is: " + accessToken);
	        }
	        else{
	        	System.err.println("unsuccessful login");
	        }  
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnauthorizedAccess e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accessToken;
	}

	// Return a summary list of Assessments currently available for this studentid
	public List<String> getAvailableSummary(int token, int studentid)
	{
		try {			
			System.out.println("Get Available Summary of Assessments ");
			List<String> availableAssessments = examServer.getAvailableSummary(accessToken, 16316271);
			
			if (availableAssessments != null)
			{
				if(!availableAssessments.isEmpty())
				{
					for (int i = 0; i < availableAssessments.size(); i++)
					{
						System.out.println(availableAssessments.get(i));
					}
				}
				else{
					System.out.println("No available assessments");
				}
			}
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnauthorizedAccess e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoMatchingAssessment e) {
			// TODO Auto-generated catch block
			System.err.println("Assesment for that course code does not exist");
			e.printStackTrace();
		}
		return null;
		
	}
	
	// Return an Assessment object associated with a particular course code
	public Assessment getAssessment(int token, int studentid, String courseCode){
		try {
			Assessment assessment = examServer.getAssessment(accessToken, 16316271, "CT414");
			
			clientAssessmentList.add(assessment);
			
			System.out.println(assessment.getInformation());
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnauthorizedAccess e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoMatchingAssessment e) {
			// TODO Auto-generated catch block
			System.err.println("Assesment for that course code does not exist");
			e.printStackTrace();
		}
		return null;
		
	}

	// Submit a completed assessment
	public void submitAssessment(int token, int studentid, Assessment completed)
	{
		Assessment assessment =clientAssessmentList.get(0);
		
		try {	
			System.out.println("Submit Assessment");
			examServer.submitAssessment(accessToken, 16316271, assessment);			
		} 
		catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnauthorizedAccess e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoMatchingAssessment e) {
			// TODO Auto-generated catch block
			System.err.println("Assesment for that course code does not exist");
			e.printStackTrace();
		}
	}
}
