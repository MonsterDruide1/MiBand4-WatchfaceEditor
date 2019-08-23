package elements.time;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import org.json.JSONObject;

import elements.Element;
import helpers.UnequalDimensionsException;

public class SecondsGroup extends Element{
	
	TwoDigits seconds;
	
	public SecondsGroup(int x, int y, int imageIndex, int imageCount, boolean ignoreDimension) throws UnequalDimensionsException, IOException{
		seconds = new TwoDigits(x,y,imageIndex,imageCount,ignoreDimension);
	}

	public SecondsGroup(JSONObject jsonObject) {
		seconds = new TwoDigits(jsonObject);
	}

	@Override
	public JPanel getPreview() throws IOException {
		return seconds.getPreview(59);
	}
	
	public void changeImageIndex(int newImageIndex) throws IOException {
		seconds.changeImageIndex(newImageIndex);
	}

	@Override
	public void setCoords(int x, int y) {
		seconds.setCoords(x, y);
	}

	@Override
	public String getJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append("    \"Seconds\": {\r\n");
		builder.append(seconds.getJSON());
		builder.append("    }");
		return builder.toString();
	}

	public void changeImageCount(int imageCount) {
		seconds.changeImageCount(imageCount);
	}

	@Override
	public File[] getUsedImgs() {
		return seconds.getUsedImgs();
	}

	public Point getLocation() {
		return seconds.getLocation();
	}

}
