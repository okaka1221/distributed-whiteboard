package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextArea;

import org.json.JSONObject;

import server.ServerRunner;
import whiteboard.ChatBox;
import whiteboard.ManagerChatBox;
import whiteboard.PaintCanvas;
import whiteboard.Whiteboard;

public class WhiteBoardClient {
	private Socket socket;
	private PaintCanvas canvas;
	private JTextArea contentArea;
	private String name;     //client and Manager has own name
	
	
	
	
	private WhiteBoardClient() {
		try {
			socket = new Socket("localhost", 8000);
			canvas = new PaintCanvas(socket);
			contentArea = new JTextArea();       //initialize the attributes
			
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String line;
			int size=0;         //size of sockets list in server side, when manager have not connect to sever, the list is empty
			while ((line = reader.readLine()) != null)
			{   JSONObject json = new JSONObject(line);
				if(json.getString("header").equals("SocketSize"))          
				{   
					size=(json.getInt("body"));       //get the size of socket list to determine use manager or client 
					break;
				}
				
			}
			
			
			ChatBox chatbox = null;
			ClientRunner client=null;
			
			if(size>=1)                 //size of socket list lager than one means that there is already a manager.
			
			{
			 client=new ClientRunner(socket, canvas, contentArea,name);
			 chatbox = new ChatBox(socket, contentArea);      //attached to normal client chat box frame
			 
			}
			
			
			if(size==0)           //there is no manager
			{ 
			  client =new ManagerRunner(socket, canvas, contentArea,name);
			  chatbox = new ManagerChatBox(socket, contentArea,(ManagerRunner) client);    //attached to manager chat box frame
			}
			
			Whiteboard whiteboard = new Whiteboard(canvas, chatbox);
			this.name=chatbox.getName();          //get the client and manager name
			whiteboard.setVisible(true);
			
			Thread thread = new Thread(client);
			thread.start();
			
        } 
        catch (UnknownHostException e) {
			e.printStackTrace();
        } 
        catch (IOException e) {
			e.printStackTrace();
        }
		
	}
	
	
	public static void main(String args[])  {
        try {
            new WhiteBoardClient() ;
        } 
        catch(Exception e)  {
            e.printStackTrace();
        }
    }
}


