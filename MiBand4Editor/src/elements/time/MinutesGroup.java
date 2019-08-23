package elements.time;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import org.json.JSONObject;

import elements.Element;
import helpers.UnequalDimensionsException;

public class MinutesGroup extends Element{
	
	TwoDigits minutes;
	
	public MinutesGroup(int x, int y, int imageIndex, int imageCount, boolean ignoreDimension) throws UnequalDimensionsException, IOException{
		minutes = new TwoDigits(x,y,imageIndex,imageCount,ignoreDimension);
	}

	public MinutesGroup(JSONObject jsonObject) {
		minutes = new TwoDigits(jsonObject);
	}

	@Override
	public JPanel getPreview() throws IOException {
		return minutes.getPreview(59);
	}

	public void changeImageIndex(int newImageIndex) throws IOException {
		minutes.changeImageIndex(newImageIndex);
	}

	@Override
	public void setCoords(int x, int y) {
		minutes.setCoords(x, y);
	}

	@Override
	public String getJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append("    \"Minutes\": {\r\n");
		builder.append(minutes.getJSON());
		builder.append("    }");
		return builder.toString();
	}

	public void changeImageCount(int imageCount) {
		minutes.changeImageCount(imageCount);
	}

	@Override
	public File[] getUsedImgs() {
		return minutes.getUsedImgs();
	}

	public Point getLocation() {
		return minutes.getLocation();
	}

}
