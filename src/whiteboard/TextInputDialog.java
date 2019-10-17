package whiteboard;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class TextInputDialog extends JDialog implements ActionListener {
	/**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel textPanel;
    private JPanel buttonPanel;
    private JTextField textField;
    private JButton button;
	
    private PaintCanvas parentCanvas;
    public String text;
    private Point inputPoint;
	
	public TextInputDialog(PaintCanvas canvas, Point point) {
		parentCanvas = canvas;
		inputPoint = point;
		
		setTitle("Text");
		textPanel = new JPanel();
	    textPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
	    
	    textField = new JTextField(30);
	    textPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	    textPanel.add(textField);
	    
	    buttonPanel = new JPanel();
	    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	    button = new JButton("OK");
	    button.addActionListener(this);
	    
	    buttonPanel.add(button);
	
	    getContentPane().setLayout(new GridLayout(2,1));
	    getContentPane().add(textPanel);
	    getContentPane().add(buttonPanel);
	
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    setSize(400, 100);
	    setLocationRelativeTo(null);
	    setLocation(point.x, point.y);
	    setResizable(false);
	    setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		text = textField.getText().toString();
		
		try {
			Graphics2D _g2d = parentCanvas.getG2D();
			_g2d.setFont(new Font("Serif", Font.PLAIN, 24));
			_g2d.drawString(text, inputPoint.x, inputPoint.y);
			parentCanvas.repaint();
			parentCanvas.setIsModified(true);
			parentCanvas.sendBufferImage();
		} catch (NullPointerException e1) {
			System.out.println(e1.getMessage());
		}
		
		dispose();
	}
}
