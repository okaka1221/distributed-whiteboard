package client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;

import whiteboard.PaintCanvas;

public class ClientThForSending extends Thread  {
    private Socket socket ;
    private PaintCanvas canvas;
    
    public ClientThForSending(Socket socket, BufferedImage bi, PaintCanvas canvas) {
        this.socket = socket;
        this.canvas = canvas;
    }
   
    public void run() {
        BufferedImage canvasPaint = canvas.getBuffer();
        System.out.println("sent image testing!");
        DataOutputStream dout;
		try {
			dout = new DataOutputStream(socket.getOutputStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(canvasPaint, "jpg", out);
			byte[] b = out.toByteArray();
			dout.write(b);
        } 
        catch (IOException e) {
			e.printStackTrace();
		}
    }
}
