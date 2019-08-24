package elements.date;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONObject;

import elements.Element;
import helpers.StaticHelpers;
import helpers.UnequalDimensionsException;
import main.MiBand4Editor;

public class AmPm extends Element{
	
	private int x;
	private int y;
	private int imageIndexAm;
	private int imageIndexPm;
	
	public AmPm(int x, int y, int imageIndexAm, int imageIndexPm) throws UnequalDimensionsException, IOException {
		this.x=x;
		this.y=y;
		this.imageIndexAm=imageIndexAm;
		this.imageIndexPm=imageIndexPm;

		String amFormatted = String.format("%04d", imageIndexAm);
		Dimension dAM = StaticHelpers.getImageDimension(new File("data/"+amFormatted+".png"));
		String pmFormatted = String.format("%04d", imageIndexAm);
		Dimension dPM = StaticHelpers.getImageDimension(new File("data/"+pmFormatted+".png"));
		if(!dAM.equals(dPM)) {
			throw new UnequalDimensionsException(dAM+" unequals "+dPM);
		}
	}

	public AmPm(JSONObject jsonObject) {
		x=jsonObject.getInt("TopLeftX");
		y=jsonObject.getInt("TopLeftY");
		imageIndexAm=jsonObject.getInt("ImageIndexAMEN");
		imageIndexPm=jsonObject.getInt("ImageIndexPMEN");
	}

	@Override
	public JPanel getPreview() throws IOException {
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT,0,0);
		JPanel panel = new JPanel();
		JLabel label = StaticHelpers.getJLabelFromImage(imageIndexPm);
		panel.add(label);
		panel.setName(this.getClass().toString());
		panel.setOpaque(false);
		panel.setSize(layout.preferredLayoutSize(panel));
		return panel;
	}

	@Override
	public void setCoords(int x, int y) {
		this.x=x;
		this.y=y;
	}

	@Override
	public String getJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append("    \"DayAmPm\": {\r\n");
		builder.append("      \"TopLeftX\": "+x+",\r\n");
		builder.append("      \"TopLeftY\": "+y+",\r\n");
		builder.append("      \"ImageIndexAMEN\": "+imageIndexAm+",\r\n");
		builder.append("      \"ImageIndexPMEN\": "+imageIndexPm+",\r\n");
		builder.append("      \"ImageIndexAMCN\": "+imageIndexAm+",\r\n");
		builder.append("      \"ImageIndexPMCN\": "+imageIndexPm+"\r\n");
		builder.append("    }");
		return builder.toString();
	}

	@Override
	public File[] getUsedImgs() {
		File[] files = new File[2];
		String absolutePath = MiBand4Editor.currentPath.getAbsolutePath();

		String amFormatted = String.format("%04d", imageIndexAm);
		files[0]=new File(absolutePath+"\\"+amFormatted+".png");
		String pmFormatted = String.format("%04d", imageIndexPm);
		files[1]=new File(absolutePath+"\\"+pmFormatted+".png");
		return files;
	}

	public void changeImageIndexAm(int imageIndex) {
		imageIndexAm=imageIndex;
	}

	public void changeImageIndexPm(int imageIndex) {
		imageIndexPm=imageIndex;
	}

	public Point getLocation() {
		return new Point(x,y);
	}

}
