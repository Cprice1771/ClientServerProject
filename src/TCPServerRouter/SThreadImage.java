import java.io.*;
import java.net.*;
import java.lang.Exception;

	
public class SThreadImage extends Thread 
{
	private Object [][] RTable; // routing table
        private DataInputStream dis = null;
        private DataOutputStream os = null;
        private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
	private String  inputLine, destination, addr; // communication strings
	private Socket outSocket; // socket for communicating with a destination
	private int ind; // indext in the routing table
        private BufferedReader in; // reader (for reading from the machine connected to)

	// Constructor
	SThreadImage(Object [][] Table, Socket toClient, int index) throws IOException
	{
            dis = new DataInputStream(toClient.getInputStream());
            out = new PrintWriter(toClient.getOutputStream(), true);
            RTable = Table;
            addr = toClient.getInetAddress().getHostAddress();
            RTable[index][0] = addr; // IP addresses 
            RTable[index][1] = toClient; // sockets for communication
            ind = index;
            in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
	}
	
	// Run method (will run for each machine that connects to the ServerRouter)
	public void run()
	{
            try
            {
                destination = in.readLine(); // initial read (the destination for writing)
		// Initial sends/receives
		System.out.println("Forwarding to " + destination);
                out.println("Connected to the router."); // confirmation of connection
		
		// waits 10 seconds to let the routing table fill with all machines' information
		try
                {
                    Thread.currentThread().sleep(10000); 
                }
		catch(InterruptedException ie){
                    System.out.println("Thread interrupted");
		}
		
		// loops through the routing table to find the destination
		for ( int i=0; i<10; i++) 
		{
                    if (destination.equals((String) RTable[i][0]))
                    {
			outSocket = (Socket) RTable[i][1]; // gets the socket for communication from the table
                        System.out.println("Found destination: " + destination);
                        os = new DataOutputStream(outSocket.getOutputStream());
                        outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
                    }
                }
		
                inputLine = in.readLine();
                System.out.println("Client/Server said: " + inputLine);
                outTo.println(inputLine); // writes to the destination
                
                int i;
		// Communication loop	
		while ((i = dis.read()) > -1) 
                {
                    System.out.println("Client/Server said: " + i);
				
                    if ( outSocket != null)
                    {				
			os.write(i);
                    }			
                }// end while		 
            }// end try
            catch (IOException e) 
            {
               System.err.println("Could not listen to socket.");
               System.exit(1);
            }
	}
}