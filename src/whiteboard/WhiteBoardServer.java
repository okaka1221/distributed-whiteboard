package whiteboard;

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
            ServerSocket ss = new ServerSocket(8888) ;
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
 
       class ServerRunner implements Runnable  {
    	    private List<Socket> sockets ;
    	    private Socket currentSocket ;  
    	   
    	    public ServerRunner (List<Socket> sockets,Socket currentSocket)  {
    	        this.sockets = sockets ;
    	        this.currentSocket = currentSocket ;
    	    }
    	   
    	    public void run()  {
    	    	BufferedImage image ;
    	        InetAddress ip = currentSocket.getInetAddress();
    	        PrintWriter pout;
    	        byte[]b = new byte[1024];
				try {
					pout = new PrintWriter(currentSocket.getOutputStream());
					pout.print("a");
					DataInputStream in = new DataInputStream(currentSocket.getInputStream());
					
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					int length = 0;
					
					while((length=in.read(b))!=-1){
						bout.write(b, 0, length);
						System.out.println("Server Image receiving!!!! length "+length);
						ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
						image = ImageIO.read(bin);
					    for(Socket temp : sockets) {
                            DataOutputStream dout = new DataOutputStream(temp.getOutputStream());
                            //ByteArrayOutputStream out = new ByteArrayOutputStream();
                            dout.write(b);
                            System.out.println(" Image sending!!!! dout "+dout);
	                    }
				    }
                } 
                catch (IOException e) {
					e.printStackTrace();
				}
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

