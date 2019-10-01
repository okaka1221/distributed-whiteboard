package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JTextArea;
import whiteboard.ChatBox;
import whiteboard.PaintCanvas;
import whiteboard.Whiteboard;

public class CreateWhiteBoard {
	private Socket socket;
	private PaintCanvas canvas;
	private JTextArea contentArea;
	DataOutputStream dos;
	
	private CreateWhiteBoard() {
		try {
			socket = new Socket("localhost", 8000);
			canvas = new PaintCanvas(socket);
			contentArea = new JTextArea();
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeBoolean(true);
			ChatBox chatbox = new ChatBox(socket, contentArea);
			Whiteboard whiteboard = new Whiteboard(canvas, chatbox, true);
			whiteboard.setVisible(true);
			Thread thread = new Thread(new ClientRunner(socket, canvas, contentArea, true));
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
            new CreateWhiteBoard() ;
        } 
        catch(Exception e)  {
            e.printStackTrace();
        }
    }
}
