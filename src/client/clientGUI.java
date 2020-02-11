package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import server.ExamEngine;

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
            String name = "ExamEngine";
            Registry registry = LocateRegistry.getRegistry(registryport);
            ExamEngine examEngine = (ExamEngine) registry.lookup(name);
            examEngine.login(16316271, "fearghal");
            
        } catch (Exception e) {
            System.err.println("clientGUI exception:");
            e.printStackTrace();
        }
    }
}
