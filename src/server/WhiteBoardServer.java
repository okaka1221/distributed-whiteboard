package server;

import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import java.io.*;
 
public class WhiteBoardServer extends Thread {
	private Map<String,Socket> sockets = new HashMap<String,Socket>(); //Connections of all clients.
	private JSONObject canvasJson = new JSONObject();
	private JSONObject chatboxJson = new JSONObject();
	ServerSocket ss;
	Socket socket;
	DataInputStream dis;
	Socket managerSocket = null;
	int managerCount = 0;
	private String manager = null;
	
	public WhiteBoardServer() throws IOException, SQLException, ClassNotFoundException, Exception {
		ss = new ServerSocket(8000);
		System.out.println("WhiteBoard Server started!");
		
		while(true) {
		    socket = ss.accept();
		    dis = new DataInputStream(socket.getInputStream());
		    boolean manager = dis.readBoolean();
		    
		    String username = "";
		    while (dis.available() > 0) {
		    	String s = String.valueOf(dis.readChar());
		    	username += s;
		    }
		    
		    System.out.println(manager);
		    InetAddress ip = socket.getInetAddress();
		    
		    if ((manager) && (managerCount == 0)) {
		    	managerCount++;
		    	managerSocket = socket;
		    	System.out.println("Manager connected! IP is " + ip);
		    	System.out.println("Usernaem: " + username);
		    	ServerRunner msr = new ServerRunner(this, socket, true);
		    	sockets.put(username, socket);
		    	Thread thread = new Thread(msr);
	            thread.start();
		    }
		    else if((manager) && (managerCount != 0)) {
		    	throw new IOException("A manager already exists! Please join as a client!");
		    }
		    else if((!manager) && (managerCount == 0)) {
		    	throw new IOException("No manager available! Please join as a manager!");
		    }
		    else if (!sockets.containsKey(username)){
		    	System.out.println("New Client connected! IP is" + ip);
		    	System.out.println("Usernaem: " + username);
		    	ServerRunner sr = new ServerRunner(this, socket, false);
		    	sockets.put(username, socket);
		    	System.out.println(sockets.size());
		    	Thread thread = new Thread(sr);
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
   
   public Map<String, Socket> getSockets() {
		return sockets;
	}

	public void setSockets(Map<String, Socket> sockets) {
		this.sockets = sockets;
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

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}
}

