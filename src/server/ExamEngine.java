package server;

// TIP: The return statement will not run if the exception is thrown.

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;

public class ExamEngine implements ExamServer 
{
	private HashMap<Integer, String> loginHashMap = new HashMap<Integer, String>();
	//private HashMap<Integer, ArrayList<Assessment>> assessmentHashMap = new HashMap<Integer, Assessment[]>();
	private static String DISTCOURSECODE = "CT414";
	private static String COMMSCOURSECODE = "EE444";
	private static int ACCESSTOKEN = 0;

    // Constructor is required
    public ExamEngine() 
    {
        super();
        
        loginHashMap = new HashMap();
        
        loginHashMap.put(16316271, "fearghal");
    }
    
    public static void main(String[] args) {
		int registryport = 20345;

        if (args.length > 0)
           registryport = Integer.parseInt(args[0]);
        
        System.out.println("RMIRegistry port = " + registryport);
		
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "ExamServer";
            ExamServer engine = new ExamEngine();
            
            // Exam Server is a remote object - RMI passes a remote stub to the client for a remote object
            // client can invoke methods on the stub
            ExamServer stub =
                (ExamServer) UnicastRemoteObject.exportObject(engine, 0);
            
            Registry registry = LocateRegistry.getRegistry(registryport);
			//System.setProperty("java.rmi.server.hostname","127.0.1.1;");
            registry.rebind(name, stub);
            System.out.println("ExamEngine bound");
        } catch (Exception e) {
            System.err.println("ExamEngine exception:");
            e.printStackTrace();
        }
    }

    // Implement the methods defined in the ExamServer interface...
    // Return an access token that allows access to the server for some time period
    public int login(int studentid, String password) throws UnauthorizedAccess
    {	
    	String actualPassword = "";
    	
    	if (loginHashMap.containsKey(studentid)) 
    	{
    		// is hashmap contains student id as key 
    		actualPassword = loginHashMap.get(studentid);
    	} 
    	else 
    	{
    	    throw new UnauthorizedAccess("No account credentials exist: " + studentid + "Make sure student ID is correct and try again");
    	}
	    
    	if(actualPassword.equals(password))
	    {
	    	System.out.println("Successful log in by student " + studentid);
	    	
	    	ACCESSTOKEN = ACCESSTOKEN +1; //increment access token for so next student who logs in gets different one
	    	return ACCESSTOKEN;
	    }
	    else
	    {
	    	System.err.println("Login from " + studentid);	
	    	throw new UnauthorizedAccess("incorrect password for: " + studentid + ". Please try again");
	    }
    }

    // Return a summary list of Assessments currently available for this studentid
    public List<String> getAvailableSummary(int token, int studentid) throws
                UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        

        return null;
    }

    // Return an Assessment object associated with a particular course code
    public Assessment getAssessment(int token, int studentid, String courseCode) throws
                UnauthorizedAccess, NoMatchingAssessment, RemoteException {
    	
    	Assessment assessmentObj = null;
    	
    	
    	if (courseCode.equals(DISTCOURSECODE))
    	{
    		assessmentObj = new DistAssessment(studentid);
    	}
    	if (courseCode.equals(COMMSCOURSECODE))
    	{
    		assessmentObj = new CommsAssessment(studentid);
    	}
    	else{
    		throw new NoMatchingAssessment("No matching assessment for course code: " + courseCode);
    	}
    	
        return assessmentObj;
    }

    // Submit a completed assessment
    public void submitAssessment(int token, int studentid, Assessment completed) throws 
                UnauthorizedAccess, NoMatchingAssessment, RemoteException {

    	//Assessment[] assessmentList = assessmentHashMap.get(studentid);
    	
    	//assessmentList.
    	
    	//assessmentHashMap.put(studentid, arg1)
    }
}
