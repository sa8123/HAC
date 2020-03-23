package edu.truman.p2p;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.net.InetAddress;
/*
 * This program implements the UDP(User Datagram Protocol) to
 * main the HAC(High Availability Cluster) where we keep the list
 * our clients which are going to act as Peer. When the program 
 * starts all of our clients start exchanging messages with each
 * other implementing the Peer-to-Peer architecture. 
 * @author Austin Bell, Sudip Aryal and Deepson Shrestha.
 * @date March 20, 2020
 */
public class P2P 

{
	private static Thread sendUpdate;
	private static Thread startSeeding;
	public P2P() 
	{
		/* Starting the thread named sendUpdate. It basically
		 * goes over the ArrayList of our clients and sends
		 * update to all of the clients present on the list.
		 */
		sendUpdate = new Thread() 
		{
			public void run() 

			{
				String message = new String("Peering Started: ");
				byte data[] = message.getBytes();
				DatagramSocket socket = null;
				
				try 
				{
					socket = new DatagramSocket();
					socket.setBroadcast(true);
				} 
				catch (SocketException ex) 

				{
					ex.printStackTrace();
				}
				while (true)

				{
					for (int j = 0; j < network.size(); j++) 

					{
						InetAddress ipAddress;
						try 
						{
							/* Send the message if your IP does not matches
							 * your IP address from the ArrayList. 
							 */
							ipAddress = network.get(j).getIP();
							if(ipAddress != InetAddress.getLocalHost()) 
							{
								DatagramPacket sendPacket = new DatagramPacket(data,data.length,ipAddress,9876);
								socket.send(sendPacket);
								Thread.sleep(5000);
							}
						} 
						catch (UnknownHostException e) 
						{
							System.out.println("Client " + network.get(j).getIP().toString() + " is down.");
						}
						catch (IOException e) 

						{
							e.printStackTrace();
						}
						catch (InterruptedException e)

						{
							e.printStackTrace();
						}
					}
				}
			}
		};
		
		/* In order for the peer-to-peer architecture. First the
		 * server needs to perform seeding. When the seeding is done
		 * peers can start sharing information between themselves.  
		 */
		startSeeding = new Thread() 
		{
			public void run() 

			{
				DatagramSocket seedSocket = null;
				byte[] receiveData = new byte[1024];
				try 

				{
					seedSocket = new DatagramSocket(9876);

				} 
				catch (SocketException ex) 

				{
					ex.printStackTrace();

				}

				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				while (true) 
				{
					try 

					{
						seedSocket.receive(receivePacket);
						InetAddress address = receivePacket.getAddress();       
						Calendar cal = Calendar.getInstance();
						System.out.println("Message received from: " + receivePacket.getAddress().toString().substring(1) + " at " + cal.getTime().toString());
						cal.add(Calendar.SECOND, 30) ; 
						
						/* Iterating over the ArrayList to check if the peer is down
						 * or not. 
						 */
						for (int i = 0; i < network.size(); i++)

						{
							if(network.get(i).getIP().equals(address) == true)
							{
								network.set(i, new Client(address, cal));
							}

							Calendar right_now = Calendar.getInstance();
							int j = right_now.compareTo(network.get(i).getCal());
							if (j == 1)
							{
								System.out.println("Client: " + network.get(i).getIP().toString().substring(1) + " is down");
							}	
						}
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
			}

		};
	}

	// List to store peer IP's and Calendar time. 
	public static ArrayList<Client> network = new ArrayList<Client>();
	
	/* Method to initiate startSeeding thread. 
	 */
	public void startSeeding() 
	{
		startSeeding.start();
	}
	
	/* Method to send messages to each peer. 
	 */
	public void startSending() throws UnknownHostException
	{
		sendUpdate.start();
	}

	public static void main(String[] args) throws Exception 
	{
		// Hard coding peer's IP address and noting the time. 
		Calendar cal = Calendar.getInstance();
		InetAddress ad1 = InetAddress.getByName("192.168.0.22");
		network.add(new Client(ad1, cal));
		InetAddress ad2 = InetAddress.getByName("192.168.0.76");
		network.add(new Client(ad2, cal));
		InetAddress ad3 = InetAddress.getByName("192.168.0.77");
		network.add(new Client(ad3, cal));
		InetAddress ad4 = InetAddress.getByName("192.168.0.78");
		network.add(new Client(ad4, cal));
		InetAddress ad5 = InetAddress.getByName("192.168.0.79");
		network.add(new Client(ad5, cal));
		cal.add(Calendar.SECOND, 30);
		P2P peer = new P2P();
		peer.startSeeding();
		peer.startSending();
	}
}
