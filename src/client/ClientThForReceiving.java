package client;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
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
   
    public void run()  {
		try {
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			while(true) {
				int length = dis.readInt();
				
				if (length > 0) {
					byte[] byteImage = new byte[length];
					dis.read(byteImage);
					ByteArrayInputStream bais = new ByteArrayInputStream(byteImage);
					BufferedImage image = ImageIO.read(bais);
					if (image != null) {
						canvas.setG2D(image);
						canvas.setBuffer(image);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}
