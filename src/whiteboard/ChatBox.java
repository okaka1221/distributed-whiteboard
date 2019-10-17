package whiteboard;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

import org.json.JSONObject;

import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class ChatBox extends JFrame {

	private Socket socket;
	private JTextArea inputArea;
	private JTextArea contentArea;
	private String name;
	private JButton btnNewButton;
	String input = null;
	

	public ChatBox(Socket socket, JTextArea contentArea, String USERNAME) {
		this.socket = socket;
		this.name = USERNAME;
		this.setContentArea(contentArea);
	
		this.setBounds(100, 100, 450, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
		this.getContentPane().setLayout(gridBagLayout);
		
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridwidth = 2;
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 0;
		contentArea.setEnabled(false);
		JScrollPane spCA = new JScrollPane(contentArea);
		spCA.setPreferredSize(new Dimension(100,100));
		this.getContentPane().add(spCA, gbc_textArea);

		btnNewButton = new JButton("SEND");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hundleAction();
            }
		});

		JLabel lblNewLabel = new JLabel("User: "+name);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		this.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.SOUTH;
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 2;
		this.getContentPane().add(btnNewButton, gbc_btnNewButton);

		inputArea = new JTextArea();
		JScrollPane spIn = new JScrollPane(inputArea);// add scrollPane to input Area.
		GridBagConstraints gbc_textArea_1 = new GridBagConstraints();
		gbc_textArea_1.insets = new Insets(0, 0, 0, 5);

		gbc_textArea_1.fill = GridBagConstraints.BOTH;
		gbc_textArea_1.gridx = 0;
		gbc_textArea_1.gridy = 2;
		this.getContentPane().add(spIn, gbc_textArea_1);
		
		// Change close action to hide instead of close.
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		setDefaultCloseOperation(ChatBox.HIDE_ON_CLOSE);
    		}
		});
	}
	
	void hundleAction() {
		String str = this.inputArea.getText();
		
		if (str.length() == 0) {
			return;
		}
		
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss") ;
        String time  = formater.format(new Date() ) ;
        String sendStr = name+" "+time+" send: "+str ;
        
        try  {
        	JSONObject json = new JSONObject();
			json.put("header", "chatbox");
			json.put("body", sendStr);
			
			OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			writer.write(json.toString() + "\n");
			writer.flush();
        }catch(Exception e)  {
        	JOptionPane.showMessageDialog(null, "Connection Failed", "Error", JOptionPane.ERROR_MESSAGE);
        	System.out.println(e.getMessage());
			System.exit(0);
        }
        
        this.inputArea.setText("");
	}
	
	public JButton getBtnNewButton() {
		return btnNewButton;
	}
	
	public  String getName() {
		return name;
	}

	public  void setName(String name) {
		this.name = name;
	}

	public Socket getSocket() {
		return this.socket;
	}
	
	public JTextArea getInputArea() {
		return this.inputArea;
	}
	
	public JTextArea getContentArea() {
		return this.contentArea;
	}
	
	public void setSocket(Socket socket) {
		this.socket=socket;
	}
	
	public void setInputArea(JTextArea inputArea) {
		this.inputArea=inputArea;
	}
	
	public void setContentArea(JTextArea contentArea) {
		this.contentArea=contentArea;
	}
	
	public void setInput() {
		this.input = null;
	}
}
