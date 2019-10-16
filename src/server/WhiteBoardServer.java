package server;

import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import java.io.*;
 
public class WhiteBoardServer extends Thread {
	private List<Socket> sockets = new ArrayList<Socket>();
	private JSONObject canvasJson = new JSONObject();
	private JSONObject chatboxJson = new JSONObject();
	ServerSocket ss;
	Socket socket;
	DataInputStream dis;
	Socket managerSocket = null;
	int managerCount = 0;
	
	public WhiteBoardServer() throws IOException, SQLException, ClassNotFoundException, Exception {
		ss = new ServerSocket(8000);
		System.out.println("WhiteBoard Server started!");
		while(true) {
		    socket = ss.accept();
		    sockets.add(socket);
		    dis = new DataInputStream(socket.getInputStream());
		    boolean manager = dis.readBoolean();
		    System.out.println(manager);
		    InetAddress ip = socket.getInetAddress();
		    if ((manager) && (managerCount == 0)) {
		    	managerCount++;
		    	managerSocket = socket;
		    	System.out.println("Manager connected! IP is " + ip);
		    	Thread thread = new Thread(new ServerRunner(this, socket, true));
	            thread.start();
		    }
		    else if((manager) && (managerCount != 0)) {
		    	throw new IOException("A manager already exists! Please join as a client!");
		    }
		    else if((!manager) && (managerCount == 0)) {
		    	throw new IOException("No manager available! Please join as a manager!");
		    }
		    else {
		    	System.out.println("New Client connected! IP is" + ip);
		    	Thread thread = new Thread(new ServerRunner(this, socket, false));
	            thread.start();
		    } 
        }
   }
   
   public static void main(String args[])  {
       try {
           new WhiteBoardServer() ;
       } 
       catch(Exception e)  {
           e.printStackTrace();
       }
   }
   
   public List<Socket> getSocketList() {
	   return this.sockets;
   }
   
   public JSONObject getCanvasJson() {
		return this.canvasJson;
	}
	
	public void setCanvasJson(JSONObject json) {
		this.canvasJson = json;
	}
	
	public JSONObject getChatboxJson() {
		return this.chatboxJson;
	}
	
	public void setChatboxJson(JSONObject json) {
		this.chatboxJson = json;
	}	
}

