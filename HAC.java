import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.List;
import java.net.UnknownHostException;

/**
 * @author Team3
 *
 */
public class HAC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Client> net = Collections.synchronizedList(new ArrayList<Client>());
		System.out.println("Are you a client or a server? \n"
                + "Enter \"C\" if you are a client and \"S\" if you are a server");
        Scanner keyboard = new Scanner(System.in);
        
        if(keyboard.next().charAt(0)=='C')
        {
            System.out.println("Hey client, what is the server IP address you want to connect to?");
            Scanner k2 = new Scanner(System.in);
            String server_address = k2.nextLine();
            try{ 
            	InetAddress add = InetAddress.getByName(server_address); 
            	UDPClient c = new UDPClient(add, net);
            	c.executeClient();
            }
            catch(UnknownHostException e)
            {
                System.out.println("Server not found!");
            }
            UDPServer theServer = new UDPServer(net);
            theServer.executeServer();
        }
        
        else if(keyboard.next().charAt(0)=='S')
        {
        	System.out.println("I am the server, I am receiving the data");
            UDPServer theServer = new UDPServer(net);
            theServer.executeServer();
        }
        keyboard.close();


	}

}
