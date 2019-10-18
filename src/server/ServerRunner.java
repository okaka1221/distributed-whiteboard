package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Map;

import javax.swing.JOptionPane;

import org.json.JSONObject;

public class ServerRunner implements Runnable  {
	private WhiteBoardServer server;
	private Map<String, Socket> sockets;
    private Socket currentSocket;
    private boolean isManger;
    private Socket managerSocket;
   
    public ServerRunner (WhiteBoardServer server, Socket currentSocket, boolean isManager)  {
        this.server = server;
    	this.currentSocket = currentSocket;
    	this.isManger = isManager;
    	
    	if(isManager) {
    		managerSocket = currentSocket;
    	}
    	
    	OutputStreamWriter writer;
		try {
			JSONObject canvasJson = server.getCanvasJson();
			JSONObject chatboxJson = server.getChatbocxJson();
			
			writer = new OutputStreamWriter(currentSocket.getOutputStream(), "UTF-8");
			
			if (!canvasJson.isEmpty()) {
				writer.write(canvasJson.toString() + "\n");
				writer.flush();
	    	} 
			
			if (!chatboxJson.isEmpty()) {
				writer.write(chatboxJson.toString() + "\n");
				writer.flush();
	    	}
			
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
    	
    }
   
    public void run()  {
		try {
			InputStream is = currentSocket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String line;
			while ((line = reader.readLine()) != null) {
				
				JSONObject json = new JSONObject(line);
				if (json.getString("header").length() > 0 && json.getString("body").length() > 0) {
					this.sockets = server.getSockets();
					
					if (json.getString("header").equals("accept")) {
						String username = json.getString("body");
						Socket socket = sockets.get(username);
						
						json.put("header", "permission");
						json.put("body", "accept");
						
						OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
						writer.write(json.toString() + "\n");
						writer.flush();
						
						server.sendUserList();
					}
					
					if (json.getString("header").equals("reject")) {
						String username = json.getString("body");
						Socket socket = sockets.get(username);
						
						json.put("header", "permission");
						json.put("body", "reject");
						
						OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
						writer.write(json.toString() + "\n");
						writer.flush();
						
						sockets.remove(username);
					}
					
					if (json.getString("header").equals("quit")) {
						server.refreshState();
						json.put("header", "quit");
						json.put("body", "null");
					}
					
					if (json.getString("header").equals("canvas")) {
						server.setCanvasJson(json);
					}
					
					if (json.getString("header").equals("chatbox")) {
						server.setChatboxJson(json);
					}
					
					if (json.getString("header").equals("remove")) {
						String username = json.getString("body");
						Socket socket = sockets.get(username);
						
						json.put("header", "close");
						json.put("body", "null");
						
						OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
						writer.write(json.toString() + "\n");
						writer.flush();
						
						sockets.get(username).close();
						sockets.remove(username);
						server.setSockets(sockets);
						server.sendUserList();
					}
					
					if (json.getString("header").equals("exit")) {
						String username = json.getString("body");
						
						sockets.get(username).close();
						sockets.remove(username);
						server.setSockets(sockets);
						server.sendUserList();
					}
					
					for (Socket s: sockets.values()) {
						OutputStreamWriter writer = new OutputStreamWriter(s.getOutputStream(), "UTF-8");
						writer.write(json.toString() + "\n");
						writer.flush();
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
    }
}