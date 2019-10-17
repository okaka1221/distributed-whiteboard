package server;

import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
 
public class WhiteBoardServer extends Thread {
	private Map<String,Socket> sockets = new HashMap<String,Socket>(); //Connections of all clients.
	ServerSocket ss;
	Socket socket;
	DataInputStream dis;
	Socket managerSocket = null;
	int managerCount = 0;
	public String manager = null;
	
	public WhiteBoardServer() throws IOException, SQLException, ClassNotFoundException, Exception {
		ss = new ServerSocket(8000);
		System.out.println("WhiteBoard Server started!");
		
		while(true) {
		    socket = ss.accept();
		    
		    String username = "";
		    boolean isManager = false;
		    
		    InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line;
			
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
		    	throw new IOException("A manager already exists! Please join as a client!");
		    } else if((!isManager) && (managerCount == 0)) {
		    	throw new IOException("No manager available! Please join as a manager!");
		    } else if (!sockets.containsKey(username)){
		    	System.out.println("New Client connected! IP is" + ip);
		    	System.out.println("Usernaem: " + username);
		    	ServerRunner sr = new ServerRunner(this, socket, false);
		    	
		    	sockets.put(username, socket);
	       	   	Thread thread = new Thread(sr);
	       	   	thread.start();
	       	   	
		    	JSONObject json = new JSONObject();
	            json.put("header", "join");
				json.put("body", username);
				OutputStreamWriter writer = new OutputStreamWriter(managerSocket.getOutputStream(), "UTF-8");
				writer.write(json.toString() + "\n");
				writer.flush();
		    } else {
	    		JSONObject json = new JSONObject();
	            json.put("header", "error");
				json.put("body", "duplicate");
				OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
				writer.write(json.toString() + "\n");
				writer.flush();
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
   
   public void sendUserList() {
		for (Socket s: sockets.values()) {
		    List<String> userList = new ArrayList<>(this.sockets.keySet());
		    JSONObject json = new JSONObject();
		    JSONArray jArray = new JSONArray(userList);
		    json.put("header", "name");
			json.put("body", jArray);       //send the size information to WhiteBoradClient
			json.put("manager", this.manager);
			OutputStreamWriter writer;
			try {
				writer = new OutputStreamWriter(s.getOutputStream(), "UTF-8");
				writer.write(json.toString() + "\n");
				writer.flush();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
   }
   
   public void refreshState() {
	   this.sockets = new HashMap<String,Socket>();
	   this.managerSocket = null;
	   this.managerCount = 0;
	   this.manager = null;
   }
   
   public Map<String, Socket> getSockets() {
		return sockets;
	}

	public void setSockets(Map<String, Socket> sockets) {
		this.sockets = sockets;
	}
}

