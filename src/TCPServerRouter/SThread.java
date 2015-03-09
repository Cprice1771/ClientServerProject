import java.io.*;
import java.net.*;
import java.lang.Exception;

	
public class SThread extends Thread 
{
	private Object [][] RTable; // routing table
	private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
        private BufferedReader in; // reader (for reading from the machine connected to)
	private String inputLine, outputLine, destination, addr; // communication strings
	private Socket outSocket; // socket for communicating with a destination
	private int ind; // indext in the routing table
        private DataInputStream dis = null;
        private DataOutputStream os = null;

	// Constructor
	SThread(Object [][] Table, Socket toClient, int index) throws IOException
	{
            dis = new DataInputStream(toClient.getInputStream());
            out = new PrintWriter(toClient.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
            RTable = Table;
            addr = toClient.getInetAddress().getHostAddress();
            RTable[index][0] = addr; // IP addresses 
            RTable[index][1] = toClient; // sockets for communication
            ind = index;
	}
	
	// Run method (will run for each machine that connects to the ServerRouter)
	public void run()
	{
            try
            {
		// Initial sends/receives
		destination = in.readLine(); // initial read (the destination for writing)
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
		
                //initial send
                inputLine = in.readLine();
                System.out.println("Client/Server said: " + inputLine);
                outputLine = inputLine;
                if (outSocket != null)
                {				
                    outTo.println(outputLine); // writes to the destination
                }
                        
                        
                if((inputLine = in.readLine()).toLowerCase().equals("image"))
                {
                    outputLine = inputLine; // passes the input from the machine to the output string for the destination
                    System.out.println("Client/Server said: " + inputLine);
                    if (outSocket != null)
                    {				
                        outTo.println(outputLine); // writes to the destination
                    }
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
                }
                else
                {
                    outputLine = inputLine; // passes the input from the machine to the output string for the destination
                    System.out.println("Client/Server said: " + inputLine);
                    if (outSocket != null)
                    {				
                        outTo.println(outputLine); // writes to the destination
                    }
                    // Communication loop	
                    while ((inputLine = in.readLine()) != null) 
                    {
                        System.out.println("Client/Server said: " + inputLine);
                        if (inputLine.equals("Bye.")) // exit statement
                                            break;
                        outputLine = inputLine; // passes the input from the machine to the output string for the destination

                        if ( outSocket != null)
                        {				
                            outTo.println(outputLine); // writes to the destination
                        }			
                    }// end while
                }
            }// end try
            catch (IOException e) 
            {
               System.err.println("Could not listen to socket.");
               System.exit(1);
            }
	}
}