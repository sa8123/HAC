package edu.truman.p2p;
import java.io.IOException;

import java.net.*;

import java.util.ArrayList;
import java.util.Calendar;


import java.net.InetAddress;




public class P2P 

{
	private static Thread sendUpdate;
	private static Thread startSeeding;
	public P2P() 
	{
		sendUpdate = new Thread() 
		{
			public void run() 

			{

				byte data[] = "Client is up".getBytes();

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
						//cal.add(Calendar.SECOND, 30) ; 
						//Loops through the arraylist and removes the client that has not responded for more than 30 seconds.
						for (int i = 0; i < network.size(); i++)

						{
							if(network.get(i).getIP().equals(address) == true)

							{

								network.set(i, new Client(address, cal));

							}

							Calendar right_now = Calendar.getInstance();


							int j = right_now.compareTo(network.get(i).getCal());

							if (j != 1)
							{
								System.out.println("Server did not hear back from the client: " + network.get(i).getIP().toString().substring(1));
								System.out.println("Client: " + network.get(i).getIP().toString().substring(1) + " is down");
							}	
						}
					} 
					catch (IOException ex) 
					{
						ex.printStackTrace();
					}
				}
			}

		};
	}

	public static ArrayList<Client> network = new ArrayList<Client>();
	public void startSeeding() 
	{
		startSeeding.start();
		//Calendar calendar = Calendar.getInstance();
		//System.out.println("Seeding at " + calendar.getTime().toString());
		//startSeeding.start();

	}
	public void startSending() throws UnknownHostException

	{
		sendUpdate.start();
		//Calendar calendar = Calendar.getInstance();
		//System.out.println("Starting to send message at " + calendar.getTime().toString());
		//sendUpdate.start();
	}

	public static void main(String[] args) throws Exception 
	{
		InetAddress ad1 = InetAddress.getByName("192.168.0.22");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 30) ;

		network.add(new Client(ad1, cal)); //Client 1

		InetAddress ad2 = InetAddress.getByName("192.168.0.76");
		network.add(new Client(ad2, cal)); //Client 2

		InetAddress ad3 = InetAddress.getByName("192.168.0.77");
		network.add(new Client(ad3, cal)); //Client 3

		//InetAddress ad4 = InetAddress.getByName("150.243.17.224");
		//network.add(new Client(ad4, cal)); //Client 4

		//startSeeding();

		//startSending();
		P2P peer = new P2P();
		peer.startSeeding();
		peer.startSending();
	}
}
