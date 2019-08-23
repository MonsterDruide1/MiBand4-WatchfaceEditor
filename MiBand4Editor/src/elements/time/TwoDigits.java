package elements.time;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONObject;

import elements.Element;
import elements.basic.ImageSet;
import helpers.StaticHelpers;
import helpers.UnequalDimensionsException;

public class TwoDigits extends Element{

	ImageSet tens;
	ImageSet ones;
	Dimension d = null;
	
	public TwoDigits(int x, int y, int imageIndex, int imageCount, boolean ignoreDimension) throws UnequalDimensionsException, IOException{
		tens = new ImageSet(x,y,imageIndex,imageCount,ignoreDimension);
		String imageIndexFormatted = String.format("%04d", imageIndex);
		d = StaticHelpers.getImageDimension(new File("data/"+imageIndexFormatted+".png"));
		ones = new ImageSet(x+d.width,y,imageIndex,imageCount,ignoreDimension);
	}

	public TwoDigits(JSONObject jsonObject) {
		tens = new ImageSet(jsonObject.getJSONObject("Tens"));
		d = tens.getSize();
		ones = new ImageSet(jsonObject.getJSONObject("Ones"));
	}

	public JPanel getPreview(int no) throws IOException {
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT,0,0);
		JPanel panel = new JPanel(layout);
		JLabel tenPrev = tens.getImage(Integer.parseInt(""+(""+no).toCharArray()[0]));
		JLabel onePrev = ones.getImage(Integer.parseInt(""+(""+no).toCharArray()[1]));
		panel.add(tenPrev);
		panel.add(onePrev);
		panel.setName(this.getClass().toString());
		panel.setOpaque(false);
		
		panel.setSize(layout.preferredLayoutSize(panel));
		
		return panel;
	}

	@Override
	public JPanel getPreview() throws IOException {
		return getPreview(0);
	}
	
	public void changeImageIndex(int newImageIndex) throws IOException {
		tens.changeImageIndex(newImageIndex);
		ones.changeImageIndex(newImageIndex);
		String iFormatted = String.format("%04d", newImageIndex);
		d = StaticHelpers.getImageDimension(new File("data/"+iFormatted+".png"));
		setCoords(tens.getX(),tens.getY());
	}

	@Override
	public void setCoords(int x, int y) {
		tens.setCoords(x, y);
		ones.setCoords(x+d.width, y);
	}

	@Override
	public String getJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append("      \"Tens\": {\r\n");
		builder.append(tens.getJSON());
		builder.append("      },\r\n");
		builder.append("      \"Ones\": {\r\n");
		builder.append(ones.getJSON());
		builder.append("      }\r\n");
		return builder.toString();
	}

	public void changeImageCount(int imageCount) {
		ones.changeImageCount(imageCount);
		tens.changeImageCount(imageCount);
	}

	@Override
	public File[] getUsedImgs() {
		File[] ten = tens.getUsedImgs();
		File[] one = ones.getUsedImgs();
		
		File[] both = new File[ten.length+one.length];
		int i=0;
		for(File f : ten) {
			both[i]=f;
			i++;
		}
		for(File f : one) {
			both[i]=f;
			i++;
		}
		return both;
	}

	public Point getLocation() {
		return tens.getLocation();
	}
	
}
