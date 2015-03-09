   import java.io.*;
   import java.net.*;

    public class TCPServerImage {
       public static void main(String[] args) throws IOException 
       {
            String routerName = "172.16.20.121"; // ServerRouter host name
            int SockNum = 5555; // port number
            String address = "10.5.3.196"; // destination IP (Client)
           
             // Variables for setting up connection and communication
            Socket Socket = null; // socket to connect with ServerRouter
            DataInputStream dis = null;
            InetAddress addr = InetAddress.getLocalHost();
            String host = addr.getHostAddress(); // Server machine's IP
            PrintWriter out = null; // for writing to ServerRouter
            BufferedReader in = null; // for reading form ServerRouter
            
            FileOutputStream fout = new FileOutputStream("image.jpg");
			
            // Tries to connect to the ServerRouter
            try 
            {
                Socket = new Socket(routerName, SockNum);
                out = new PrintWriter(Socket.getOutputStream(), true);
                dis = new DataInputStream(Socket.getInputStream());
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
            String fromServer; // messages sent to ServerRouter
            String fromClient; // messages received from ServerRouter      
            
			
            // Communication process (initial sends/receives)
            out.println(address);// initial send (IP of the destination Client)
            fromClient = in.readLine();// initial receive from router (verification of connection)
            System.out.println("ServerRouter: " + fromClient);
            
            //Initial connection from client
            fromClient = in.readLine();
            fromServer = fromClient.toUpperCase(); // converting received message to upper case
            System.out.println("Server said: " + fromServer);
            out.println(fromServer); // sending the converted message back to the Client via ServerRouter
            
            int i;
            // Communication while loop
            while ((i = dis.read()) > -1) 
            {
                 fout.write(i);
            }
            
            out.close();
            in.close();
            // closing connections
            dis.close();
            Socket.close();
      }
   }
