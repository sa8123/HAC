import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.List;
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date; 

public class UDPServer {
	
    private List<Client> network;	
    public UDPServer(List<Client> passed_net)
    {
        network = passed_net;
    }
	
    public void executeServer()
    {		 
        try 
        {
            DatagramSocket socket = new DatagramSocket(1234);
            socket.setSoTimeout(30000);
            byte[] incomingData = new byte[1024];

            boolean is_first_data_received = false;
            while(true) 

			{

				// Declaring the socket to receive data and store the message in message.
				if ((!network.isEmpty()) && (is_first_data_received == false)) {
					String message = "I am your server! - " + InetAddress.getLocalHost().getHostAddress();
					byte[] sendMessage = message.getBytes();
					
					for (int i = 0; i<network.size(); i++)
					{
						DatagramPacket sendPacket = new DatagramPacket(sendMessage, sendMessage.length, network.get(i).getIP(), network.get(i).getPort());
						socket.send(sendPacket);
					}
				}

				DatagramPacket receivePacket = new DatagramPacket(incomingData, incomingData.length);

				System.out.println("\n--------Server is listening ----------\n");

				

				socket.receive(receivePacket);
                                is_first_data_received = true;

				// Note the time

				InetAddress address = receivePacket.getAddress();

				int port = receivePacket.getPort();

				String message = new String(receivePacket.getData());

				

				System.out.println("Received message from client: " + message);

             System.out.println("Client IP:"+ address.getHostAddress());

             System.out.println("Client port: "+ port);

             

				// Create client object with ip address, (time_of_receipt + 30sec) within which the next message is to be received

				Calendar cal = Calendar.getInstance();

				System.out.println("Package was received at: --------------------" + cal.getTime().toString());

				

				cal.add(Calendar.SECOND, 30) ;

				System.out.println("Maximum wait_time from the same client till: " + cal.getTime().toString());

				

				Client newClient = new Client(address, cal, port);

				//Run over the arraylist to see if the client with IP address is there, if not:

				//Add the client object to the array

				

				//System.out.print("11111111111111111111111111111111111111111111111111111111111111111");

				

				if (network.isEmpty())

				{

					network.add(newClient);

				

					String r1 = "add-" + newClient.getIP().getHostAddress()+ "-" + newClient.getPort();

					byte[] s1 = r1.getBytes();

					DatagramPacket sp = new DatagramPacket(s1, s1.length, address, port);

					socket.send(sp);



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

						

						if(network.get(i).getIP().equals(address))

						{

							System.out.println("Client already found in arraylist");

							network.set(i, new Client(address, cal, port));

							break;

						}



						else if((network.get(i).getIP().equals(address) == false) && visited_all == true)

						{

							System.out.println("Client not found in arraylist, so added");

							network.add(new Client(address, cal, port));
                                                        
                                                       SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                                                      
                                      
                                                        
                                                        StringBuilder sbf = new StringBuilder("new-");
                                                        for (int s=0; s<network.size(); s++)
                                                        {
                                                            sbf.append(network.get(s).getIP().getHostAddress());
                                                            sbf.append("%%");
                                                            Date date = network.get(s).getCal().getTime();
                                                            sbf.append(df.format(date));
                                                            sbf.append("%%");
                                                            sbf.append(network.get(i).getPort());
                                                            if (s!=(network.size()-1))
                                                            {
                                                                sbf.append("$$");
                                                            }
                                                        }
							String r0 = sbf.toString();
                                                        byte[] s0 = r0.getBytes();
                                                        DatagramPacket sp0 = new DatagramPacket(s0, s0.length, address, port);
                                                        socket.send(sp0);

							String r1 = "add-" + newClient.getIP().getHostAddress() + "-" + newClient.getPort();

							byte[] s1 = r1.getBytes();

							for (int k=0; k<network.size(); k++)

							{
							DatagramPacket sp = new DatagramPacket(s1, s1.length, network.get(k).getIP(), network.get(k).getPort());
							socket.send(sp);

							}

							

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

				

				

             String response = "Thank you for the message----- Response sent from server";

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

						

						System.out.println("Response sent to update arraylist by removing.");

						String r2 = "rmv-" + network.get(i).getIP().getHostAddress()+ "-" + newClient.getPort();

						byte[] s2 = r2.getBytes();

						for (int k=0; k<network.size(); k++)

						{
                                                DatagramPacket spz = new DatagramPacket(s2, s2.length, network.get(k).getIP(), network.get(k).getPort());
						socket.send(spz);

						}

						network.remove(i); 

					}				

				}//for-end

                                // Clear the buffer after every use
         incomingData = new byte[1024];
			}//(infinite while loop end)

			

		}catch (SocketException e) 

		{	

			e.printStackTrace();

		} 

         catch (SocketTimeoutException e)
         {
        	 e.printStackTrace();
         }
		catch (IOException e) 

		{

			e.printStackTrace();

		} 
         
         
     }




}
