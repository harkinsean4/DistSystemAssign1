package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.ExamEngine;
import server.ExamServer;

public class clientGUI 
{
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
            ExamServer examServer = (ExamServer) registry.lookup(name);
            examServer.login(16316271, "fearghal");
            
        } catch (Exception e) {
            System.err.println("clientGUI exception:");
            e.printStackTrace();
        }
    }
}
