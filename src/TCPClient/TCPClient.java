/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCPClient;

/**
 *
 * @author Cprice
 */
   import java.io.*;
   import java.net.*;

    public class TCPClient {
        
        private String RouterName;
        private String ServerName;
        private int Port;
        private String LogPath;
        
        public TCPClient()
        {
            RouterName = "";
            ServerName = "";
            Port = 5555;
            LogPath = "C:\\Users\\Cprice\\Desktop\\DistCompLog.txt";
        }
        
        public TCPClient(String routerName, String serverName, int port)
        {
            RouterName = routerName;
            ServerName = serverName;
            Port = port;
            LogPath = "C:\\Users\\Cprice\\Desktop\\DistCompLog.txt";
        }
        
        public void setServerName(String serverName)
        {
            ServerName = serverName;
        }
        
        public void setRouterName(String routerName)
        {
            RouterName = routerName;
        }
        
        public void SetRouterPort(int port)
        {
            Port = port;
        }
        
        public void SendImage(String file) throws IOException
        {
           FileWriter logFileWriter = new FileWriter(LogPath);
           BufferedWriter logger = new BufferedWriter(logFileWriter);
           FileInputStream fis = new FileInputStream (file);
           // Variables for setting up connection and communication
           Socket Socket = null; // socket to connect with ServerRouter
           PrintWriter out = null; // for writing to ServerRouter
           BufferedReader in = null; // for reading form ServerRouter
           DataOutputStream os = null;
           DataInputStream dis = null;
           InetAddress addr = InetAddress.getLocalHost();
           String host = addr.getHostAddress(); // Client machine's IP
           // ServerRouter host name


           // Tries to connect to the ServerRouter
            try {
                Socket = new Socket(RouterName, Port);
                out = new PrintWriter(Socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
                os = new DataOutputStream(Socket.getOutputStream());
                dis = new DataInputStream(Socket.getInputStream());
            } 
            catch (UnknownHostException e) {
               System.err.println("Don't know about router: " + RouterName);
               System.exit(1);
            } 
            catch (IOException e) {
               System.err.println("Couldn't get I/O for the connection to: " + RouterName);
               System.exit(1);
            }

            // Variables for message passing	
            Reader reader = new FileReader(file); 
            BufferedReader fromFile =  new BufferedReader(reader); // reader for the string file
            String fromServer; // messages received from ServerRouter
            String fromUser; // messages sent to ServerRouter


            // Communication process (initial sends/receives
            long initialSend, initialTotalTime, t0, t1, t=0;

            initialSend = System.currentTimeMillis();
            // Communication process (initial sends/receives
            out.println(ServerName);// initial send (IP of the destination Server)
            fromServer = in.readLine();//initial receive from router (verification of connection)
            initialTotalTime = System.currentTimeMillis() - initialSend;
            System.out.println("ServerRouter: " + fromServer);
            out.println(host); // Client sends the IP of its machine as initial send
            t0 = System.currentTimeMillis();

            fromServer = in.readLine();
            System.out.println("Server: " + fromServer);
            
            out.println("image"); // Client sends the IP of its machine as initial send
            fromServer = in.readLine();
            System.out.println("Server: " + fromServer);
            long cycle = 0;
            long startFileSend,endFileSend,totalFileSend=0;

            startFileSend =  System.nanoTime();


            int i;
            // Communication while loop
            do 
            {
                if((i = fis.read()) > -1)
                {
                    t1 = System.nanoTime();
                    os.write(i);

                    t0 = System.nanoTime();
                    t = t0-t1; //calcultates overall recieve time
                    cycle++;

                    System.out.println("Cycle " + cycle + " time: " + t + " ns");
                    logger.write(+ cycle + "," + t + "\n");
                }
                else
                    break;
            } while ((i = dis.read()) > -1) ;

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
            os.close();
            Socket.close();
        }
        
        public void SendFile(String file) throws IOException 
        {
      	
            // Variables for setting up connection and communication
            Socket Socket = null; // socket to connect with ServerRouter
            PrintWriter out = null; // for writing to ServerRouter
            BufferedReader in = null; // for reading form ServerRouter
            InetAddress addr = InetAddress.getLocalHost();
            String host = addr.getHostAddress(); // Client machine's IP
            FileWriter logFileWriter = new FileWriter(LogPath);
            BufferedWriter logger = new BufferedWriter(logFileWriter);
             logger.write("New Run for file: " + file + "\n");
			
            // Tries to connect to the ServerRouter
            try 
            {
                Socket = new Socket(RouterName, Port);
                Socket.setKeepAlive(true);
                out = new PrintWriter(Socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
            } 
            catch (UnknownHostException e) 
            {
               System.err.println("Don't know about router: " + RouterName);
               System.exit(1);
            } 
            catch (IOException e) 
            {
               System.err.println("Couldn't get I/O for the connection to: " + RouterName);
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
            out.println(ServerName);// initial send (IP of the destination Server)
            fromServer = in.readLine();//initial receive from router (verification of connection)
            initialTotalTime = System.currentTimeMillis() - initialSend;
            System.out.println("ServerRouter: " + fromServer);
            System.out.println("Initial Communication Time: " + initialTotalTime + " ms");
            logger.write("Initial Communication Time: " + initialTotalTime + " ms \n");
            
            System.out.println("Sending: " + host);
            out.println(host); // Client sends the IP of its machine as initial send
            fromServer = in.readLine();
            System.out.println("Server: " + fromServer);
            t0 = System.currentTimeMillis();
            
            out.println("text"); // Client sends the IP of its machine as initial send
            
            long cycle = 0;
            long startFileSend,endFileSend,totalFileSend=0;
            
            System.out.println("Waiting for response: ");
            while ((fromServer = in.read() + "") != null) 
            //while(true)
            {
                System.out.println("Server: " + fromServer);
                
                t1 = System.currentTimeMillis();
                
                t0  = System.currentTimeMillis();
                t+=t0-t1; //calcultates overall recieve time
                cycle++;
                if (fromServer.equals("Bye.")) // exit statement
                   break;

                System.out.println("Cycle time: " + t);
                logger.write("Cycle time: " + t + "\n");
                
                startFileSend =  System.currentTimeMillis();
                fromUser = fromFile.readLine(); // reading strings from a file

                if (fromUser != null) 
                {
                    if(fromUser.equals("Bye."))
                    break;
                    
                   System.out.println("Client: " + fromUser);
                   out.println(fromUser); // sending the strings to the Server via ServerRouter
                   endFileSend = System.currentTimeMillis();
                   totalFileSend += endFileSend - startFileSend;
                }
                else
                    break;
            }
            
            
            System.out.println("Server cycle time: " + t + "ms  Number of Cycles: " + cycle + "\n");
            logger.write("Server cycle time: " + t + "ms  Number of Cycles: " + cycle + "\n");
            System.out.println("File send time: " + totalFileSend + " ms\n");
            logger.write("File send time: " + totalFileSend + " ms\n");
            
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

