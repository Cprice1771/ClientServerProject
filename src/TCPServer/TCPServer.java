   import java.io.*;
   import java.net.*;

    public class TCPServer {
       public static void main(String[] args) throws IOException 
       {
            String routerName = "172.16.20.121"; // ServerRouter host name
            int SockNum = 5555; // port number
            String address ="172.16.20.21"; // destination IP (Client)
            String imageLocation = "C:\\Users\\Cprice\\Desktop\\image.jpg";
            
            // Variables for setting up connection and communication
            Socket Socket = null; // socket to connect with ServerRouter
            PrintWriter out = null; // for writing to ServerRouter
            BufferedReader in = null; // for reading form ServerRouter
            DataInputStream dis = null;
            InetAddress addr = InetAddress.getLocalHost();
            String host = addr.getHostAddress(); // Server machine's IP			
            FileOutputStream fout = new FileOutputStream(imageLocation);
			
            // Tries to connect to the ServerRouter
            try {
                Socket = new Socket(routerName, SockNum);
                Socket.setKeepAlive(true);
                out = new PrintWriter(Socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
                dis = new DataInputStream(Socket.getInputStream());
            } 
            catch (UnknownHostException e) 
            {
               System.err.println("Don't know about router: " + routerName);
               System.exit(1);
            } 
            catch (IOException e) {
               System.err.println("Couldn't get I/O for the connection to: " + routerName);
               System.exit(1);
            }
				
            // Variables for message passing			
            String fromServer; // messages sent to ServerRouter
            String fromClient; // messages received from ServerRouter      
            
			
            // Communication process (initial sends/receives)
            out.println(address);// initial send (IP of the destination Client)
            fromClient = in.readLine();// initial receive from router (verification of connection)
            System.out.println("ServerRouter: " + fromClient);
            
            fromClient = in.readLine();// initial receive from client 
            System.out.println("Client said: " + fromClient);

            fromServer = fromClient.toUpperCase(); // converting received message to upper case
            System.out.println("Server said: " + fromServer);
            out.println(fromServer); // sending the converted message back to the Client via ServerRouter
            
            if((fromServer = in.readLine()).equals("image"))
            {
                fromServer = fromClient.toUpperCase(); // converting received message to upper case
                System.out.println("Server said: " + fromServer);
                out.println(fromServer); // sending the converted message back to the Client via ServerRouter
                int i;
                // Communication while loop
                while ((i = dis.read()) > -1) 
                {
                     fout.write(i);
                }
            }
            else
            {
                fromServer = fromClient.toUpperCase(); // converting received message to upper case
                System.out.println("Server said: " + fromServer);
                out.println(fromServer); // sending the converted message back to the Client via ServerRouter
                // Communication while loop
                while ((fromClient = in.readLine()) != null) 
                {
                    System.out.println("Client said: " + fromClient);
                    if (fromClient.equals("Bye.")) // exit statement
                        break;

                    fromServer = fromClient.toUpperCase(); // converting received message to upper case
                    System.out.println("Server said: " + fromServer);
                    out.println(fromServer); // sending the converted message back to the Client via ServerRouter
                }
            }
            
            
            
			
	// closing connections
        out.close();
        in.close();
        dis.close();
        Socket.close();
      }
   }
