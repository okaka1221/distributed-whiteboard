package whiteboard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class Whiteboard extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JToggleButton freedrawButton  = new JToggleButton("Freedraw");
	private JToggleButton lineButton = new JToggleButton("Line");
	private JToggleButton circleButton = new JToggleButton("Circle");
	private JToggleButton rectangleButton  = new JToggleButton("Rectangle");
	private JToggleButton ovalButton = new JToggleButton("Oval");
	private JToggleButton eraseButton = new JToggleButton("Erase");
	private JToggleButton textButton = new JToggleButton("Text");
	private JButton colorButton = new JButton("Color");
	private JButton chatButton = new JButton("Open Chat Window");
	
	private JMenuItem newMenu;
	private JMenuItem openMenu;
	private JMenuItem saveMenu;
	private JMenuItem saveAsMenu;
	private JMenuItem closeMenu;
	
	public Whiteboard() {
		setTitle("Distributed WHiteboard");
		setSize(800, 600);
		setResizable(false);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
    			closeAction();
    		}
		});
        
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerSize(0);
        getContentPane().add(splitPane, BorderLayout.CENTER);
        
        JPanel tools = new JPanel();
        tools.setLayout(new GridLayout(9, 1));
        splitPane.setLeftComponent(tools);
        
        ButtonGroup group = new ButtonGroup();
        group.add(freedrawButton);
        group.add(lineButton);
        group.add(circleButton);
        group.add(rectangleButton);
        group.add(ovalButton);
        group.add(eraseButton);
        group.add(textButton);
        
        colorButton.addActionListener(this);
        colorButton.setActionCommand("color");
        tools.add(colorButton);
        
        freedrawButton.addActionListener(this);
        freedrawButton.setActionCommand("freedraw");
        tools.add(freedrawButton, BorderLayout.NORTH);
        
        lineButton.addActionListener(this);
        lineButton.setActionCommand("line");
        tools.add(lineButton);
        
        circleButton.addActionListener(this);
        circleButton.setActionCommand("circle");
        tools.add(circleButton);
        
        rectangleButton.addActionListener(this);
        rectangleButton.setActionCommand("rectangle");
        tools.add(rectangleButton);
        
        ovalButton.addActionListener(this);
        ovalButton.setActionCommand("oval");
        tools.add(ovalButton);
        
        eraseButton.addActionListener(this);
        eraseButton.setActionCommand("erase");
        tools.add(eraseButton);
        
        textButton.addActionListener(this);
        textButton.setActionCommand("text");
        tools.add(textButton);
        
        chatButton.addActionListener(this);
        chatButton.setActionCommand("chat");
        tools.add(chatButton);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
	private void closeAction()
	{
		int ans = JOptionPane.showConfirmDialog(
				null, 
				"Do you want to exit?", 
				"Confirm",
				JOptionPane.YES_NO_OPTION);
		
		if (ans == JOptionPane.YES_OPTION) {
			System.out.print("Closing.......");
			System.exit(0);
		}
	}
}
