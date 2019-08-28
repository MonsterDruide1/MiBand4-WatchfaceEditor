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
import helpers.UnequalDimensionsException;
import main.MiBand4Editor;

public class Pulse extends Element{
	
	Number pulse;
	int noDataImageIndex;
	
	public Pulse(int x, int y, String textAlignment, int spacing, int imageIndex, int imagesCount, int noDataImageIndex) throws IOException, UnequalDimensionsException {
		pulse = new Number(x,y,textAlignment,spacing,imageIndex,imagesCount,3);
		this.noDataImageIndex=noDataImageIndex;
	}

	public Pulse(JSONObject jsonObject) {
		pulse = new Number(jsonObject.getJSONObject("Number"));
		noDataImageIndex = jsonObject.getInt("NoDataImageIndex");
	}

	public JPanel getPreview(int w, int h) throws IOException {
		FlowLayout layout = new FlowLayout(FlowLayout.CENTER,pulse.getSpacing()*2,0);
		layout.setAlignOnBaseline(true);
		JPanel panel = new JPanel(layout);
		TopalignedLabel label1 = new TopalignedLabel(pulse.getImage(1));
		TopalignedLabel label2 = new TopalignedLabel(pulse.getImage(0));
		TopalignedLabel label3 = new TopalignedLabel(pulse.getImage(0));
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
		
		Object[] alignment = pulse.getAlignmentConstraints();
		
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
		pulse.setCoords(x, y);
	}

	@Override
	public String getJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append("    \"Pulse\": {\r\n");
		builder.append(pulse.getJSON());
		builder.append(",\r\n");
		builder.append("      \"NoDataImageIndex\": "+noDataImageIndex);
		builder.append("\r\n    }");
		return builder.toString();
	}

	@Override
	public File[] getUsedImgs() {
		File[] used = pulse.getUsedImgs();
		File[] all = new File[used.length+1];
		for(int i=0;i<used.length;i++) {
			all[i]=used[i];
		}
		String absolutePath = MiBand4Editor.currentPath.getAbsolutePath();
		String iFormatted = String.format("%04d", noDataImageIndex);
		all[all.length-1]=new File(absolutePath+"\\"+iFormatted+".png");
		return all;
	}

	public void changeAlignment(String alignment) {
		pulse.changeAlignment(alignment);
	}

	public void changeSpacing(int spacing) {
		pulse.changeSpacing(spacing);
	}

	public void changeImageCount(int imageCount) {
		pulse.changeImageCount(imageCount);
	}

	public void changeImageIndex(int imageIndex) {
		pulse.changeImageIndex(imageIndex);
	}

	public void resizeToCoords(int x, int y) {
		pulse.resizeToCoords(x, y);
	}

	public void changeNoDataImageIndex(int noDataImageIndex) {
		this.noDataImageIndex=noDataImageIndex;
	}

	public Point getLocation() {
		return pulse.getLocation();
	}

	public Point getBottomRightPoint() {
		return pulse.getBottomRightPoint();
	}

}
