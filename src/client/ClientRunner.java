package client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.JTextArea;

import org.json.JSONObject;

import whiteboard.PaintCanvas;

public class ClientRunner extends Thread {
    private Socket socket;
    private PaintCanvas canvas;
    private JTextArea contentArea;
    
    public ClientRunner(Socket socket, PaintCanvas canvas, JTextArea  conteArea) {
        this.socket = socket;
        this.canvas = canvas;
        this.contentArea = conteArea;
    }
   
    public void run()  {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);
				if (json.getString("header").equals("canvas")) {
					String encodedImage = json.getString("body");
					
					if (encodedImage.length() > 0) {
						byte[] imageBytes = Base64.getDecoder().decode((encodedImage));
						ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
						BufferedImage image = ImageIO.read(bais);
						if (image != null) {
							canvas.setG2D(image);
							canvas.setBuffer(image);
						}
					}
				} if (json.getString("header").equals("chatbox")) {
					String message = json.getString("body");
					System.out.println(message.length());
					if (message.length() > 0) {
						System.out.println(message);
                        contentArea.append(message);
                        contentArea.append("\n");
					}
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
}
