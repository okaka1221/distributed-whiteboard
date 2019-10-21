package server;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.*;
 
public class WhiteBoardServer extends Thread {
	private Map<String,Socket> sockets = new HashMap<String,Socket>();
	
	ServerSocket ss;
	Socket socket;
	DataInputStream dis;
	
	Socket managerSocket = null;
	int managerCount = 0;
	public String manager = null;
	
	private JSONObject json;
	private JSONObject canvasJson = new JSONObject();
	private JSONObject chatboxJson = new JSONObject();
	
	public WhiteBoardServer(String args[]) {
		WhiteBoardServerArgs bean = new WhiteBoardServerArgs();
		
		// Parse parameters.
        CmdLineParser parser = new CmdLineParser(bean);
        
        try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.out.println(e.getMessage());
			parser.printUsage(System.out);
			System.exit(0);
		}
        
        int PORT = bean.getPort();
        
		try {
			System.out.println("WhiteBoard Server started!");
			
			ss = new ServerSocket(PORT);
			
			while(true) {
			    socket = ss.accept();
			    
			    String username = "";
			    boolean isManager = false;
			    
			    InputStream is = socket.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String line;
				
				// get username, and user type
			    while(true) {
			    	while ((line = reader.readLine()) != null) {
			    		JSONObject json = new JSONObject(line);
			    		if (json.getString("header").equals("connect")) {
							username = json.getString("body");
							
							if (json.getString("type").equals("manager")) {
								isManager = true;
							}
						}
			    		break;
			    	}
			    	
			    	if (!username.isEmpty()) {
			    		break;
			    	}
			    }
			    
			    InetAddress ip = socket.getInetAddress();
			    
			    if ((isManager) && (managerCount == 0)) {
			    	managerCount++;
			    	managerSocket = socket;
			    	manager = username;
			    	
			    	System.out.println("Manager connected! IP is " + ip);
			    	System.out.println("Usernaem: " + username);
			    	
			    	ServerRunner msr = new ServerRunner(this, socket, true);
			    	sockets.put(username, socket);
	
			    	Thread thread = new Thread(msr);
		            thread.start();
				    sendUserList();
				    
			    } else if((isManager) && (managerCount != 0)) {
			    	json = new JSONObject();
		            json.put("header", "error");
					json.put("body", "multiple manager");
					
					OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
					writer.write(json.toString() + "\n");
					writer.flush();
			    } else if((!isManager) && (managerCount == 0)) {
			    	json = new JSONObject();
		            json.put("header", "error");
					json.put("body", "manager absent");
					
					OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
					writer.write(json.toString() + "\n");
					writer.flush();
			    } else if (!sockets.containsKey(username)){
			    	System.out.println("New Client connected! IP is" + ip);
			    	System.out.println("Usernaem: " + username);
			    	
			    	ServerRunner sr = new ServerRunner(this, socket, false);
			    	
			    	sockets.put(username, socket);
		       	   	Thread thread = new Thread(sr);
		       	   	thread.start();
		       	   	
			    	json = new JSONObject();
		            json.put("header", "join");
					json.put("body", username);
					
					OutputStreamWriter writer = new OutputStreamWriter(managerSocket.getOutputStream(), "UTF-8");
					writer.write(json.toString() + "\n");
					writer.flush();
			    } else {
		    		JSONObject json = new JSONObject();
		            json.put("header", "error");
					json.put("body", "duplicate username");
					
					OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
					writer.write(json.toString() + "\n");
					writer.flush();
			    }
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
   }
   
   public static void main(String args[])  {
	   new WhiteBoardServer(args) ;
   }
   
   public void sendUserList() {
		for (Socket s: sockets.values()) {
		    List<String> userList = new ArrayList<>(this.sockets.keySet());
		    JSONObject json = new JSONObject();
		    JSONArray jArray = new JSONArray(userList);
		    json.put("header", "name");
			json.put("body", jArray);
			json.put("manager", this.manager);
			
			try {
				OutputStreamWriter writer = new OutputStreamWriter(s.getOutputStream(), "UTF-8");
				writer.write(json.toString() + "\n");
				writer.flush();
			} catch (UnsupportedEncodingException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
   }
   
   public void refreshState() {
	   this.sockets = new HashMap<String,Socket>();
	   this.managerSocket = null;
	   this.managerCount = 0;
	   this.manager = null;
	   this.canvasJson = new JSONObject();
	   this.chatboxJson = new JSONObject();
   }
   
   public Map<String, Socket> getSockets() {
		return this.sockets;
	}

	public void setSockets(Map<String, Socket> sockets) {
		this.sockets = sockets;
	}
	
	public JSONObject getCanvasJson() {
		return this.canvasJson;
	}
	
	public void setCanvasJson(JSONObject canvasJson) {
		this.canvasJson = canvasJson;
	}
	
	public JSONObject getChatbocxJson() {
		return this.chatboxJson;
	}
	
	public void setChatboxJson(JSONObject canvasJson) {
		this.chatboxJson = chatboxJson;
	}
}

