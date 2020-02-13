package client;

public class ClientDAOTest 
{
	//private static ClientDAO clientDAO;
	
	public ClientDAOTest(){
		
	}
	
	public static void main(String args[]) 
	{
		ClientDAO clientDAO = new ClientDAO();
		
		clientDAO.login(16316271, "fearghal");
    }

}
