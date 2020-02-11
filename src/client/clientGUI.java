package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.ExamEngine;
import server.ExamServer;
import server.UnauthorizedAccess;

public class clientGUI 
{
	private static ExamServer examServer = null;
	
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
			int loginBool = examServer.login(16316271, "fearghal");
			
			if (loginBool == 1){
	        	System.out.println("Successful login");
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
        
    }
}
