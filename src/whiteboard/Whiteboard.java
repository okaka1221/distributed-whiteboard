package whiteboard;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.BevelBorder; 

public class Whiteboard extends JFrame implements ActionListener {
	PaintCanvas canvas;
	
	private JToggleButton freedrawButton;
	private JToggleButton eraseButton;
	private JToggleButton textButton;
	private JToggleButton lineButton;
	private JToggleButton circleButton;
	private JToggleButton rectangleButton;
	private JToggleButton ovalButton;
	
	private JLabel sizeLabel;
	private JLabel colorLabel;
	
	private int _size = 2;
	private Color _color = Color.BLACK;
	
	private JComboBox<String> sizeCombo;
	private JButton colorButton;
	private JButton chatButton;
	
	JMenuBar menu;
	private JMenuItem newMenu;
	private JMenuItem openMenu;
	private JMenuItem saveMenu;
	private JMenuItem saveAsMenu;
	private JMenuItem exitMenu;
	
	
	public Whiteboard() {
		setTitle("Distributed Whiteboard");
		setResizable(false);
		setSize(800, 600);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(0);
		getContentPane().add(splitPane);
		
		Container drawContainer = new Container();
		splitPane.setRightComponent(drawContainer);
		
		canvas = new PaintCanvas();
		canvas.setBounds(10, 10, 615, 535);
		drawContainer.add(canvas);
		
		JPanel toolsPanel = new JPanel();
		toolsPanel.setLayout(new GridLayout(12, 1));
		splitPane.setLeftComponent(toolsPanel);
		
		ButtonGroup group = new ButtonGroup();
		group.add(freedrawButton);
		group.add(eraseButton);
		group.add(textButton);
		group.add(lineButton);
		group.add(circleButton);
		group.add(rectangleButton);
		group.add(ovalButton);
		
		freedrawButton = new JToggleButton("Freedraw");
		freedrawButton.setActionCommand("freedraw");
		freedrawButton.addActionListener(this);
		toolsPanel.add(freedrawButton);
		
		eraseButton = new JToggleButton("Erase");
		eraseButton.setActionCommand("erase");
		eraseButton.addActionListener(this);
		toolsPanel.add(eraseButton);
		
		sizeLabel = new JLabel("Pen and Eraser Size");
		toolsPanel.add(sizeLabel);
		
		sizeCombo = new JComboBox<String>();
		sizeCombo.setModel(new DefaultComboBoxModel<String>(new String[] {"2", "4", "6", "8", "10"}));
		sizeCombo.setSelectedIndex(0);
		toolsPanel.add(sizeCombo);
		
		sizeCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				_size = Integer.parseInt(sizeCombo.getSelectedItem().toString());
			}
		});
		
		colorButton = new JButton("Color");
		colorButton.setActionCommand("color");
		colorButton.addActionListener(this);
		toolsPanel.add(colorButton);
		
		colorLabel = new JLabel("");
        colorLabel.setOpaque(true);
        colorLabel.setBackground(_color);
        toolsPanel.add(colorLabel);
		
		textButton = new JToggleButton("Text Box");
		textButton.setActionCommand("textbox");
		textButton.addActionListener(this);
		toolsPanel.add(textButton);
		
		lineButton = new JToggleButton("Line");
		lineButton.setActionCommand("line");
		lineButton.addActionListener(this);
		toolsPanel.add(lineButton);
		
		circleButton = new JToggleButton("Circle");
		circleButton.setActionCommand("circle");
		circleButton.addActionListener(this);
		toolsPanel.add(circleButton);
		
		rectangleButton = new JToggleButton("Rectangle");
		rectangleButton.setActionCommand("rectangle");
		rectangleButton.addActionListener(this);
		toolsPanel.add(rectangleButton);
		
		ovalButton = new JToggleButton("Oval");
		ovalButton.setActionCommand("oval");
		ovalButton.addActionListener(this);
		toolsPanel.add(ovalButton);
		
		chatButton = new JButton("Open Chat Window");
		chatButton.setActionCommand("chat");
		chatButton.addActionListener(this);
		toolsPanel.add(chatButton);
		
		menu = new JMenuBar();
		setJMenuBar(menu);
		
		newMenu = new JMenuItem("New");
		newMenu.setActionCommand("new");
		newMenu.addActionListener(this);
		menu.add(newMenu);
		
		openMenu = new JMenuItem("Open");
		openMenu.setActionCommand("open");
		openMenu.addActionListener(this);
		menu.add(openMenu);
		
		saveMenu = new JMenuItem("Save");
		saveMenu.setActionCommand("save");
		saveMenu.addActionListener(this);
		menu.add(saveMenu);
		
		saveAsMenu = new JMenuItem("Save As");
		saveAsMenu.setActionCommand("saveas");
		saveAsMenu.addActionListener(this);
		menu.add(saveAsMenu);
		
		exitMenu = new JMenuItem("Exit");
		exitMenu.setActionCommand("exit");
		exitMenu.addActionListener(this);
		menu.add(exitMenu);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
    			closingAction();
    		}
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
	}
	
	private void closingAction() {
		int ans = JOptionPane.showConfirmDialog(
				null, 
				"Do you want to exit?", 
				"Confirm",
				JOptionPane.YES_NO_OPTION);
		
		if (ans == JOptionPane.YES_OPTION) {
			System.out.println("Server is closed.");
			System.exit(0);
		}
	}
}
