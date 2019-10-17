package client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.json.JSONArray;
import org.json.JSONObject;

import whiteboard.PaintCanvas;
import whiteboard.UserList;

public class ClientRunner extends Thread {
	private JoinWhiteBoard client;
    private Socket socket;
    private UserList userlist;
    private PaintCanvas canvas;
    private JTextArea contentArea;
    
    public ClientRunner(JoinWhiteBoard client, Socket socket, UserList userlist, PaintCanvas canvas, JTextArea  conteArea) {
    	this.client = client;
    	this.socket = socket;
    	this.userlist = userlist;
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
				
				if (json.getString("header").equals("permission")) {
					String res = json.getString("body");
					
					if (res.equals("accept")) {
						client.establishWhitebord();
					} else {
						JOptionPane.showMessageDialog(null, "You are rejected by manager.", "Error", JOptionPane.ERROR_MESSAGE);
						socket.close();
						System.exit(0);
					}
				} 
				
				if (json.getString("header").equals("close")) {
					JOptionPane.showMessageDialog(null, "You are removed from whiteboard.", "Error", JOptionPane.ERROR_MESSAGE);
					
					socket.close();	
					System.exit(0);
				}
				
				if (json.getString("header").equals("quit")) {
					JOptionPane.showMessageDialog(null, "Manager closed whiteboard.", "Error", JOptionPane.ERROR_MESSAGE);
					
					socket.close();
					System.exit(0);
				} 
				
				if (json.getString("header").equals("error")) {
					String message = json.getString("body");
					System.out.println(message);
					if (message.equals("manager absent")) {
						JOptionPane.showMessageDialog(null, "No manager available. Please join as a manager.", "Error", JOptionPane.ERROR_MESSAGE);
					} else if (message.equals("duplicate username")) {
						JOptionPane.showMessageDialog(null, "Same username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
					}
					
					socket.close();	
					System.exit(0);
				}
				
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
				} 
				
				if (json.getString("header").equals("chatbox")) {
					String message = json.getString("body");
					
					if (message.length() > 0) {
						System.out.println(message);
                        contentArea.append(message);
                        contentArea.append("\n");
					}
				}
				
				if (json.getString("header").equals("name")) {
					String manager = json.getString("manager");
					JSONArray jArray = json.getJSONArray("body");
					List<String> userList = new ArrayList<String>();
					
					for (int i = 0; i < jArray.length(); i++) {
						userList.add(jArray.getString(i));
					}
					
					userlist.setManagerName(manager);
					userlist.setNameList(userList);
					userlist.revalidate();
					userlist.repaint();
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Connection Failed.", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
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
}
