package elements.activity;

import java.awt.BorderLayout;
import java.awt.Color;
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

public class Distance extends Element{
	
	Number distance;
	int suffixImageIndex;
	int decimalPointImageIndex;
	
	public Distance(int x, int y, String textAlignment, int spacing, int imageIndex, int imagesCount, int suffixImageIndex, int decimalPointImageIndex) throws IOException, UnequalDimensionsException {
		distance = new Number(x,y,textAlignment,spacing,imageIndex,imagesCount,6);
		this.suffixImageIndex=suffixImageIndex;
		this.decimalPointImageIndex=decimalPointImageIndex;
	}

	public Distance(JSONObject jsonObject) {
		distance = new Number(jsonObject.getJSONObject("Number"));
		suffixImageIndex = jsonObject.getInt("SuffixImageIndex");
		decimalPointImageIndex = jsonObject.getInt("DecimalPointImageIndex");
	}

	public JPanel getPreview(int w, int h) throws IOException {
		FlowLayout layout = new FlowLayout(FlowLayout.CENTER,distance.getSpacing()*2,0);
		layout.setAlignOnBaseline(true);
		JPanel panel = new JPanel(layout);
		TopalignedLabel label1 = new TopalignedLabel(distance.getImage(1));
		TopalignedLabel label2 = new TopalignedLabel(distance.getImage(0));
		TopalignedLabel labeldec = new TopalignedLabel(StaticHelpers.getJLabelFromImage(decimalPointImageIndex));
		TopalignedLabel label3 = new TopalignedLabel(distance.getImage(0));
		TopalignedLabel label4 = new TopalignedLabel(distance.getImage(0));
		TopalignedLabel labelsuf = new TopalignedLabel(StaticHelpers.getJLabelFromImage(suffixImageIndex));
		panel.add(label1);
		panel.add(label2);
		panel.add(labeldec);
		panel.add(label3);
		panel.add(label4);
		panel.add(labelsuf);
		panel.setName(this.getClass().toString());
		panel.setOpaque(false);
		
		JPanel centerPanel = new JPanel(new GridBagLayout()); //Center horizontally if CENTER is selected
		centerPanel.setOpaque(false);
		centerPanel.add(panel,new GridBagConstraints());
		
		JPanel mainPanel = new JPanel(); //LEFT/CENTER/RIGHT
		BorderLayout mainLayout = new BorderLayout();
		mainPanel.setLayout(mainLayout);
		mainPanel.setOpaque(false);
		mainPanel.setBackground(Color.CYAN);
		mainLayout.setHgap(0);
		mainLayout.setVgap(0);
		
		JPanel belowMainPanel = new JPanel(); //TOP/CENTER/BOTTOM
		BorderLayout belowMainLayout = new BorderLayout();
		belowMainPanel.setLayout(belowMainLayout);
		belowMainLayout.setHgap(0);
		belowMainPanel.setOpaque(false);
		belowMainLayout.setVgap(0);
		
		Object[] alignment = distance.getAlignmentConstraints();
		
		belowMainPanel.add(centerPanel,alignment[0]);
		
		mainPanel.add(belowMainPanel,alignment[1]);
		mainPanel.setName(panel.getName());
		
		if(w!=0 && h!=0) {
			mainPanel.setSize(new Dimension(w,h));
		}
		else {
			mainPanel.setSize(layout.preferredLayoutSize(panel));
		}
		
		return mainPanel;
	}

	@Override
	public JPanel getPreview() throws IOException {
		return getPreview(0,0);
	}

	@Override
	public void setCoords(int x, int y) {
		distance.setCoords(x, y);
	}

	@Override
	public String getJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append("    \"Distance\": {\r\n");
		builder.append(distance.getJSON());
		builder.append(",\r\n");
		builder.append("      \"SuffixImageIndex\": "+suffixImageIndex+",\r\n");
		builder.append("      \"DecimalPointImageIndex\": "+decimalPointImageIndex);
		builder.append("\r\n    }");
		return builder.toString();
	}

	@Override
	public File[] getUsedImgs() {
		File[] used = distance.getUsedImgs();
		File[] all = new File[used.length+2];
		for(int i=0;i<used.length;i++) {
			all[i]=used[i];
		}
		String absolutePath = MiBand4Editor.currentPath.getAbsolutePath();
		String iFormatted = String.format("%04d", suffixImageIndex);
		all[all.length-2]=new File(absolutePath+"\\"+iFormatted+".png");
		iFormatted = String.format("%04d", decimalPointImageIndex);
		all[all.length-1]=new File(absolutePath+"\\"+iFormatted+".png");
		return all;
	}

	public void changeAlignment(String alignment) {
		distance.changeAlignment(alignment);
	}

	public void changeSpacing(int spacing) {
		distance.changeSpacing(spacing);
	}

	public void changeImageCount(int imageCount) {
		distance.changeImageCount(imageCount);
	}

	public void changeImageIndex(int imageIndex) {
		distance.changeImageIndex(imageIndex);
	}

	public void resizeToCoords(int x, int y) {
		distance.resizeToCoords(x, y);
	}

	public void changeSuffixImageIndex(int noDataImageIndex) {
		this.suffixImageIndex=noDataImageIndex;
	}

	public void changeDecimalPointImageIndex(int decimalPointImageIndex) {
		this.decimalPointImageIndex=decimalPointImageIndex;
	}

	public Point getLocation() {
		return distance.getLocation();
	}

	public Point getBottomRightPoint() {
		return distance.getBottomRightPoint();
	}

}
