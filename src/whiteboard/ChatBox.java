package edu.unimelb.se.ds.Chat;


import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ChatBox extends JFrame {

	private Socket socket;
	private JTextArea inputArea;
	private JTextArea contentArea;
	String input = null;
	private static String name;

	public ChatBox() {
		
		initialize();
		try {
		//	socket = new Socket("localhost", 8888);

		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

		ClientThread thread = new ClientThread(socket, contentArea);
		thread.start();

	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new ChatBox();
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		name = JOptionPane.showInputDialog("Please input your name.");
		this.setBounds(100, 100, 450, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
		this.getContentPane().setLayout(gridBagLayout);

		contentArea = new JTextArea();
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridwidth = 2;
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 0;
		this.getContentPane().add(contentArea, gbc_textArea);

		JButton btnNewButton = new JButton("SEND");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
                String str = inputArea.getText() ;
                SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss") ;
                String time  = formater.format(new Date() ) ;
                String sendStr = name+" "+time+" send: "+str ;
                PrintWriter out = null ;
                try  {
                    out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream() ) ) ;
                    out.println(sendStr) ;
                    out.flush();
                }catch(Exception e1)  {
                    e1.printStackTrace();
                }
                inputArea.setText("");
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

	class ClientThread extends Thread  {
	    private Socket socket ;
	    private JTextArea contentArea ;
	   
	    public ClientThread(Socket socket, JTextArea  conteArea)  {
	        this.socket = socket ;
	        this.contentArea = conteArea ;
	    }
	   
	    public void run()  {
	        BufferedReader br = null ;
	        try  {
	            br = new BufferedReader(new InputStreamReader( socket.getInputStream())) ;
	            String str = null ;
	                    while( (str = br.readLine()) != null)  {
	                        System.out.println(str) ;
	                        contentArea.append(str);
	                        contentArea.append("\n");
	                    }
	        } catch(IOException e)  {
	            e.printStackTrace();
	        } finally  {
	            if(br != null)  {
	                try  {
	                    br.close () ;
	                }catch(IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}}
}
