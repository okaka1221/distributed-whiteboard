package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JTextArea;

import org.json.JSONObject;

import whiteboard.ChatBox;
import whiteboard.ManagerChatBox;
import whiteboard.PaintCanvas;
import whiteboard.UserList;
import whiteboard.Whiteboard;

public class CreateWhiteBoard {
	private Socket socket;
	private PaintCanvas canvas;
	private JTextArea contentArea;
	private DataOutputStream dos;
	private String HOST, USERNAME;
	private int PORT;
	
	private CreateWhiteBoard() {
		HOST = "localhost";
		PORT = 8000;
		USERNAME = "#TESTMANAGER#11";
		new CreateWhiteBoard(HOST, PORT, USERNAME);
	}
	
	private CreateWhiteBoard(String HOST, int PORT, String USERNAME) {
		try {
			this.HOST = HOST;
			this.PORT = PORT;
			this.USERNAME = USERNAME;
			
			socket = new Socket(HOST, PORT);
			canvas = new PaintCanvas(socket);
			contentArea = new JTextArea();
			
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeBoolean(true);
			dos.writeChars(USERNAME);
			dos.flush();
			
			ChatBox chatbox = new ChatBox(socket, contentArea, USERNAME);
			UserList userlist = new UserList(socket, true);
			Whiteboard whiteboard = new Whiteboard(socket, USERNAME, canvas, chatbox, userlist, true);
			whiteboard.setVisible(true);
			
			Thread thread = new Thread(new ManagerRunner(socket, userlist, canvas, contentArea));
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
			if (args.length == 0)
				new CreateWhiteBoard();
			else if (args.length == 3)
				new CreateWhiteBoard(args[0], Integer.parseInt(args[1]), args[2]);
			else
				throw new IOException("Illegal arguments!!!!");
        } 
        catch(Exception e)  {
            e.printStackTrace();
        }
    }
}
