package server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import javax.imageio.ImageIO;

public class ServerRunner implements Runnable  {
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