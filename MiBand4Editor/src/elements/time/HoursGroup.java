package elements.time;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import org.json.JSONObject;

import elements.Element;
import helpers.UnequalDimensionsException;

public class HoursGroup extends Element{
	
	TwoDigits hours;
	
	public HoursGroup(int x, int y, int imageIndex, int imageCount, boolean ignoreDimension) throws UnequalDimensionsException, IOException{
		hours = new TwoDigits(x,y,imageIndex,imageCount,ignoreDimension);
	}

	public HoursGroup(JSONObject jsonObject) {
		hours = new TwoDigits(jsonObject);
	}

	@Override
	public JPanel getPreview() throws IOException {
		return hours.getPreview(23);
	}
	
	public void changeImageIndex(int newImageIndex) throws IOException {
		hours.changeImageIndex(newImageIndex);
	}

	@Override
	public void setCoords(int x, int y) {
		hours.setCoords(x, y);
	}

	@Override
	public String getJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append("    \"Hours\": {\r\n");
		builder.append(hours.getJSON());
		builder.append("    }");
		return builder.toString();
	}

	public void changeImageCount(int imageCount) {
		hours.changeImageCount(imageCount);
	}

	@Override
	public File[] getUsedImgs() {
		return hours.getUsedImgs();
	}

	public Point getLocation() {
		return hours.getLocation();
	}

}
