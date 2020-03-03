

import java.io.IOException;

import java.net.DatagramPacket;

import java.net.DatagramSocket;

import java.net.InetAddress;

import java.net.SocketException;

import java.net.UnknownHostException;

import java.util.ArrayList;

import java.util.Timer;

import java.util.TimerTask;

import java.util.Random;


public class UDPClient extends TimerTask

{

	DatagramSocket socket;

	

	

	

	//Constructor 

	public UDPClient()

	{

		

	}

	

	@Override

	public void run() 

	{

		try 

		{

			socket = new DatagramSocket();

			InetAddress address = InetAddress.getByName("150.243.205.194");

			String message = "Hello! from Client Deepson";

			byte[] sendMessage = message.getBytes();

			byte[] receiveMessage = new byte[1024];

			DatagramPacket sendPacket = new DatagramPacket(sendMessage, sendMessage.length, address, 1234);

			socket.send(sendPacket);

			System.out.println("Message sent from client");

			DatagramPacket receivePacket = new DatagramPacket(receiveMessage, receiveMessage.length);

			socket.receive(receivePacket);

			String actualData = new String(receivePacket.getData());
			System.out.println(actualData);

			//socket.close();

			

			

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

	public static void main(String[] args) throws InterruptedException 

	{
        // create instance of Random class 
        Random rand = new Random(); 
  
        // Generate random integers in range 0 to 30,000 
        int rand_int = rand.nextInt(30001); 
        
		Timer timer = new Timer();

		timer.schedule(new UDPClient(), 0, 
				rand_int);

	}



}