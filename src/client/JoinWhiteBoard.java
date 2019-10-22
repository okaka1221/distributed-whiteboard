package client;

import java.io.DataOutputStream;
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

public class JoinWhiteBoard {
	private Socket socket;
	
	private PaintCanvas canvas;
	private JTextArea contentArea;
	private ChatBox chatbox;
	private UserList userlist;
	private Whiteboard whiteboard;
	
	DataOutputStream dos;
	private String HOST;
	private int PORT;
	private String USERNAME;
	
	private JoinWhiteBoard(String args[]) {
		JoinWhiteBoardArgs bean = new JoinWhiteBoardArgs();
		
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
			
			JSONObject json = new JSONObject();
			json.put("header", "connect");
			json.put("body", USERNAME);
			json.put("type", "normal");
			OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			writer.write(json.toString() + "\n");
			writer.flush();
			
			contentArea = new JTextArea();
			chatbox = new ChatBox(socket, contentArea, USERNAME);
			userlist = new UserList(null, false, USERNAME);
			whiteboard = new Whiteboard(socket, USERNAME, canvas, chatbox, userlist, false);
			
			Thread thread = new Thread(new ClientRunner(this, socket, userlist, canvas, contentArea));
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
	
	public void establishWhitebord() {
		whiteboard.setVisible(true);
	}
	
	public static void main(String args[])  {
        new JoinWhiteBoard(args);  	
    }
}
