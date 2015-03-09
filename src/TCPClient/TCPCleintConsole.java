   import java.io.*;
   import java.net.*;

    public class TCPCleintConsole {
       public static void main(String[] args) throws IOException 
       {
           String routerName = "172.16.20.21";
           int SockNum = 5555; // port number
           String address ="172.16.20.21"; // destination IP (Server)
           String file = "C:\\Users\\Cprice\\Desktop\\DistComputingData\\file-1000.txt";
           String LogPath = "C:\\Users\\Cprice\\Desktop\\DistComputingData\\DistCompLog.txt";
           
           FileWriter logFileWriter = new FileWriter(LogPath);
           BufferedWriter logger = new BufferedWriter(logFileWriter);
            // Variables for setting up connection and communication
            Socket Socket = null; // socket to connect with ServerRouter
            PrintWriter out = null; // for writing to ServerRouter
            BufferedReader in = null; // for reading form ServerRouter
            InetAddress addr = InetAddress.getLocalHost();
            String host = addr.getHostAddress(); // Client machine's IP
            // ServerRouter host name
            	
			
            // Tries to connect to the ServerRouter
            try {
            Socket = new Socket(routerName, SockNum);
            out = new PrintWriter(Socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
            } 
            catch (UnknownHostException e) {
               System.err.println("Don't know about router: " + routerName);
               System.exit(1);
            } 
            catch (IOException e) {
               System.err.println("Couldn't get I/O for the connection to: " + routerName);
               System.exit(1);
            }
				
            // Variables for message passing	
            Reader reader = new FileReader(file); 
            BufferedReader fromFile =  new BufferedReader(reader); // reader for the string file
            String fromServer; // messages received from ServerRouter
            String fromUser; // messages sent to ServerRouter
			
            long t0, t1, t=0;
			
            // Communication process (initial sends/receives
            long initialSend,initialTotalTime;
            initialSend = System.currentTimeMillis();
            // Communication process (initial sends/receives
            out.println(address);// initial send (IP of the destination Server)
            fromServer = in.readLine();//initial receive from router (verification of connection)
            initialTotalTime = System.currentTimeMillis() - initialSend;
            System.out.println("ServerRouter: " + fromServer);
            out.println(host); // Client sends the IP of its machine as initial send
            
            fromServer = in.readLine();
            System.out.println("Server: " + fromServer);
            
            t0 = System.currentTimeMillis();
            
            long cycle = 0;
            long startFileSend,endFileSend,totalFileSend=0;
            
            out.println("text"); // tells the server were doing text
            startFileSend =  System.nanoTime();
            // Communication while loop
            while ((fromServer = in.readLine()) != null) 
            {
                System.out.println("Server: " + fromServer);
		t1 = System.nanoTime();

                
                fromUser = fromFile.readLine(); // reading strings from a file
                if (fromUser != null) 
                {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser); // sending the strings to the Server via ServerRouter
                }
                else
                    break;
                
                t0 = System.nanoTime();
                t = t0-t1; //calcultates overall recieve time
                cycle++;
                
                if (fromServer.equals("Bye.")) // exit statement
                    break;
                
		System.out.println("Cycle " + cycle + " time: " + t + " ns");
                logger.write(+ cycle + "," + t + "\n");
            }
            
            endFileSend = System.nanoTime();
            totalFileSend = endFileSend - startFileSend;

            System.out.println("File send time: " + totalFileSend + " ns\n");
            logger.write("File send time: " + totalFileSend + " ns\n");
            
            logger.write("\n");
            logger.write("\n");
            logger.close();
            logFileWriter.close(); 
           
	// closing connections
         out.close();
         in.close();
         Socket.close();
      }
   }