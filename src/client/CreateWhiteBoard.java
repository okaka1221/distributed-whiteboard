package client;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.json.JSONObject;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import whiteboard.ChatBox;
import whiteboard.PaintCanvas;
import whiteboard.UserList;
import whiteboard.Whiteboard;

public class CreateWhiteBoard {
	private Socket socket;
	private PaintCanvas canvas;
	private JTextArea contentArea;
	private String HOST;
	private int PORT;
	private String USERNAME;
	
	private CreateWhiteBoard(String args[]) {
		CreateWhiteBoardArgs bean = new CreateWhiteBoardArgs();
		
		// Parse parameters.
        CmdLineParser parser = new CmdLineParser(bean);
        
        try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.out.println(e.getMessage());
			parser.printUsage(System.out);
			System.exit(0);
		}
        
        this.HOST = bean.getHost();
		this.PORT = bean.getPort();
		this.USERNAME = bean.getUsername();
		
		try {
			socket = new Socket(HOST, PORT);
			canvas = new PaintCanvas(socket);
			contentArea = new JTextArea();
			
			JSONObject json = new JSONObject();
			json.put("header", "connect");
			json.put("body", USERNAME);
			json.put("type", "manager");
			OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			writer.write(json.toString() + "\n");
			writer.flush();
			
			ChatBox chatbox = new ChatBox(socket, contentArea, USERNAME);
			UserList userlist = new UserList(socket, true, USERNAME);
			Whiteboard whiteboard = new Whiteboard(socket, USERNAME, canvas, chatbox, userlist, true);
			whiteboard.setVisible(true);
			
			Thread thread = new Thread(new ManagerRunner(socket, userlist, canvas, contentArea));
			thread.start();
        } catch (UnknownHostException e) {
        	JOptionPane.showMessageDialog(null, "Unknown Host.", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
        } catch (SocketException e){
        	JOptionPane.showMessageDialog(null, "Failed to establish connection.", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
        } catch (IOException e) {
        	JOptionPane.showMessageDialog(null, "Connection Failed", "Error", JOptionPane.ERROR_MESSAGE);
        	System.out.println(e.getMessage());
        }
	}
	
	public static void main(String args[])  {
		new CreateWhiteBoard(args);
    }
}
