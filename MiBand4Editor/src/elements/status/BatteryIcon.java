package elements.status;

import java.awt.FlowLayout;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONObject;

import elements.Element;
import elements.basic.ImageSet;
import helpers.UnequalDimensionsException;

public class BatteryIcon extends Element{

	ImageSet set;
	
	public BatteryIcon(int x, int y, int imageIndex, int imageCount, boolean ignoreDimension) throws UnequalDimensionsException, IOException{
		set = new ImageSet(x,y,imageIndex,imageCount,ignoreDimension);
	}
	
	public BatteryIcon(JSONObject jsonObject) {
		set = new ImageSet(jsonObject);
	}

	@Override
	public JPanel getPreview() throws IOException {
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT,0,0);
		JPanel panel = new JPanel(layout);
		JLabel tenPrev = set.getLastImage();
		panel.add(tenPrev);
		panel.setName(this.getClass().toString());
		panel.setOpaque(false);
		
		panel.setSize(layout.preferredLayoutSize(panel));
		
		return panel;
	}
	
	public void changeImageIndex(int newImageIndex) throws IOException {
		set.changeImageIndex(newImageIndex);
		setCoords(set.getX(),set.getY());
	}

	@Override
	public void setCoords(int x, int y) {
		set.setCoords(x, y);
	}

	@Override
	public String getJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append("      \"Icon\": {\r\n");
		builder.append(set.getJSON());
		builder.append("      }");
		return builder.toString();
	}

	public void changeImageCount(int imageCount) {
		set.changeImageCount(imageCount);
	}

	@Override
	public File[] getUsedImgs() {
		return set.getUsedImgs();
	}

	public Point getLocation() {
		return set.getLocation();
	}
	
}
