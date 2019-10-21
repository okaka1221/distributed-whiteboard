package whiteboard;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.json.JSONObject;

public class UserList extends JPanel implements ActionListener {

	private List<String> nameList = new ArrayList<String>();
    private Socket managerSocket;
    private boolean isManager;
    private String mananger = "";
    private String username;
	
	public UserList(Socket socket, boolean isManager, String username) {
	    this.managerSocket = socket;
	    this.isManager = isManager;
	    this.username = username;
	    setLayout();
	}
	
	private void setLayout() {
		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(280, 530));
		
		JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        
	    JLabel label = new JLabel("User List");
	    label.setPreferredSize(new Dimension(260, 20));
	    label.setFont (label.getFont ().deriveFont (24.0f));
	    listPanel.add(label);
	    
	    for (String name : nameList) {
	    	if (name.equals(this.username)) {
	    		continue;
	    	}
	    	JSplitPane splitPane = new JSplitPane();
	    	splitPane.setPreferredSize(new Dimension(260, 50));
	    	splitPane.setDividerLocation(180);
	    	splitPane.setDividerSize(0);
	    	
	    	JLabel nameLabel = new JLabel(name);
	    	nameLabel.setPreferredSize(new Dimension(160, 25));
	    	nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    	JScrollPane scrollLabel = new JScrollPane(nameLabel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    	splitPane.setLeftComponent(scrollLabel);
	    	
	    	if (this.isManager && !name.equals(mananger)) {
	    		JButton nameButton = new JButton("Remove");
	    		nameButton.addActionListener(this);
	    		nameButton.setActionCommand(name);
		    	splitPane.setRightComponent(nameButton);
	    	} else {
	    		splitPane.setRightComponent(new JPanel());
	    	}
	    	
	    	listPanel.add(splitPane);
	    }
	    
	    JScrollPane scrollPane = new JScrollPane(listPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);	    
	    mainPanel.add(scrollPane);
	    this.add(mainPanel);
    }
	
	public void setManagerName(String name) {
		this.mananger = name;
	}
	
	public void setNameList(List<String> userList) {
		this.nameList = userList;
		this.removeAll();
		setLayout();
	}
    
    @Override
	public void actionPerformed(ActionEvent e) {
		String name = e.getActionCommand();
		
		JSONObject json = new JSONObject();
	    json.put("header", "remove");
		json.put("body", name);
		OutputStreamWriter writer;
		
		try {
			writer = new OutputStreamWriter(this.managerSocket.getOutputStream(), "UTF-8");
			writer.write(json.toString() + "\n");
			writer.flush();
		} catch (Exception error) {
			JOptionPane.showMessageDialog(null, "Connection Failed", "Error", JOptionPane.ERROR_MESSAGE);
        	System.out.println(error.getMessage());
        	System.exit(0);
		}	
	}
}
