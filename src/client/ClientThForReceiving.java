package client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.imageio.ImageIO;

import whiteboard.PaintCanvas;

public class ClientThForReceiving extends Thread {
    private Socket socket ;
    private PaintCanvas canvas ;
    
    public ClientThForReceiving(Socket socket, PaintCanvas canvas) {
        this.socket = socket ;
        this.canvas = canvas ;
    }
   
    public void run() {
        BufferedImage image ; 
        PrintWriter pout;
		try {
	  	    while(true) {
                pout = new PrintWriter(socket.getOutputStream());
                pout.print("a");
                DataInputStream in = new DataInputStream(socket.getInputStream());
                byte[] b = new byte[1024];
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                int length = 0;
                while((length=in.read(b)) != -1){
                    bout.write(b, 0, length);
                    ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
                    image = ImageIO.read(bin);
                    System.out.println("----Client Image receiving!!!! image "+image);
                    if(image != null) {
                        canvas.setBuffer(image);
                        canvas.setG2D(image);
                        //canvas.paint(canvas.getGraphics());
                        canvas.repaint();
                    }
			    }
			}
        } 
        catch (IOException e) {
			e.printStackTrace();
		}

    }
}
