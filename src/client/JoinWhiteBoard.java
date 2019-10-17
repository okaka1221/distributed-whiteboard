package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

import org.json.JSONArray;
import org.json.JSONObject;

import whiteboard.ChatBox;
import whiteboard.PaintCanvas;
import whiteboard.UserList;
import whiteboard.Whiteboard;

public class JoinWhiteBoard {
	private Socket socket;
	
	private PaintCanvas canvas;
	private JTextArea contentArea;
	private ChatBox chatbox;
	private UserList userlist;
	private Whiteboard whiteboard;
	
	DataOutputStream dos;
	private String HOST, USERNAME;
	private int PORT;
	private boolean isAccepted;
	
	private JoinWhiteBoard() {
		HOST = "localhost";
		PORT = 8000;
		USERNAME = "#TESTCIENT#";
		new JoinWhiteBoard(HOST, PORT, USERNAME);
	}
	
	private JoinWhiteBoard(String HOST, int PORT, String USERNAME) {
		try {
			this.HOST = HOST;
			this.PORT = PORT;
			this.USERNAME = USERNAME;
			
			socket = new Socket(HOST, PORT);
			canvas = new PaintCanvas(socket);
			
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeBoolean(false);
			dos.writeChars(USERNAME);
			dos.flush();
			
			contentArea = new JTextArea();
			chatbox = new ChatBox(socket, contentArea, USERNAME);
			userlist = new UserList(null, false);
			whiteboard = new Whiteboard(socket, canvas, chatbox, userlist, false);
			
			Thread thread = new Thread(new ClientRunner(this, socket, userlist, canvas, contentArea));
			thread.start();
        } 
        catch (UnknownHostException e) {
			e.printStackTrace();
        } 
        catch (IOException e) {
			e.printStackTrace();
        }
	}
	
	public void establishWhitebord() {
		whiteboard.setVisible(true);
	}
	
	public static void main(String args[])  {
        try {
        	if (args.length == 0)
        		new JoinWhiteBoard();
        	else if (args.length == 3)
        		new JoinWhiteBoard(args[0], Integer.parseInt(args[1]), args[2]);
        	else
        		throw new IOException("Illegal arguments!!!!");
        } 
        catch(Exception e)  {
            e.printStackTrace();
        }
    }
}
