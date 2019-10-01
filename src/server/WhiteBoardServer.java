package server;

import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
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
            Thread thread = new Thread(new ServerRunner(socket)) ;
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
   
   public List<Socket> getSocketList() {
	   return this.sockets;
   }
   
   public class ServerRunner implements Runnable  {
	    private List<Socket> sockets ;
	    private Socket currentSocket ;  
	   
	    public ServerRunner (Socket currentSocket)  {
	        this.currentSocket = currentSocket ;
	    }
	   
	    public void run()  {
			try {
				while(true) {
					DataInputStream dis = new DataInputStream(currentSocket.getInputStream());
					int length = dis.readInt();
					
					if (length > 0) {
						byte[] inImage = new byte[length];
						dis.read(inImage);
						
						if (inImage.length > 0) {
							this.sockets = getSocketList();
							for (Socket s: sockets) {
								
								DataOutputStream dos = new DataOutputStream(s.getOutputStream());
								dos.writeInt(inImage.length);
								dos.write(inImage);
								dos.flush();
							}
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
}

