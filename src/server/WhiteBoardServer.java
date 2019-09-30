package server;

import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.io.*;
 
public class WhiteBoardServer extends Thread
{
	private List<Socket> sockets = new ArrayList<Socket>() ; 
       public WhiteBoardServer() throws IOException, SQLException, ClassNotFoundException, Exception
       {
            ServerSocket ss = new ServerSocket(8000) ;
            System.out.println("WhiteBoard Server started!") ;
            while(true) {
                Socket socket = ss.accept() ;
                sockets.add(socket) ;
                InetAddress ip = socket.getInetAddress();
                System.out.println("New Client connected! IP is"+ip) ;
                Thread thread = new Thread(new ServerRunner(sockets,socket)) ;
                thread.start();
            }
       }
       
       public static void main(String args[])  {
           try {
               new WhiteBoardServer() ;
           } 
           catch(Exception e)  {
               e.printStackTrace();
           }
       }
}

