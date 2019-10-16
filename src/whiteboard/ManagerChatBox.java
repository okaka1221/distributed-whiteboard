package whiteboard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.json.JSONObject;

import client.ManagerRunner;

public class ManagerChatBox extends ChatBox {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton newButton;
	private ManagerRunner manager;

	public ManagerChatBox(Socket socket, JTextArea contentArea, ManagerRunner manager, String name) {
	super(socket,contentArea, name);
	this.manager= manager;
	
	
	newButton= new JButton("manage the client");
	
	add(newButton);
	
	
	newButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			hundleAction(e);
        }
	});
	
	
	
	

}
	
	void hundleAction(ActionEvent e)
	{
		if(e.getSource()== this.getBtnNewButton())
		{   
			String str = this.getInputArea().getText();
			
			if (str.length() == 0) {
				return;
			}
			
	        SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss") ;
	        String time  = formater.format(new Date() ) ;
	        String sendStr = this.getName()+" "+time+" send: "+str ;
	        
	        try  {
	        	JSONObject json = new JSONObject();
				json.put("header", "chatbox");
				json.put("body", sendStr);
				
				OutputStreamWriter writer = new OutputStreamWriter(getSocket().getOutputStream(), "UTF-8");
				writer.write(json.toString() + "\n");
				writer.flush();
	        }catch(Exception e1)  {
	            e1.printStackTrace();
	        }
	        this.getInputArea().setText("");
		}
		
		else if (e.getSource()==this.newButton)
		{
			new ManagingWindow(manager.getClientNameList(),this.getSocket());
			
		}
		
		
		
	}
	
}