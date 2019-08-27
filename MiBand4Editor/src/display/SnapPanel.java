package display;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.JSONObject;

import main.MiBand4Editor;

public class SnapPanel extends JPanel implements MouseListener,MouseMotionListener {
	private static final long serialVersionUID = 0L;
	private int snapX = 30;
    private int snapY = 30;
    
    private int snapDelta = 3;

    public boolean dragging = true;
    
    MiBand4Editor main;
    
    Rectangle selection;
    Point anchor;
	public boolean selecting;
	
	public boolean bgNotFoundShown = false;
	
	private Point backgroundCoords;

    public SnapPanel(MiBand4Editor main) throws IOException {
    	super.setName("SnapPanel");
    	this.main=main;
    	addMouseListener(this);
    	addMouseMotionListener(this);
		BufferedReader br = new BufferedReader(new FileReader(new File(MiBand4Editor.currentPath.getAbsolutePath()+"\\watchface.json")));
		String line = "";
		StringBuilder builder = new StringBuilder();
		while((line=br.readLine())!=null) {
			builder.append(line);
		}
		br.close();
		
		JSONObject all = new JSONObject(builder.toString());
		JSONObject image = all.getJSONObject("Background").getJSONObject("Image");
		backgroundCoords = new Point(image.getInt("X"),image.getInt("Y"));
    }

    public int getSnapDelta() {
        return snapDelta;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
		try(FileInputStream stream = new FileInputStream(new File(MiBand4Editor.currentPath.getAbsolutePath()+"/background.png"))){
			BufferedImage image = ImageIO.read(stream);
			Image im = new ImageIcon(image).getImage();
			im = im.getScaledInstance(image.getWidth()*3, image.getHeight()*3, Image.SCALE_SMOOTH);
			stream.close();
			image.flush();
			im.flush();
			System.gc();
			g.drawRect(0, 0, 120*3, 240*3);
	        g.drawImage(im,backgroundCoords.x,backgroundCoords.y,null);
		} catch (FileNotFoundException e) {
			if(!bgNotFoundShown) {
				bgNotFoundShown=true;
				JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_background"), main.getInLang("error_imageNotFound_background_title"), JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_background")+e.getMessage(), main.getInLang("error_io_single_title"), JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
        if(selection!=null) {
        	Graphics2D g2d = (Graphics2D)g;
        	g2d.setColor(new Color(51,193,255,100));
        	g2d.fill(selection);
        	g2d.setColor(new Color(0,120,215,255));
        	g2d.draw(selection);
        	
        	ArrayList<JPanel> selected = new ArrayList<JPanel>();
        	for(Component component : getComponents()) {
        		if(component.getBounds().intersects(selection)) {
        			selected.add((JPanel) component);
        		}
        	}
        	
        	if(!main.ctrlPressed) {
            	main.setSelected(selected.toArray(new JPanel[0]));
        	}
        	else {
        		ArrayList<JPanel> added = new ArrayList<JPanel>();
        		ArrayList<JPanel> removed = new ArrayList<JPanel>();
        		
        		for(JPanel panel : selected) {
        			boolean found = false;
        			for(JPanel sel : main.lastSelected) {
        				if(sel.equals(panel)) {
        					found=true;
        					removed.add(panel);
        				}
        			}
        			if(!found) {
        				added.add(panel);
        			}
        		}
        		
        		List<JPanel> select = new ArrayList<JPanel>(Arrays.asList(main.lastSelected));
        		for(JPanel add : added) {
        			select.add(add);
        		}
        		for(JPanel rem : removed) {
        			select.remove(rem);
        		}
        		
        		main.setPreviewSelected(select.toArray(new JPanel[0]));
        	}
        }

        if (dragging)
            paintSnap(g);
    }

    public void paintSnap(Graphics g) {
        g.setColor(Color.GRAY);

        for (int y = snapY; y < this.getHeight(); y += snapY)
            g.drawLine(0, y, getWidth(), y);
        for (int x = snapX; x < getWidth(); x += snapX)
            g.drawLine(x, 0, x, getHeight());

        g.setColor(Color.BLACK);
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		selection.setBounds((int)Math.min(anchor.x, e.getX()),(int)Math.min(anchor.y,e.getY()),
                (int)Math.abs(e.getX()-anchor.x), (int)Math.abs(e.getY()-anchor.y));
		selecting=true;
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		anchor = e.getPoint();
		selection = new Rectangle(anchor);
		selecting=false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		selection=null;
		main.setSelected(main.selected);
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public void readBGCoords() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(MiBand4Editor.currentPath.getAbsolutePath()+"\\watchface.json")));
		String line = "";
		StringBuilder builder = new StringBuilder();
		while((line=br.readLine())!=null) {
			builder.append(line);
		}
		br.close();
		
		JSONObject all = new JSONObject(builder.toString());
		JSONObject image = all.getJSONObject("Background").getJSONObject("Image");
		backgroundCoords = new Point(image.getInt("X"),image.getInt("Y"));
	}
}
