package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextArea;

import org.json.JSONObject;

import server.ServerRunner;
import whiteboard.ChatBox;
import whiteboard.PaintCanvas;
import whiteboard.Whiteboard;

public class WhiteBoardClient {
	private Socket socket;
	private PaintCanvas canvas;
	private JTextArea contentArea;
	
	private WhiteBoardClient() {
		try {
			socket = new Socket("localhost", 8000);
			canvas = new PaintCanvas(socket);
			contentArea = new JTextArea();
			ChatBox chatbox = new ChatBox(socket, contentArea);
			Whiteboard whiteboard = new Whiteboard(canvas, chatbox);
			whiteboard.setVisible(true);
			Thread thread = new Thread(new ClientRunner(socket, canvas, contentArea));
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
