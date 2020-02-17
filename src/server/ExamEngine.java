/*
 * Exam Engine is a remote object and also runs the main server program code
 * it makes itself available to the client by adding itself to rmi registry
 * it looks after client request to login and keeps track of what assessments
 * client gets and submits by using hashmaps
 */

package server;

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
	private HashMap<Integer, Boolean> isloggedinHashMap = new HashMap<Integer, Boolean>(); // Hash Map to keep track of who is logged in
	private HashMap<Integer, Boolean> loggedintimerHashMap = new HashMap<Integer, Boolean>(); // 
	private HashMap<Integer, Date> loggedinDateTimeHashMap = new HashMap<Integer, Date>(); // Hash Map tracking last time client was active by using Date object
	private HashMap<Integer, ArrayList<Assessment>> submittedAssessmentHashMap = new HashMap<Integer, ArrayList<Assessment>>(); //Hashmap of submitted Assessments ready for correction
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
        loginHashMap.put(16484724, "fergy");
        isloggedinHashMap.put(16316271, false);
        isloggedinHashMap.put(16484724, false);
    }
    
    
    public static void main(String[] args) {
		int registryport = 20345;

        if (args.length > 0)
           registryport = Integer.parseInt(args[0]);
        
        System.out.println("RMIRegistry port = " + registryport);
        
        //System.setProperty("java.rmi.server.hostname","127.0.1.1;");
        //System.setProperty("java.security.policy","file:c:Users/harki/workspace/DS_RMI_SeanHarkin_Assignment_1/src/server.policy");
        //System.setProperty("java.rmi.server.codebase","file:c:Users//harki/workspace/DS_RMI_SeanHarkin_Assignment_1/src/");
		
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
    		// if hashmap contains student id as key 
    		actualPassword = loginHashMap.get(studentid);
    	} 
    	else 
    	{
    	    throw new UnauthorizedAccess("No account credentials exist: " + studentid + " Make sure student ID is correct and try again");
    	}
	    
    	if(actualPassword.equals(password))
	    {
    		// check is submitted password matches actualPassword
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
	    			  Date logintime = loggedinDateTimeHashMap.get(studentid);
	    			  // display time and date using toString()
	    			  System.out.println(now.toString() + "is " + (now.getTime() - logintime.getTime()) + " greater than " + logintime.toString());
	    			  
	    			  if (now.getTime() - loggedinDateTimeHashMap.get(studentid).getTime() >= 2*60*1000)
	    			  {
	    				  // if last action was greater than 2 minutes then timeout
	    				  System.out.println("" + studentid + " login has timed out");
	    				  loggedintimerHashMap.put(ACCESSTOKEN, false);
	    			  }
	    			  else{
	    				  // else user is still active
	    				  // System.out.println("" + studentid + " is still active");
	    				  loggedintimerHashMap.put(ACCESSTOKEN, true);
	    			  }
	    				  
	    		  }
	    		}, 2*60*1000, 2*60*1000); // 1000 milliseconds = 1 second x 60 = 60 seconds x 2 = 2 minutes
	    	
	    	return ACCESSTOKEN;
	    }
	    else
	    {
	    	System.err.println("Login Error from " + studentid);
	    	System.err.println(""+ password + " - does not equal - " + actualPassword);
	    	throw new UnauthorizedAccess("incorrect password for: " + studentid + ". Please try again");
	    }
    }

    // Return a summary list of Assessments currently available for this studentid
    // it only returns assessments that have not been submitted yet 
    public List<String> getAvailableSummary(int token, int studentid) throws
                UnauthorizedAccess, NoMatchingAssessment, RemoteException 
    {
    	List<String> assmentSummaryStringList = new ArrayList<String>();
    	ArrayList submittedAssmentArrayList = null;
    	
    	// check student has valid access token
    	if (loggedintimerHashMap.get(token) == true) 
    	{
    		//if access token still valid, update the time since last method call
    		loggedinDateTimeHashMap.put(studentid, new Date());
    		
    		// add only uncompleted assessments to the available list, 
    		if (submittedAssessmentHashMap.containsKey(studentid)) 
        	{
    			//if student has submitted an assessment before check for it 
    			submittedAssmentArrayList = submittedAssessmentHashMap.get(studentid);
        		
        		for(int i = 0; i < submittedAssmentArrayList.size(); i++)
            	{
        			//loop through to get submitted assessments
        			Assessment assessment = (Assessment) submittedAssmentArrayList.get(i);
        			
        			String assessmentCode = assessment.getInformation();
        			
            		if (!assessmentCode.contains(DISTCOURSECODE)){
            			//check assessment code from submitted assessment list is not equal - meaning client has already taken out assessment and submitted
            			assmentSummaryStringList.add(DISTCOURSECODE);
            			
            		}
            		if (!assessmentCode.contains(COMMSCOURSECODE)){
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
			throw new UnauthorizedAccess("Access token has timed out");
		}
    	  	
        return assmentSummaryStringList;
    }

    // Return an Assessment object associated with a particular course code
    public Assessment getAssessment(int token, int studentid, String courseCode) throws
                UnauthorizedAccess, NoMatchingAssessment, RemoteException {
    	
    	Assessment assessmentObj = null;
    	
    	// check student has valid access token
    	if (loggedintimerHashMap.get(token) == true) 
    	{
    		//if access token still valid, update the time since last method call
    		loggedinDateTimeHashMap.put(studentid, new Date());
    		
    		ArrayList<Assessment> submittedAssessmentArrayList = null;
    		
			if (submittedAssessmentHashMap.containsKey(studentid)) 
			{
				// check if Hashmap for submitted assessments already contains submitted assessment for this student
				// students can't request an assessment they have already submitted
				
				submittedAssessmentArrayList = submittedAssessmentHashMap.get(studentid);
				System.out.println("get submitted Assessment Array List for " + studentid);
				
				if (submittedAssessmentArrayList == null){
					System.out.println("assmentArrayList for " + studentid + " is null");
					submittedAssessmentArrayList = new ArrayList<Assessment>();
				}
				else
				{
					for(int i = 0; i < submittedAssessmentArrayList.size(); i++)
	            	{
	        			//loop through to get submitted assessments
	        			Assessment assessment = (Assessment) submittedAssessmentArrayList.get(i);
	        			String assessmentCode = assessment.getInformation();
	        			
	            		if (courseCode.contains(assessmentCode))
	            		{
	            			//if the request course code is sontained in the submitted assessment list then throw exception
	            			throw new NoMatchingAssessment("You have already completed this assessment! - " + courseCode);
	            		}
	            	}
					// close for loop looping through submitted assessments
				}
			} 
			else{
			// else student has never submitted an assessment before	
				// if student has never submitted an assessment before, put in an empty array list in submittedHashMap
				System.out.println("getAssessment() - " +studentid + ", this student has never submitted an assessment before");
				ArrayList<Assessment> newSubmittedAssessmentArrayList = new ArrayList<Assessment>();
				submittedAssessmentHashMap.put(studentid, newSubmittedAssessmentArrayList);
			}
				
    		//get the assessment object according to requested course code
	    	if (courseCode.equals(DISTCOURSECODE))
	    	{
	    		System.out.println("getAssessment() for: " + courseCode);
	    		assessmentObj = new DistAssessment(studentid, 1);
	    	}
	    	else if (courseCode.equals(COMMSCOURSECODE))
	    	{
	    		System.out.println("getAssessment() for: " + courseCode);
	    		assessmentObj = new CommsAssessment(studentid, 2);
	    	}
	    	else{
	    		throw new NoMatchingAssessment("No matching assessment for course code: " + courseCode);
	    	}
    	}
    	else{
			throw new UnauthorizedAccess("Access token has timed out");
		}
    	
        return assessmentObj;
    }

    // Submit a completed assessment
    public void submitAssessment(int token, int studentid, Assessment completed) throws 
                UnauthorizedAccess, NoMatchingAssessment, RemoteException 
                {
    	System.out.println("submitAssessment() for: " + studentid);
    	
    	// check student has valid access token
    	if (loggedintimerHashMap.get(token) == true) 
    	{
    		//if access token still valid, update the time since last method call
    		loggedinDateTimeHashMap.put(studentid, new Date());
			ArrayList<Assessment> submittedAssessmentArrayList = null;
		
			if (submittedAssessmentHashMap.containsKey(studentid)) 
			{
				// check if Hashmap for submitted assessments already contains entries for this student
				// it should contain an empty assessment list (student id)- value (assessment array list)
				// created in the getAssessment() method
				
				submittedAssessmentArrayList = submittedAssessmentHashMap.get(studentid);
				System.out.println("get submitted Assessment Array List for " + studentid);
				
				if (submittedAssessmentArrayList == null){
					System.out.println("assmentArrayList for " + studentid + " is null");
					submittedAssessmentArrayList = new ArrayList<Assessment>();
				}
			} 
			else 
			{
				System.err.println("could not find entry in submittedAssessmentHashMap for studentid key:" + studentid);
			}
			
			System.out.println("Inserting completed assessment: " + completed.getInformation());
			submittedAssessmentArrayList.add(completed);
			submittedAssessmentHashMap.put(studentid, submittedAssessmentArrayList);
			
			ArrayList<Question> completedQuestionList = (ArrayList<Question>) completed.getQuestions();
			
			for(int i = 0; i < completedQuestionList.size(); i ++){
				
				Question question = completedQuestionList.get(i);	
				System.out.println(question.getQuestionDetail() + "?\nAnswer = " + completed.getSelectedAnswer(question.getQuestionNumber()));
			}
    	}
		else{
			throw new UnauthorizedAccess("Access token has timed out");
		}
    }
}
