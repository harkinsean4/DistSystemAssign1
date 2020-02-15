package server;

// TIP: The return statement will not run if the exception is thrown.

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ExamEngine implements ExamServer 
{
	private HashMap<Integer, String> loginHashMap = new HashMap<Integer, String>();
	private HashMap<Integer, Boolean> isloggedinHashMap = new HashMap<Integer, Boolean>();
	private HashMap<Integer, Boolean> loggedintimerHashMap = new HashMap<Integer, Boolean>();
	private HashMap<Integer, Date> loggedinDateTimeHashMap = new HashMap<Integer, Date>();
	private HashMap<Integer, ArrayList<Assessment>> submittedAssessmentHashMap = new HashMap<Integer, ArrayList<Assessment>>();
	private static String DISTCOURSECODE = "CT414";
	private static String COMMSCOURSECODE = "EE444";
	private static int ACCESSTOKEN = 0;
	private List<String> courseList = new ArrayList<String>();

    // Constructor is required
    public ExamEngine() 
    {
        super();
        
        loginHashMap = new HashMap();
        courseList.add(DISTCOURSECODE);
        courseList.add(COMMSCOURSECODE);
        
        loginHashMap.put(16316271, "fearghal");
        isloggedinHashMap.put(16316271, false);
    }
    
    
    public static void main(String[] args) {
		int registryport = 20345;

        if (args.length > 0)
           registryport = Integer.parseInt(args[0]);
        
        System.out.println("RMIRegistry port = " + registryport);
        
        //System.setProperty("java.rmi.server.hostname","127.0.1.1;");
        //System.setProperty("java.security.policy","file:c:Users/harki/workspace/DS_RMI_SeanHarkin_Assignment_1/src/server.policy");
        //System.setProperty("java.rmi.server.codebase","file:/mnt/c/Users/harki/workspace/DS_RMI_SeanHarkin_Assignment_1/src/");
		
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
			registry.rebind(name, stub);
            System.out.println("ExamEngine bound");
        } catch (Exception e) {
            System.err.println("ExamEngine exception:");
            e.printStackTrace();
        }
    }

    // Implement the methods defined in the ExamServer interface...
    // Return an access token that allows access to the server for some time period
    public int login(final int studentid, String password) throws UnauthorizedAccess
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
	    	
	    	if(isloggedinHashMap.get(studentid) == false){
	    		//check if student is already logged in
	    		isloggedinHashMap.put(studentid, true);
	    	}
	    	else{
	    		throw new UnauthorizedAccess(studentid + " already logged in!");
	    	}
	    	
	    	ACCESSTOKEN = ACCESSTOKEN +1; //increment access token for so next student who logs in gets different one
	    	
	    	//start the activation time for this timer
	    	loggedintimerHashMap.put(ACCESSTOKEN, true);
	    	loggedinDateTimeHashMap.put(studentid, new Date());
	    	
	    	Timer timer = new Timer();
	    	timer.scheduleAtFixedRate(new TimerTask() {
	    		  @Override
	    		  public void run() 
	    		  {
	    			  Date now = new Date();
	    			  // display time and date using toString()
	    			  System.out.println(now.toString());
	    			  
	    			  if (now.getTime() - loggedinDateTimeHashMap.get(studentid).getTime() >= 20*60*1000)
	    			  
	    			  loggedintimerHashMap.put(ACCESSTOKEN, false);
	    		  }
	    		}, 2*60*1000, 2*60*1000); // 1000 milliseconds = 1 second x 60 = 60 seconds x 2 = 2 minutes
	    	
	    	return ACCESSTOKEN;
	    }
	    else
	    {
	    	System.err.println("Login from " + studentid);	
	    	throw new UnauthorizedAccess("incorrect password for: " + studentid + ". Please try again");
	    }
    }

    // Return a summary list of Assessments currently available for this studentid
    // it only returns assessments that have not been submitted yet 
    public List<String> getAvailableSummary(int token, int studentid) throws
                UnauthorizedAccess, NoMatchingAssessment, RemoteException 
    {
    	List<String> assmentSummaryStringList = new ArrayList<String>();
    	ArrayList assmentArrayList = null;
    	
    	//check student is logged in
    	if (loginHashMap.containsKey(studentid)) 
    	{
    		// add only uncompleted assessments to the list, 
    		if (submittedAssessmentHashMap.containsKey(studentid)) 
        	{
    			//if student has submitted an assessment before check for it 
        		assmentArrayList = submittedAssessmentHashMap.get(studentid);
        		
        		for(int i = 0; i < assmentArrayList.size(); i++)
            	{
        			Assessment assessment = (Assessment) assmentArrayList.get(i);
        			
        			String assessmentCode = assessment.getInformation();
        			
            		if (!assessmentCode.equals(DISTCOURSECODE)){
            			//check assessment is not equal - meaning client had already taken out assessment and submitted
            			assmentSummaryStringList.add(DISTCOURSECODE);
            			
            		}
            		if (!assessmentCode.equals(COMMSCOURSECODE)){
            			assmentSummaryStringList.add(COMMSCOURSECODE);
            		}
            	}
        	}
    		else{
    			//student has yet to submit any assessment so just return full list
    			assmentSummaryStringList = courseList;
    		}
    	}
    	else{
    		System.err.println("getAvailableSummary() - Student not listed as logged in studentid " + studentid);
    	    throw new UnauthorizedAccess("getAvailableSummary() - No account credentials exist for: " + studentid);
    	}    	

        return assmentSummaryStringList;
    }

    // Return an Assessment object associated with a particular course code
    public Assessment getAssessment(int token, int studentid, String courseCode) throws
                UnauthorizedAccess, NoMatchingAssessment, RemoteException {
    	
    	Assessment assessmentObj = null;
    	
    	if (courseCode.equals(DISTCOURSECODE))
    	{
    		System.out.println("getAssessment() for: " + courseCode);
    		assessmentObj = new DistAssessment(studentid);
    	}
    	else if (courseCode.equals(COMMSCOURSECODE))
    	{
    		System.out.println("getAssessment() for: " + courseCode);
    		assessmentObj = new CommsAssessment(studentid);
    	}
    	else{
    		throw new NoMatchingAssessment("No matching assessment for course code: " + courseCode);
    	}
    	
    	// get existing submitted assessments
    	ArrayList assessmentArrayList = null;
    	
    	if (submittedAssessmentHashMap.containsKey(studentid)) 
    	{
    		// if student has already submitted an assessment before
    		assessmentArrayList = submittedAssessmentHashMap.get(studentid);
    		System.out.println("getAssessment() assessment array List for " + studentid);
    	} 
    	else
    	{
    		// if student has never submitted an assessment before, put in an emtpy array list in submittedHashMap
    		assessmentArrayList = new ArrayList<Assessment>();
			//assmentArrayList.add(assessmentObj);
    		submittedAssessmentHashMap.put(studentid, assessmentArrayList);
    	}
    	
    	submittedAssessmentHashMap.put(studentid, assessmentArrayList);
    	
        return assessmentObj;
    }

    // Submit a completed assessment
    public void submitAssessment(int token, int studentid, Assessment completed) throws 
                UnauthorizedAccess, NoMatchingAssessment, RemoteException 
                {
    	System.out.println("submitAssessment() for: " + studentid);
    	
    	ArrayList assmentArrayList = null;

    	if (submittedAssessmentHashMap.containsKey(studentid)) 
    	{
    		assmentArrayList = submittedAssessmentHashMap.get(studentid);
    		System.out.println("get Assessment Array List for " + studentid);
    		
    		if (assmentArrayList == null){
    			System.out.println("assmentArrayList for " + studentid + " is null");
    			assmentArrayList = new ArrayList<Assessment>();
    		}
    	} 
    	else 
    	{
    		System.err.println("assessmentHashMap does not have a key for studentid " + studentid);
    	    throw new UnauthorizedAccess("No account credentials exist for: " + studentid + "Make sure student ID is correct and try again");
    	}
    	
    	assmentArrayList.add(completed);
    	
    	System.out.println("Inserting completed assessment: " + completed.getInformation());
    	
    	submittedAssessmentHashMap.put(studentid, assmentArrayList);
    }
}
