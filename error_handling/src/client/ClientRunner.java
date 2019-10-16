package client;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.JTextArea;

import org.json.JSONObject;
import org.omg.CORBA.portable.OutputStream;

import whiteboard.PaintCanvas;

public class ClientRunner implements Runnable {
    private Socket socket ;
    private PaintCanvas canvas;
    private JTextArea contentArea;
    private String name;        //every client has name and we need to store it
    
    public ClientRunner(Socket socket, PaintCanvas canvas, JTextArea  conteArea,String name) {
        this.socket = socket;
        this.canvas = canvas;
        this.contentArea = conteArea;
        this.name= name;
        
    }
   
    public void run()  {
		try {
			
			InputStream is = getSocket().getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String line;
			while ((line = reader.readLine()) != null) {
				
				System.out.println(line);
				JSONObject json = new JSONObject(line);
				
				// normal clients handle image
				if (json.getString("header").equals("canvas")) {
					String encodedImage = json.getString("body");
					
					if (encodedImage.length() > 0) {
						byte[] imageBytes = Base64.getDecoder().decode((encodedImage));
						ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
						BufferedImage image = ImageIO.read(bais);
						if (image != null) {
							getCanvas().setG2D(image);
							getCanvas().setBuffer(image);
						}
					}
				} 
				
				// normal clients handle chat content
				if (json.getString("header").equals("chatbox")) {
					String message = json.getString("body");
					System.out.println(message.length());
					if (message.length() > 0) {
						System.out.println(message);
						getContentArea().append(message);
						getContentArea().append("\n");
					}
				}
				
				//normal clients do not need to handle client name list
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    
    public Socket getSocket()
    {
    	return this.socket;
    }
    
    public PaintCanvas getCanvas()
    {
    	return this.canvas;
    }
    
    public JTextArea getContentArea()
    {
    	return this.contentArea;
    }
    
    public String getClientName()
    {
    	return this.name;
    }
    
}
