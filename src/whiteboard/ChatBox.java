package whiteboard;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.event.ActionListener;
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

public class ChatBox extends JFrame {

	/**
     *
     */
    private static final long serialVersionUID = -2619377735582365735L;
    private Socket socket;
	private JTextArea inputArea;
	private JTextArea contentArea;
	String input = null;
	private static String name;

	public ChatBox(Socket socket, JTextArea contentArea) {
		this.socket = socket;
		this.contentArea = contentArea;
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		name = JOptionPane.showInputDialog("Please input your name.");
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
		this.getContentPane().add(contentArea, gbc_textArea);

		JButton btnNewButton = new JButton("SEND");
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
		GridBagConstraints gbc_textArea_1 = new GridBagConstraints();
		gbc_textArea_1.insets = new Insets(0, 0, 0, 5);

		gbc_textArea_1.fill = GridBagConstraints.BOTH;
		gbc_textArea_1.gridx = 0;
		gbc_textArea_1.gridy = 2;
		this.getContentPane().add(inputArea, gbc_textArea_1);
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
        }catch(Exception e1)  {
            e1.printStackTrace();
        }
        this.inputArea.setText("");
	}
}
