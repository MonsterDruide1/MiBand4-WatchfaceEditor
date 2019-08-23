package elements.date;

import java.awt.Component;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import org.json.JSONObject;

import elements.Element;
import helpers.UnequalDimensionsException;

public class MonthAndDay extends Element{
	
	//SeperateMonthAndDay separate;
	OneLineMonthAndDay oneLine;
	boolean twoDigitsMonth;
	boolean twoDigitsDay;
	
	public MonthAndDay(int x, int y, String textAlignment, int spacing, int imageIndex, int imageCount, int delimiterImageIndex, boolean twoDigitsMonth, boolean twoDigitsDay) throws IOException, UnequalDimensionsException {
		this.twoDigitsDay = twoDigitsDay;
		this.twoDigitsMonth = twoDigitsMonth;
		
		oneLine = new OneLineMonthAndDay(x,y,textAlignment,spacing,imageIndex,imageCount,delimiterImageIndex);
	}

	public MonthAndDay(JSONObject jsonObject) {
		oneLine = new OneLineMonthAndDay(jsonObject.getJSONObject("OneLine"));
		twoDigitsMonth = jsonObject.getBoolean("TwoDigitsMonth");
		twoDigitsDay = jsonObject.getBoolean("TwoDigitsDay");
	}

	@Override
	public JPanel getPreview() throws IOException {
		return oneLine.getPreview();
	}

	@Override
	public void setCoords(int x, int y) {
		oneLine.setCoords(x, y);
	}

	@Override
	public String getJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append("    \"MonthAndDay\": {\r\n");
		builder.append(oneLine.getJSON());
		builder.append(",\r\n      \"TwoDigitsMonth\": "+twoDigitsMonth+",\r\n");
		builder.append("      \"TwoDigitsDay\": "+twoDigitsDay+"\r\n");
		builder.append("    }\r\n");
		return builder.toString();
	}

	@Override
	public File[] getUsedImgs() {
		return oneLine.getUsedImgs();
	}

	public void resizeToCoords(int x, int y) {
		oneLine.resizeToCoords(x,y);
	}

	public void changeImageIndex(int imageIndex) {
		oneLine.changeImageIndex(imageIndex);
	}

	public void changeImageCount(int imageCount) {
		oneLine.changeImageCount(imageCount);
	}

	public void changeAlignment(String alignment) {
		oneLine.changeAlignment(alignment);
	}

	public void changeSpacing(int spacing) {
		oneLine.changeSpacing(spacing);
	}

	public void changeDelimiterIndex(int delimiterIndex) {
		oneLine.chageDelimiterIndex(delimiterIndex);
	}

	public void changeTwoDigitsMonth(boolean twoDigitsMonth) {
		this.twoDigitsMonth=twoDigitsMonth;
	}

	public void changeTwoDigitsDay(boolean twoDigitsDay) {
		this.twoDigitsMonth=twoDigitsDay;
	}

	public Component getPreview(int w, int h) throws IOException {
		return oneLine.getPreview(w,h);
	}

	public Point getBottomRightPoint() {
		return oneLine.getBottomRightPoint();
	}

	public Point getLocation() {
		return oneLine.getLocation();
	}

}
