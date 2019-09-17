package whiteboard;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import whiteboard.tools.*;

public class PaintCanvas extends Canvas implements MouseListener, MouseMotionListener {
	
	// shape type
	public static final int NONE	  = -1;
    public static final int FREEDRAW  = 0;
    public static final int ERASE	  = 1;
    public static final int TEXTBOX	  = 2;
    public static final int LINE      = 3;
    public static final int CIRCLE	  = 4;
    public static final int RECTANGLE = 5;
    public static final int OVAL 	  = 6;
	
    // current state
	private int _type = -1;
	private int _currX = -1;
	private int _currY = -1;
	private int _prevX = -1;
	private int _prevY = -1;
	
	private BufferedImage _buffer;
	private Graphics2D _g2d;
	
	private BasicStroke _pen;
	private Color _color;
	private static final int SIZE = 800;
	
	public PaintCanvas() {
		setBackground(Color.WHITE);
		_color = Color.BLACK;
		_pen = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		_type = 0;
		
		addMouseListener((MouseListener) this);
		addMouseMotionListener((MouseMotionListener) this);
        
		repaint();
	}
	
	public void paint(Graphics g) {
		if (_buffer == null) {
			int w = getWidth();
			int h = getHeight();
			_buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	        _g2d = (Graphics2D) _buffer.getGraphics();
	        _g2d.setColor(Color.WHITE);
	        _g2d.fillRect(0, 0, w, h);
		} 
		
		_g2d.setColor(_color);
		_g2d.setStroke(_pen);
		
		switch(_type) {
		
			case NONE:
				break;
			
			case FREEDRAW:
				_g2d.drawLine(_prevX, _prevY, _currX, _currY);
				break;
				
			case ERASE:
				break;
				
			case TEXTBOX:
				break;
				
			case LINE:
				break;
				
			case CIRCLE:
				break;
				
			case RECTANGLE:
				break;
			
			case OVAL:
				break;
				
			default:
				_g2d.drawString("Error", 10, 10);
				
		}
		
		g.drawImage(_buffer, 0, 0, null);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		_prevX = _currX;
		_prevY = _currY;
		
		Point point = e.getPoint();
		_currX = point.x;
		_currY = point.y;
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point point = e.getPoint();
		_currX = point.x;
		_currY = point.y;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
