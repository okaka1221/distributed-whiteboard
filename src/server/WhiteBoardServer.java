package server;

import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.*;
 
public class WhiteBoardServer extends Thread {
	private List<Socket> sockets = new ArrayList<Socket>();
	private JSONObject canvasJson = new JSONObject();
	private JSONObject chatboxJson = new JSONObject();
	
	public WhiteBoardServer() throws IOException, SQLException, ClassNotFoundException, Exception {
		ServerSocket ss = new ServerSocket(8000) ;
		System.out.println("WhiteBoard Server started!") ;
		while(true) {
		    Socket socket = ss.accept() ;
		    sockets.add(socket) ;
		    InetAddress ip = socket.getInetAddress();
		    System.out.println("New Client connected! IP is"+ip) ;
            Thread thread = new Thread(new ServerRunner(this, socket));
            thread.start();
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

