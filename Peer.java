package edu.truman.peertry;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Thread;

public class Peer extends Thread
{
	public Peer() 
	{
		startSeeding();
	}
	static ArrayList<String> clientInfo = new ArrayList<String>();
	Thread peerStart = new Thread() 
	{
		public void run() 
		{
			System.out.println("I am from run method");
			DatagramSocket socketPeer = null;
			try 
			{
				System.out.println("I am from run method");
				socketPeer = new DatagramSocket();
			} 
			catch (SocketException e) 
			{
				e.printStackTrace();
			}
			DatagramPacket packetPeer;
			byte[] bytePeer = new byte[1024];
			InetAddress address = null;
			String messagePeer;
			String ipAddress;
			//String message = new String("Hello");
			for(int i = 0; i < clientInfo.size(); i++) 
			{
				bytePeer = clientInfo.get(i).getBytes();
				try 
				{
					address = InetAddress.getByName(clientInfo.get(i));
				} 
				catch (UnknownHostException e) 
				{
					
					e.printStackTrace();
				}
				packetPeer = new DatagramPacket(bytePeer, bytePeer.length, address, 1234);
				try 
				{
					socketPeer.send(packetPeer);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			
		}
	};

	
	
	// It first acts as Server
	public void startSeeding() 
	{
		DatagramSocket socket;
		DatagramPacket receivedPacket;
		byte[] receivedData = new byte[1024];
		String actualData;
		try 
		{
			socket = new DatagramSocket(1234);
			receivedPacket = new DatagramPacket(receivedData, receivedData.length);
			while(true) 
			{
				try 
				{
					System.out.println("I am from seeding");
					socket.receive(receivedPacket);
					actualData = new String(receivedPacket.getData().toString());
					System.out.println("Seeding has started.");
					System.out.println(actualData);
					clientInfo.add(actualData);	
					System.out.println(actualData);
					peerStart.run();
					//startPeering();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	//If seeding is already done. 
	
	public static void main(String[] args) throws Exception
	{
		//clientInfo.add("192.168.0.71");
		//clientInfo.add("192.168.0.77");
		//clientInfo.add("192.168.0.76");
		Scanner in = new Scanner(System.in);
		System.out.println("Are you peer or server seeder?(P/S)");
		String whoIs = in.nextLine();
		if(whoIs.equals("P")) 
		{
			System.out.println("Enter the IP address");
			String ip = in.nextLine();
			System.out.println(ip);
			clientInfo.add(ip);
		}
		else if(whoIs.equals("S")) 
		{
			InetAddress ip = InetAddress.getByName("localhost");
			clientInfo.add(ip.toString());
		}
		Peer peer = new Peer();
		peer.startSeeding();
	}

	

}
