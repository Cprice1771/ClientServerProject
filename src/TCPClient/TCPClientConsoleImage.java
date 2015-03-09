/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCPClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Cprice
 */
public class TCPClientConsoleImage {
    public static void main(String[] args) throws IOException 
    {
       String routerName = "172.16.20.21";
       int SockNum = 5555; // port number
       String address ="172.16.20.21"; // destination IP (Server)
       String file = "C:\\Users\\Cprice\\Desktop\\avatar.jpg";
       String LogPath = "C:\\Users\\Cprice\\Desktop\\DistCompLog.txt";

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
        Socket = new Socket(routerName, SockNum);
        out = new PrintWriter(Socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
        os = new DataOutputStream(Socket.getOutputStream());
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


        // Communication process (initial sends/receives
        long initialSend, initialTotalTime, t0, t1, t=0;

        initialSend = System.currentTimeMillis();
        // Communication process (initial sends/receives
        out.println(address);// initial send (IP of the destination Server)
        fromServer = in.readLine();//initial receive from router (verification of connection)
        initialTotalTime = System.currentTimeMillis() - initialSend;
        System.out.println("ServerRouter: " + fromServer);
        out.println(host); // Client sends the IP of its machine as initial send
        t0 = System.currentTimeMillis();

        fromServer = in.readLine();
        System.out.println("Server: " + fromServer);
        
        out.println("image"); // Tells the server were doing images
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
}
