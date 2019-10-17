package client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

public class ManagerRunner extends Thread {
    private Socket socket;
    private UserList userlist;
    private PaintCanvas canvas;
    private JTextArea contentArea;
    
    public ManagerRunner(Socket socket, UserList userlist, PaintCanvas canvas, JTextArea  conteArea) {
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
				
				if (json.getString("header").equals("join")) {
					String username = json.getString("body");
					
					String[] options = {"Accept", "Reject"};
					int ans = JOptionPane.showOptionDialog(null,  '"'+ username + '"' +  " is trying to join whiteboard?",  "Confirm", 
															JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
					
					JSONObject jsonOut = new JSONObject();
					
					if (ans == 0) {
						jsonOut.put("header", "accept");
						jsonOut.put("body", username);
					} else {
						jsonOut.put("header", "reject");
						jsonOut.put("body", username);
					}
					
					OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
					writer.write(jsonOut.toString() + "\n");
					writer.flush();
				} 
				
				if (json.getString("header").equals("error")) {
					String message = json.getString("body");
					if (message.equals("multiple manager")) {
						JOptionPane.showMessageDialog(null, "A manager already exists. Please join as a client.", "Error", JOptionPane.ERROR_MESSAGE);
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
                        contentArea.append(message);
                        contentArea.append("\n");
					}
				}
				
				if (json.getString("header").equals("name")) {
					JSONArray jArray = json.getJSONArray("body");
					List<String> userList = new ArrayList<String>();
					
					for (int i = 0; i < jArray.length(); i++) {
						userList.add(jArray.getString(i));
					}
					
					userlist.setManagerName(json.getString("manager"));
					userlist.setNameList(userList);
					userlist.revalidate();
					userlist.repaint();
				}
			}
		} catch (IOException e) {
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
