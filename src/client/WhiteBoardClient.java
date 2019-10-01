package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import whiteboard.PaintCanvas;
import whiteboard.Whiteboard;

public class WhiteBoardClient {
	private Socket socket;
	private PaintCanvas canvas;
	
	private WhiteBoardClient() {
		try {
			socket = new Socket("localhost", 8000);
			canvas = new PaintCanvas(socket);
			Whiteboard whiteboard = new Whiteboard(canvas);
			whiteboard.setVisible(true);
			ClientThForReceiving ctr = new ClientThForReceiving(socket, canvas);
			ctr.start();
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
