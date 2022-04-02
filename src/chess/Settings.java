package chess;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Settings extends JPanel {
    
	private int width;
	private int height;
	
	public Settings() {
		this.width = 30;
		this.height = 30;
		this.setPreferredSize(new Dimension(50, 50));
        this.setBackground(Color.DARK_GRAY);
        UIManager.put("ToolTip.background", Color.LIGHT_GRAY);
        UIManager.put("ToolTip.foreground", Color.BLACK);
        UIManager.put("ToolTip.font", new Font("Arial", Font.BOLD, 12));
        this.setToolTipText("Settings");
        
        addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		setBackground(Color.LIGHT_GRAY);
        		width = 30;
        		height = 30;
        		repaint();
        	}
        	
        	@Override
        	public void mouseExited(MouseEvent e) {
        		setBackground(Color.DARK_GRAY);
        		width = 30;
        		height = 30;
        		repaint();
        	}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2D = (Graphics2D)g;
        String imgLocation = "/images/settings.png";
    	URL imageURL = getClass().getResource(imgLocation);
    	ImageIcon image = new ImageIcon(imageURL);
        g2D.drawImage(image.getImage(), (50 - width) / 2, (50 - height) / 2, width, height, null);
	}
	
}
