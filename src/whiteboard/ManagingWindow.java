package whiteboard;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.json.JSONObject;

public class ManagingWindow extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel Label1 =new JLabel("please press update to get the new client list");
	private JLabel Label2 =new JLabel("please enter the client name you want to remove");
	private JTextArea show;
    private JButton update;
    private JTextArea enterWord;
    private JButton re;
    private ArrayList<String> clientNameList;
    private Socket socket;

	public ManagingWindow(ArrayList<String> clientNameList, Socket socket)
	{   
		show= new JTextArea(10,30);
		update=new JButton("update");
		enterWord=new JTextArea(2,30);
		re=new JButton("remove");
		add(Label1);
		add(show);
		add(update);
		add(Label2);
		add(enterWord);
		add(re);
		setLayout(new FlowLayout());
		setVisible(true);
		setSize(400,400);
		
		update.addActionListener(this);
		re.addActionListener(this);
	    this.clientNameList=clientNameList;
	    this.socket=socket;
		
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==this.update)
		{String sum="";
		for(String a:clientNameList)
		{
			sum=sum+a+"\n";
			}
		show.setText(sum);}
		else if(e.getSource()==this.re)
		{   
		    
			int index= clientNameList.indexOf(enterWord.getText());  //re.getText()
			if(index<0)
			{
			Label2.setText("Try again");	
			enterWord.setText("");
			}
			else
			{
			Label2.setText("please enter the client name you want to remove");
			System.out.println(index);
			clientNameList.remove(index);
			JSONObject json = new JSONObject();
		    json.put("header", "Remove");
			json.put("body", enterWord.getText());
			OutputStreamWriter writer;
			try {
				
				writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
				writer.write(json.toString() + "\n");
				writer.flush();
				
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
			}
					
		}
		
	}


}
