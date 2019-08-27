package elements.status;

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
import helpers.UnequalDimensionsException;

public class BatteryText extends Element{
	
	Number batteryText;
	
	public BatteryText(int x, int y, String textAlignment, int spacing, int imageIndex, int imagesCount) throws IOException, UnequalDimensionsException {
		batteryText = new Number(x,y,textAlignment,spacing,imageIndex,imagesCount);
	}

	public BatteryText(JSONObject jsonObject) {
		batteryText = new Number(jsonObject);
	}

	public JPanel getPreview(int w, int h) throws IOException {
		FlowLayout layout = new FlowLayout(FlowLayout.CENTER,batteryText.getSpacing()*2,0);
		layout.setAlignOnBaseline(true);
		JPanel panel = new JPanel(layout);
		TopalignedLabel label1 = new TopalignedLabel(batteryText.getImage(1));
		TopalignedLabel label2 = new TopalignedLabel(batteryText.getImage(0));
		TopalignedLabel label3 = new TopalignedLabel(batteryText.getImage(0));
		panel.add(label1);
		panel.add(label2);
		panel.add(label3);
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
		
		Object[] alignment = batteryText.getAlignmentConstraints();
		
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
		batteryText.setCoords(x, y);
	}

	@Override
	public String getJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append(batteryText.getJSON("Text"));
		return builder.toString();
	}

	@Override
	public File[] getUsedImgs() {
		return batteryText.getUsedImgs();
	}

	public void changeAlignment(String alignment) {
		batteryText.changeAlignment(alignment);
	}

	public void changeSpacing(int spacing) {
		batteryText.changeSpacing(spacing);
	}

	public void changeImageCount(int imageCount) {
		batteryText.changeImageCount(imageCount);
	}

	public void changeImageIndex(int imageIndex) {
		batteryText.changeImageIndex(imageIndex);
	}

	public void resizeToCoords(int x, int y) {
		batteryText.resizeToCoords(x, y);
	}

	public Point getLocation() {
		return batteryText.getLocation();
	}

	public Point getBottomRightPoint() {
		return batteryText.getBottomRightPoint();
	}

}
