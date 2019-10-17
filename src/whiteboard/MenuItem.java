package whiteboard;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MenuItem {
	
	PaintCanvas canvas;
	private JFileChooser fileChooser;
	private String fileName;
	
	
	public MenuItem(PaintCanvas canvas) {
		this.canvas = canvas;
		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image (*.jpg)", "jpg", "jpeg"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image (*.png)", "png"));
	}
	
	public void newCanvas() {
		if (canvas.getIsModified()) {
			int res = JOptionPane.showConfirmDialog(canvas, "Save changes to file?", "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION);
			
			if (res == JOptionPane.YES_OPTION) {
				save();
			} else if (res == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		
		canvas.setBuffer(null);
		canvas.resetState(canvas.getType());
		canvas.paint(canvas.getGraphics());
		canvas.repaint();
		canvas.sendBufferImage();
	}
	
	public void open() {
		int res = fileChooser.showOpenDialog(canvas);
		
		try {
			if (res == JFileChooser.APPROVE_OPTION) {
				Image img = ImageIO.read(fileChooser.getSelectedFile());
				canvas.setBuffer((BufferedImage)img);
				canvas.setG2D((BufferedImage)img);
				canvas.paint(canvas.getGraphics());
				canvas.repaint();
			} 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		canvas.setIsModified(false);
		canvas.sendBufferImage();
	}
	
	public void save( ) {
		if (fileName == null || fileName.isEmpty()) {
			int res = fileChooser.showSaveDialog(canvas);
			
			if (res == JFileChooser.APPROVE_OPTION) {
				fileName = fileChooser.getSelectedFile().getAbsolutePath();
			} else if (res == JFileChooser.CANCEL_OPTION) {
				return;
			}
		}
		
		File file = new File(fileName);
		String format = fileName.substring(fileName.lastIndexOf('.') + 1);
		
		if (format.toLowerCase().equals("png")) {
			try {
				ImageIO.write(canvas.getBuffer(), format, file);
				canvas.setIsModified(false);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		} else {
			if (!(format.toLowerCase().equals("jpg") || format.toLowerCase().equals("jpeg"))) {
				file = new File(fileName.concat(".jpg"));
			}
			
			try {
				ImageIO.write(canvas.getBuffer(), "jpg", file);
				canvas.setIsModified(false);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void saveAs( ) {
		fileName = null;
		save();
	}
	
	public void exit( ) {
		if (canvas.getIsModified()) {
			int res = JOptionPane.showConfirmDialog(canvas, "Save changes to file?", "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION);
			
			if (res == JOptionPane.YES_OPTION) {
				save();
				System.exit(0);
			} else if (res == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
		} else {
			System.exit(0);
		}
	}
	
	public void setCanvas(PaintCanvas canvas) {
		this.canvas = canvas;
	}
}
