
import java.net.InetAddress;
import java.util.Calendar;

public class Client {
	private InetAddress IPAddress;
	private Calendar cal;
	private int port;
	
    public Client(InetAddress IP, Calendar cal1, int port)

    {

    	this.IPAddress = IP;

        this.cal = cal1;
        
        this.port = port;

    }
    
    public InetAddress getIP()

    {

    	return IPAddress;

    }
    
    public Calendar getCal()
    {
    	return cal;
    }
    
    public int getPort()
    {
    	return port;
    }
}
