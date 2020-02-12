package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

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
			
			examServer.submitAssessment(accessToken, 16316271, assessment);
			
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
