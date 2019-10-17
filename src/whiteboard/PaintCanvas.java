package whiteboard;

import java.awt.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.json.*;

import java.util.Base64;

public class PaintCanvas extends Canvas implements MouseListener, MouseMotionListener {

    private static final long serialVersionUID = 1L;
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
	// position for free draw and eraser 
	private Point _currPoint = null;
	private Point _prevPoint = null;
	// position for drawing shapes
	private Point _startPoint = null;
	private Point _endPoint = null;
	// width and height for shapes
	private int _shapeWidth = 0;
	private int _shapeHeight = 0;
	private BufferedImage _buffer;
	private Graphics2D _g2d;
	private BasicStroke _pen = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private Color _color = Color.BLACK;
	private boolean _isModified = false;
	BufferedImage prev;
	private Boolean isSend = false;
	
	private Socket socket;
	
	public PaintCanvas(Socket socket) {
		this.socket = socket;
		setBackground(Color.WHITE);
		addMouseListener((MouseListener) this);
        addMouseMotionListener((MouseMotionListener) this);
        repaint();
	}
	
	// draw shape or erase according to _type
	public void paint(Graphics g) {
		super.paint(g);
		// if buffer image is null, create initial buffer image
		if (_buffer == null) {
			int w = getWidth();
			int h = getHeight();
			_buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			_g2d = (Graphics2D) _buffer.createGraphics();
	        _g2d.setColor(Color.WHITE);
	        _g2d.fillRect(0, 0, w, h);
		} 		
		_g2d.setColor(_color);
		_g2d.setStroke(_pen);
		switch(_type) {
            
            case NONE:
				break;
            
            case FREEDRAW:
				try {
					_g2d.drawLine(_prevPoint.x, _prevPoint.y, _currPoint.x, _currPoint.y);
					_isModified = true;
				} catch (NullPointerException e) {
					System.out.println(e.getMessage());
				}
				break;
				
			case ERASE:
				try {
					_g2d.setColor(Color.WHITE);
					_g2d.drawLine(_prevPoint.x, _prevPoint.y, _currPoint.x, _currPoint.y);
					_isModified = true;
				} catch (NullPointerException e) {
					System.out.println(e.getMessage());
				}
				break;
				
			case TEXTBOX:
				break;
				
			case LINE:
				try {
					_g2d.drawLine(_startPoint.x, _startPoint.y, _endPoint.x, _endPoint.y);
					_isModified = true;
				} catch (NullPointerException e) {
					System.out.println(e.getMessage());
				}
				break;
				
			case CIRCLE:
				try {
					_g2d.drawOval(_startPoint.x, _startPoint.y, _shapeWidth, _shapeWidth);
					_isModified = true;
				} catch (NullPointerException e) {
					System.out.println(e.getMessage());
				}
				break;
				
			case RECTANGLE:
				try {
					_g2d.drawRect(_startPoint.x, _startPoint.y, _shapeWidth, _shapeHeight);
					_isModified = true;
				} catch (NullPointerException e) {
					System.out.println(e.getMessage());
				}
				break;
			
			case OVAL:
				try {
					_g2d.drawOval(_startPoint.x, _startPoint.y, _shapeWidth, _shapeHeight);
					_isModified = true;
				} catch (NullPointerException e) {
					System.out.println(e.getMessage());
				}
				break;
				
			default:
				_g2d.drawString("Error", 10, 10);
		}
		
		g.drawImage(_buffer, 0, 0, null);
		
		if (isSend) {
			sendBufferImage();
			isSend = false;
		}
	}
	
	public boolean getIsModified() {
		return _isModified;
	}
	
	public void setIsModified(boolean modified) {
		_isModified = modified;
	}
	
	public void setType(int type) {
		_type = type;
	}
	
	public int getType() {
		return _type;
	}
	
	public void setColor(Color color) {
		_color = color;
	}
	
	public void setPenSize(int size) {
		_pen = new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	}
	
	public void setBuffer(BufferedImage img) {
        _buffer = img;
        repaint();
	}
	
	public BufferedImage getBuffer() {
		return _buffer;
	}
	
	public Graphics2D getG2D() {
		return _g2d;
	}
	
	public void setG2D(BufferedImage img) {
		_g2d = (Graphics2D) img.createGraphics();
	}
	
	// set width and height of shape
	private void setShapeSize() {
		if (_startPoint.x > _endPoint.x) {
			_shapeWidth = _startPoint.x - _endPoint.x;
			_startPoint.x = _endPoint.x;
        } 
        else {
			_shapeWidth = _endPoint.x - _startPoint.x;
		}
		if (_startPoint.y > _endPoint.y) {
			_shapeHeight = _startPoint.y - _endPoint.y;
			_startPoint.y = _endPoint.y;
        } 
        else {
			_shapeHeight= _endPoint.y - _startPoint.y;
		}
	}
	
	public void resetState(int prevType) {
		_type = prevType;
		_currPoint = null;
		_prevPoint = null;
		_startPoint = null;
		_endPoint = null;
		_shapeWidth = 0;
		_shapeHeight = 0;
	}
	
	public void sendBufferImage() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(_buffer, "jpg", baos);
			byte[] imageBytes = baos.toByteArray();
			String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
			
			JSONObject json = new JSONObject();
			json.put("header", "canvas");
			json.put("body", encodedImage);
			
			OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			writer.write(json.toString() + "\n");
			writer.flush();
        } 
        catch (IOException e) {
        	JOptionPane.showMessageDialog(null, "Connection Failed", "Error", JOptionPane.ERROR_MESSAGE);
        	System.out.println(e.getMessage());
        	System.exit(0);
		}
		
	}

	public void mouseDragged(MouseEvent e) {
		_prevPoint = _currPoint;
		_currPoint = e.getPoint();
		_endPoint = e.getPoint();
		if (_type == 0 || _type == 1) {
			repaint();
		}
	}

	public void mousePressed(MouseEvent e) {
		_currPoint = e.getPoint();
		_prevPoint = _currPoint;
		_startPoint = e.getPoint();
	}

	public void mouseReleased(MouseEvent e) {
		isSend = true;
		_endPoint = e.getPoint();
		repaint();
		
		if (_type == 2) {
			TextInputDialog textInputDialog = new TextInputDialog(this ,e.getPoint());
        } else if (_type == 4 || _type == 5 || _type == 6) {
        	setShapeSize();
        }
	}
	
	public void mouseMoved(MouseEvent e) {
		
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {	
		
	}
}
