package edu.truman.p2p;
import java.net.InetAddress;

import java.util.Calendar;



public class Client {

	private InetAddress IPAddress;

	private Calendar cal;

	

    public Client(InetAddress IP, Calendar cal1)



    {



    	this.IPAddress = IP;



        this.cal = cal1;



    }

    

    public InetAddress getIP()



    {



    	return IPAddress;



    }

    

    public Calendar getCal()

    {

    	return cal;

    }

}

