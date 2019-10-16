package client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.JTextArea;

import org.json.JSONObject;

import whiteboard.PaintCanvas;

public class ManagerRunner extends ClientRunner {
	
private ArrayList<String> clientNameList =new ArrayList<String>();        //manager hold all client names
	

	public ManagerRunner(Socket socket, PaintCanvas canvas, JTextArea conteArea, String name) 
	{
		super(socket, canvas, conteArea,name);
		
	}
	
	
	public void run()
	{
				
	try {
		
		InputStream is = getSocket().getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		
		String line;
		while ((line = reader.readLine()) != null) {
			
			JSONObject json = new JSONObject(line);
			
			//handle image
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
			
			//handle chat content
			if (json.getString("header").equals("chatbox")) {
				String message = json.getString("body");
				System.out.println(message.length());
				if (message.length() > 0) {
					System.out.println(message);
					getContentArea().append(message);
					getContentArea().append("\n");
				}
			}        
			
			//add the new client name to client list
			if (json.getString("header").equals("Name"))
			{   
				clientNameList.add(json.getString("body"));
				System.out.println(clientNameList);
				
			}
			if (json.getString("header").equals("close"))
			{
				System.out.println("manager successfully receive close command");
			    int index = clientNameList.indexOf(json.getString("body"));     //get the index of client name in client name list
				clientNameList.remove(json.getString("body"));     //remove the client name in client name list
				
				//send message to server to remove the socket from socket list and close the socket
				JSONObject json1 = new JSONObject(); 
			    json1.put("header", "Remove");         
				json1.put("body", index+"");
				OutputStreamWriter writer;
				try {
					
					writer = new OutputStreamWriter(this.getSocket().getOutputStream(), "UTF-8");
					writer.write(json1.toString() + "\n");
					writer.flush();
					
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
			
	}
	
	public ArrayList<String> getClientNameList()
	{
		return this.clientNameList;
	}

}
