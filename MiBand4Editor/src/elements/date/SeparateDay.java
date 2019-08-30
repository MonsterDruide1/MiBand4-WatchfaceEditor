package elements.date;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import org.json.JSONObject;

import display.TopalignedLabel;
import elements.Element;
import elements.basic.Number;
import helpers.StaticHelpers;
import helpers.UnequalDimensionsException;
import main.MiBand4Editor;

public class SeparateDay extends Element{

	Number number;
	int delimiterImageIndex;
	boolean twoDigitsDay;
	
	public SeparateDay(int x, int y, String textAlignment, int spacing, int imageIndex,
			int imageCount, int delimiterImageIndex, boolean twoDigitsDay) throws IOException, UnequalDimensionsException {
		this.delimiterImageIndex=delimiterImageIndex;
		number = new Number(x,y,textAlignment,spacing,imageIndex,imageCount,2);
		String absolutePath = MiBand4Editor.currentPath.getAbsolutePath();
		
		String iFormatted = String.format("%04d", delimiterImageIndex);
		File delimiter = new File(absolutePath+"\\"+iFormatted+".png");
		number.resizeToCoords(number.getBottomRightPoint().x+(number.getSize().width*3)+StaticHelpers.getImageDimension(delimiter).width, number.getBottomRightPoint().y);
		this.twoDigitsDay = twoDigitsDay;
	}

	public SeparateDay(JSONObject fullObj) {
		JSONObject jsonObject = fullObj.getJSONObject("Separate");
		number = new Number(jsonObject.getJSONObject("Day"));
		twoDigitsDay = fullObj.getBoolean("TwoDigitsDay");
	}

	public JPanel getPreview(int w, int h) throws IOException {
		FlowLayout layout = new FlowLayout(FlowLayout.CENTER,number.getSpacing()*2,0);
		layout.setAlignOnBaseline(true);
		JPanel panel = new JPanel(layout);
		TopalignedLabel dayTensPrev = new TopalignedLabel(number.getImage(3));
		TopalignedLabel dayOnesPrev = new TopalignedLabel(number.getImage(1));
		panel.add(dayTensPrev);
		panel.add(dayOnesPrev);
		panel.setName(this.getClass().toString());
		panel.setOpaque(false);
		
		JPanel centerPanel = new JPanel(new GridBagLayout()); //Center horizontally if CENTER is selected
		centerPanel.setOpaque(false);
		centerPanel.add(panel,new GridBagConstraints());
		
		JPanel mainPanel = new JPanel(); //LEFT/CENTER/RIGHT
		BorderLayout mainLayout = new BorderLayout();
		mainPanel.setLayout(mainLayout);
		mainPanel.setOpaque(false);
		mainLayout.setHgap(0);
		mainLayout.setVgap(0);
		
		JPanel belowMainPanel = new JPanel(); //TOP/CENTER/BOTTOM
		BorderLayout belowMainLayout = new BorderLayout();
		belowMainPanel.setLayout(belowMainLayout);
		belowMainLayout.setHgap(0);
		belowMainPanel.setOpaque(false);
		belowMainLayout.setVgap(0);
		
		Object[] alignment = number.getAlignmentConstraints();
		
		belowMainPanel.add(centerPanel,alignment[0]);
		
		mainPanel.add(belowMainPanel,alignment[1]);
		mainPanel.setName(panel.getName());
		
		if(w!=0 && h!=0) {
			Dimension preferred = layout.preferredLayoutSize(panel);
			Dimension finalD = new Dimension(Math.max(preferred.width, w),Math.max(preferred.height, h));
			mainPanel.setSize(finalD);
		}
		else {
			mainPanel.setSize(layout.preferredLayoutSize(panel));
		}
		
		return mainPanel;
	}

	@Override
	public void setCoords(int x, int y) {
		number.setCoords(x, y);
	}

	@Override
	public String getJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append("    \"MonthAndDay\": {\r\n");
		builder.append("      \"Separate\": {\r\n");
		builder.append("        \"Day\": {\r\n");
		builder.append(number.getJSON());
		builder.append("        }\r\n");
		builder.append("      }\r\n");
		builder.append(",\r\n      \"TwoDigitsDay\": "+twoDigitsDay+"\r\n");
		builder.append("    }\r\n");
		return builder.toString();
	}

	@Override
	public File[] getUsedImgs() {
		File[] files = number.getUsedImgs();
		File[] newFiles = new File[files.length+1];
		for(int i=0;i<files.length;i++) {
			newFiles[i]=files[i];
		}
		String absolutePath = MiBand4Editor.currentPath.getAbsolutePath();
		
		String iFormatted = String.format("%04d", delimiterImageIndex);
		newFiles[newFiles.length-1]=new File(absolutePath+"\\"+iFormatted+".png");
		
		return newFiles;
	}
	
	public boolean getTwoDigitsDay() {
		return twoDigitsDay;
	}

	public void resizeToCoords(int x, int y) {
		number.resizeToCoords(x,y);
	}

	public void changeImageIndex(int imageIndex) {
		number.changeImageIndex(imageIndex);
	}

	public void changeImageCount(int imageCount) {
		number.changeImageCount(imageCount);
	}

	public void changeAlignment(String alignment) {
		number.changeAlignment(alignment);
	}
	
	public void changeSpacing(int spacing) {
		number.changeSpacing(spacing);
	}

	public void chageDelimiterIndex(int delimiterIndex) {
		this.delimiterImageIndex=delimiterIndex;
	}

	@Override
	public JPanel getPreview() throws IOException {
		return getPreview(0,0);
	}

	public Point getBottomRightPoint() {
		return number.getBottomRightPoint();
	}

	public Point getLocation() {
		return number.getLocation();
	}

	public void changeTwoDigitsDay(boolean twoDigitsDay) {
		this.twoDigitsDay=twoDigitsDay;
	}

}
