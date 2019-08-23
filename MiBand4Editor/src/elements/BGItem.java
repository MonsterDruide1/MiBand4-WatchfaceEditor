package elements;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import helpers.StaticHelpers;

public class BGItem extends Element{
	
	Image image;
	File file;
	int i;
	
	int x;
	int y;
	
	public BGItem(File selFile, int i, int x, int y) throws IOException {
		String temp = new File(".").getAbsolutePath();
		String absolutePath = temp.substring(0,temp.length()-1)+"data";
		file = new File(absolutePath+"\\BGItem"+i+".png");
		Files.copy(Paths.get(selFile.getAbsolutePath()), Paths.get(file.getAbsolutePath()),StandardCopyOption.REPLACE_EXISTING);
		
		image = StaticHelpers.getBufferedImage("BGItem"+i);
		this.i=i;
		this.x=x;
		this.y=y;
	}

	public BGItem(String item) throws IOException {
		System.out.println(item);
		x=Integer.parseInt(item.split("\"X\": ")[1].split("\\r?\\n")[0]);
		y=Integer.parseInt(item.split("\"Y\": ")[1].split("\\r?\\n")[0]);
		i=Integer.parseInt(item.split("\"I\": ")[1].split("\\r?\\n")[0]);
		file=new File(item.split("\"File\": ")[1].split("\\r?\\n")[0]);
		image = StaticHelpers.getBufferedImage("BGItem"+i);
		
	}

	@Override
	public JPanel getPreview() throws IOException {
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT,0,0);
		JPanel panel = new JPanel(layout);
		panel.add(new JLabel(new ImageIcon(image)));
		panel.setOpaque(false);
		panel.setName(this.getClass().toString());
		panel.setSize(layout.preferredLayoutSize(panel));
		return panel;
	}

	@Override
	public void setCoords(int x, int y) {
		this.x=x;
		this.y=y;
	}

	@Override
	public String getJSON() { //Not json, own format
		StringBuilder builder = new StringBuilder();
		builder.append("\"X\": "+x+"\r\n");
		builder.append("\"Y\": "+y+"\r\n");
		builder.append("\"I\": "+i+"\r\n");
		builder.append("\"File\": "+file.getAbsolutePath()+"\r\n");
		
		return builder.toString();
	}

	@Override
	public File[] getUsedImgs() { //UNUSED
		return null;
	}

	public void setFile(File file) throws IOException {
		this.file=file;
		Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(this.file.getAbsolutePath()),StandardCopyOption.REPLACE_EXISTING);
		this.image=StaticHelpers.getBufferedImage("BGItem"+i);
	}

	public Image getImage() {
		return image;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public Point getLocation() {
		return new Point(x,y);
	}

}
