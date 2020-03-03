import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.*;


public class UDPServer 
{
	DatagramSocket socket = null;
	//Constructor
	public ArrayList<Client> network = new ArrayList <Client>();
	public UDPServer()
	{
		
	}
	
	public void listenToSocket() 
	{
	    
		try 
		{
			socket = new DatagramSocket(1234);
			
			// Closes the socket if does not get respond from all the clients for 30 seconds.
			socket.setSoTimeout(30000);
			byte [] incomingData = new byte[1024];

		   // public static ArrayList <Client> network = new ArrayList <Client>();
			while(true) 
			{
				// Declaring the socket to receive data and store the message in message.
				DatagramPacket receivePacket = new DatagramPacket(incomingData, incomingData.length);
				System.out.println("--------Server is listening ----------\n");
				
				socket.receive(receivePacket);
				// Note the time
				InetAddress address = receivePacket.getAddress();
				int port = receivePacket.getPort();
				
				System.out.println("----Received message from client IP: " + address.getHostAddress());
                System.out.println("Client IP:"+ address.getHostAddress());
                System.out.println("Client port: "+ port);
                
				// Create client object with ip address, (time_of_receipt + 30sec) within which the next message is to be received
				Calendar cal = Calendar.getInstance();
				System.out.println("Package was received at: --------------------" + cal.getTime().toString());
				
				cal.add(Calendar.SECOND, 30) ;
				System.out.println("Maximum wait_time from the same client till: " + cal.getTime().toString());
				
				Client newClient = new Client(address, cal);
				//Run over the arraylist to see if the client with IP address is there, if not:
				//Add the client object to the array
				
				//System.out.print("11111111111111111111111111111111111111111111111111111111111111111");
				
				if (network.size() == 0)
				{
					network.add(newClient);
				} //ifend
				
			
				else
				{	
					//System.out.println("Network size is not 0");
					boolean visited_all = false;
					//System.out.println("The network size is: " + network.size());
					//System.out.println(" equals?" + network.get(0).getIP().equals(address));
					
					for (int i = 0; i < network.size(); i++)
					{
						if (i == (network.size() - 1))  
						{
							visited_all = true;
						}
						
						if(network.get(i).getIP().equals(address) == true)
						{
							System.out.println("Client already found in arraylist");
							network.set(i, new Client(address, cal));
							break;
						}

						else if((network.get(i).getIP().equals(address)) == false && visited_all == true)
						{
							System.out.println("Client not found in arraylist, so added");
							network.add(new Client(address, cal));
							break;
						}
					}
				}//else-end
				
				
				//Else, replace with a new client object with same IP address but different time
				//create for loop, loop through the array and see if (current_time > time within which the next message is to be received)

				
				
				for (int i = 0; i <network.size(); i++)
				{
					System.out.println("---------------------------");
					System.out.println("Printing out the arraylist: ");
					System.out.println("Client" + (i + 1) + "-----------: " + network.get(i).getIP());
				}
				
                String response = "Thank you for the message.";
                byte[] sendData = response.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
                System.out.println("Sending packet");
                socket.send(sendPacket);
                
                //Loops through the arraylist and removes the client that has not responded for more than 30 seconds.
				for (int i = 0; i<network.size(); i++)
				{
					Calendar right_now = Calendar.getInstance();
					//System.out.println(right_now.getTime().toString());
					
					int j = right_now.compareTo(network.get(i).getCal());
					//System.out.println(j);
					if (j == 1)
					{
						System.out.println("Server did not hear back from the client" + network.get(i).getIP().getHostAddress());
						network.remove(i); 
					}
					
				}//for-end
		
			}
			
		}catch (SocketException e) 
		{	
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
	}
	public static void main(String[] args) 
	{
		UDPServer theServer = new UDPServer();
		theServer.listenToSocket();

	}

}