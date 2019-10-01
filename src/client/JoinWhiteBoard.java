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
	
	private JoinWhiteBoard() {
		try {
			socket = new Socket("localhost", 8000);
			canvas = new PaintCanvas(socket);
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeBoolean(false);
			contentArea = new JTextArea();
			ChatBox chatbox = new ChatBox(socket, contentArea);
			Whiteboard whiteboard = new Whiteboard(canvas, chatbox, false);
			whiteboard.setVisible(true);
			Thread thread = new Thread(new ClientRunner(socket, canvas, contentArea, false));
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
            new JoinWhiteBoard() ;
        } 
        catch(Exception e)  {
            e.printStackTrace();
        }
    }
}
