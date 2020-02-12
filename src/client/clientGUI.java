package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import server.Assessment;
import server.ExamServer;
import server.NoMatchingAssessment;
import server.UnauthorizedAccess;

public class clientGUI 
{
	private static ExamServer examServer = null;
	private static int accessToken;
	private static ArrayList<Assessment> clientAssessmentList = new ArrayList<Assessment>();
	
	public static void main(String args[]) {
        int registryport = 20345;

        if (args.length > 0)
           registryport = Integer.parseInt(args[0]);

        System.out.println("RMIRegistry port = " + registryport);

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "ExamServer";
            Registry registry = LocateRegistry.getRegistry(registryport);
            examServer = (ExamServer) registry.lookup(name);
        } catch (Exception e) {
            System.err.println("clientGUI exception:");
            e.printStackTrace();
        }

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
		
		try {
			Assessment assessment = examServer.getAssessment(accessToken, 16316271, "CT414");
			
			clientAssessmentList.add(assessment);
			
			System.out.println(assessment.getInformation());
			
			System.out.println("Submit Assessment");
			examServer.submitAssessment(accessToken, 16316271, assessment);
			
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
		/*
		Assessment assessmentToSubmmit = clientAssessmentList.get(0);
		
		if (assessmentToSubmmit == null)
		{
			try {
				examServer.submitAssessment(accessToken, 16316271, assessmentToSubmmit);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnauthorizedAccess e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoMatchingAssessment e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Assessment submitted");
		}
		else{
			System.err.println("No assesment to submit");
		}*/
    }
}
