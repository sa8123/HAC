/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author maste
 */
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.List;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import java.net.*;
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date;
import java.text.ParseException;


public class UDPClient

{

	
	private List<Client> network;
	private InetAddress server_address;

        DatagramSocket socket;

	

	//Constructor 

	public UDPClient(InetAddress add, List<Client> passed_net)

	{

		server_address = add;
		network = passed_net;

	}

	public void setServerAddress(InetAddress add)
	{
		server_address = add;
	}

	public void executeClient() 
	{
		//List<Client> toreturn;
    	// create instance of Random class 
    	Random rand = new Random();
    	
        boolean hasConnectionEstablished = false;
    	while (true) {
            System.out.println(hasConnectionEstablished);
            int random_int = 0;
            if (hasConnectionEstablished == true)
            {
                random_int = rand.nextInt(30000);
            }
            try 
            {
                Thread.sleep(random_int);
		try 
		{
                        socket = new DatagramSocket();
			InetAddress address = server_address;
			String message = "Hello! from Client Windows, I am the simple guy";
			byte[] sendMessage = message.getBytes();
			byte[] receiveMessage = new byte[1024];
			DatagramPacket sendPacket = new DatagramPacket(sendMessage, sendMessage.length, address, 1234);
			socket.send(sendPacket);
			System.out.println("Message sent");

			DatagramPacket receivePacket = new DatagramPacket(receiveMessage, receiveMessage.length);
     
                        socket.setSoTimeout(5000);
                        
                        try {
                            socket.receive(receivePacket);
                            hasConnectionEstablished = true;
                            boolean isFirstResponse = true; 
                            String actualData = new String(receivePacket.getData());
                            
                            if(actualData.substring(0,4).equals("new-") && isFirstResponse == true)
                            {
                                isFirstResponse = false;
                                String[] strdiv = actualData.split("-");
                                String useful_data = strdiv[1];
                                String[] individual_clients = useful_data.split("$$");
                                
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                                
                                for (int e=0; e<individual_clients.length;e++)
                                {
                                    String[] individualComponents = individual_clients[e].split("%%");
                                    InetAddress ress = InetAddress.getByName(individualComponents[0]);
                                    
                                    
                                     try{
                                         Date date = df.parse(individualComponents[1]);
                                         Calendar cale = Calendar.getInstance();
                                                    cale.setTime(date);
                                                    int prt = Integer.parseInt(individualComponents[2]);
                                            
                                    network.add(new Client(ress, cale, prt));
                                               }
                                                        catch (ParseException x)
                                                                    {
                                                        System.out.println("hahaha");
                                                            }
                                }
                                
                            }
                            
                            else if(actualData.substring(0,20).equals("I am your server! - "))
                            {
                        	  System.out.println("Server address changed");
                        	  for (int m = 0; m<network.size();m++)
                                  {
                        		  if(network.get(m).getIP().equals(receivePacket.getAddress())== true)
                        		  {
                        			  network.remove(m);
                        			  break;
                        		  }
                                  }
                        	  setServerAddress(receivePacket.getAddress());
                            }
                       
                            else if (actualData.substring(0,4).equals("rmv-"))
                            {
                                String[] arrOfStr = actualData.split("-");
                                InetAddress adr = InetAddress.getByName(arrOfStr[1]);
                                for (int m = 0; m<network.size();m++)
                                {
                                    if(network.get(m).getIP().equals(adr)== true)
                                    {
                                        network.remove(m);
                                    }
                                }
                            }
                            
                            else if (actualData.substring(0,4).equals("add-"))
                            {
                        	String[] arrOfStr = actualData.split("-");
                                InetAddress adr = InetAddress.getByName(arrOfStr[1]);
                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.SECOND, 30);
                                int prt = Integer.parseInt(arrOfStr[2].trim());
                                network.add(new Client(adr, cal, prt));
                            }
                        
                            System.out.println(actualData);
                        }
                        
                        catch (SocketTimeoutException e)
                        {
                            System.out.println("I am the fucking server now, everyone send me the data");
                            socket.close();
                            
                            boolean is_server_removed_from_clientList = false;
                            for (int i = 0; (i<network.size()) && (is_server_removed_from_clientList == false) ; i++)
                            {
                                if (network.get(i).getIP().equals(InetAddress.getLocalHost()))
                                {
                                    network.remove(i);
                                    is_server_removed_from_clientList = true;
                                }
                            }
                            return;                        
                        }
         
			


 System.out.println("Printing out the arraylist: ");
			//socket.close();
                        for (int i = 0; i<network.size(); i++)
                        {
                           
                            System.out.println("Client" + (i+1) + "----: " + network.get(i).getIP());
                        }

			

                        //System.out.println(InetAddress.getLocalHost().equals(network.get(0).getIP()));
			

		} 

		catch (SocketException e) 

		{

			e.printStackTrace();

		} 

		catch (UnknownHostException e) 

		{

			e.printStackTrace();

		} 

		catch (IOException e) 

		{

			e.printStackTrace();

		}
	}
	catch(InterruptedException e)
    {
		e.printStackTrace();
    }
	
    	}

	}
	


} //end of class
