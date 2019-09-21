package edu.unimelb.se.ds.Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Chat {
	private List<Socket> sockets = new ArrayList<Socket>() ;   
	
    public Chat() throws IOException  {
        ServerSocket ss = new ServerSocket(8888) ;
        System.out.println("Chat Server started!") ;
        
        while(true)  {
            Socket socket = ss.accept() ;
            sockets.add(socket) ;
            InetAddress ip = socket.getInetAddress();
            System.out.println("New Client connected！IP is"+ip) ;
            Thread thread = new Thread(new ServerRunner(sockets,socket)) ;
            thread.start();
        }
    }
 
    public static void main(String args[])  {
        try {
            new Chat() ;
        } catch(Exception e)  {
            e.printStackTrace();
        }
    }

}

class ServerRunner implements Runnable  {
    private List<Socket> sockets ;
    private Socket currentSocket ;  
   
    public ServerRunner (List<Socket> sockets,Socket currentSocket)  {
        this.sockets = sockets ;
        this.currentSocket = currentSocket ;
    }
   
    public void run()  {
        InetAddress ip = currentSocket.getInetAddress();
        BufferedReader br = null ;
        try  {
            br = new BufferedReader(new InputStreamReader(currentSocket.getInputStream())) ;
            String str = null ;
            while((str = br.readLine()) != null)  {
                System.out.println(ip+": "+str) ;
               
                for(Socket temp : sockets)  {
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(temp.getOutputStream())) ;
                    pw.println(str) ;
                    pw.flush();
                }
            }
        }catch(IOException e)  {
            e.printStackTrace();
        }
    }
}
