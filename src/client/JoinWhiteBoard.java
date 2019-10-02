package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JTextArea;
import whiteboard.ChatBox;
import whiteboard.PaintCanvas;
import whiteboard.Whiteboard;

public class JoinWhiteBoard {
	private Socket socket;
	private PaintCanvas canvas;
	private JTextArea contentArea;
	DataOutputStream dos;
	private String HOST, USERNAME;
	private int PORT;
	
	private JoinWhiteBoard() {
		HOST = "localhost";
		PORT = 8000;
		USERNAME = "#TESTCIENT#";
		new JoinWhiteBoard(HOST, PORT, USERNAME);
	}
	
	private JoinWhiteBoard(String HOST, int PORT, String USERNAME) {
		try {
			socket = new Socket("localhost", 8000);
			canvas = new PaintCanvas(socket);
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeBoolean(false);
			dos.flush();
			dos.writeUTF(USERNAME);
			dos.flush();
			contentArea = new JTextArea();
			ChatBox chatbox = new ChatBox(socket, contentArea, USERNAME);
			Whiteboard whiteboard = new Whiteboard(canvas, chatbox, false);
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
