package server;

import java.net.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import java.io.*;
 
public class WhiteBoardServer extends Thread {
	private Map<String,Socket> sockets = new HashMap<String,Socket>();//Connections of all clients.
	private JSONObject canvasJson = new JSONObject();
	private JSONObject chatboxJson = new JSONObject();
	private String manager = null;
	
	public WhiteBoardServer() throws IOException, SQLException, ClassNotFoundException, Exception {
		ServerSocket ss = new ServerSocket(8000) ;
		System.out.println("WhiteBoard Server started!") ;
		
		while(true) {
		    Socket socket = ss.accept() ;
		    ServerRunner sr = new ServerRunner(this, socket);
		    Thread thread = new Thread(sr);
            thread.start();
           
            if(sr.getOp() != null) {
            	sockets.put(sr.getName(), socket);
            }
		    InetAddress ip = socket.getInetAddress();
            
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

