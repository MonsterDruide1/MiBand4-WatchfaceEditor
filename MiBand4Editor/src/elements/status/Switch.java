package elements.status;

import java.awt.FlowLayout;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import elements.Element;
import helpers.StaticHelpers;
import main.MiBand4Editor;

public class Switch extends Element {
	
	String name;
	Coordinates coordinates;
	int imageIndexOn;
	int imageIndexOff;
	
	public Switch(String name, int x, int y, int boxWidth, String alignment, int order, int imageIndexOn, int imageIndexOff) {
		coordinates = new Coordinates(x,y,boxWidth,alignment,order);
		this.imageIndexOn=imageIndexOn;
		this.imageIndexOff=imageIndexOff;
		this.name=name;
	}

	public Switch(String name, JSONObject obj) {
		this.name=name;
		try {
			imageIndexOn = obj.getInt("ImageIndexOn");
		}
		catch(JSONException e) {
			imageIndexOn = 0;
		}
		try {
			imageIndexOff = obj.getInt("ImageIndexOff");
		}
		catch(JSONException e) {
			imageIndexOff = 0;
		}
		coordinates = new Coordinates(obj.getJSONObject("Coordinates"));
	}

	@Override
	public JPanel getPreview() throws IOException {
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT,0,0);
		JPanel panel = new JPanel(layout);
		if(imageIndexOn!=0) {
			panel.add(StaticHelpers.getJLabelFromImage(imageIndexOn));
		}
		else if(imageIndexOff!=0) {
			panel.add(StaticHelpers.getJLabelFromImage(imageIndexOff));
		}
		panel.setName(this.getClass().toString());
		panel.setOpaque(false);
		panel.setSize(layout.preferredLayoutSize(panel));
		return panel;
	}

	@Override
	public void setCoords(int x, int y) {
		coordinates.setCoords(x,y);
	}

	@Override
	public String getJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append("    \""+name+"\": {\r\n");
		builder.append(coordinates.getJSON());
		if(imageIndexOn!=0) {
			builder.append(",\r\n      \"ImageIndexOn\": "+imageIndexOn);
		}
		if(imageIndexOff!=0) {
			builder.append(",\r\n      \"ImageIndexOff\": "+imageIndexOff);
		}
		builder.append("\r\n    }");
		return builder.toString();
	}

	@Override
	public File[] getUsedImgs() {
		String absolutePath = MiBand4Editor.currentPath.getAbsolutePath();
		File[] files;
		String formattedOn = String.format("%04d", imageIndexOn);
		String formattedOff = String.format("%04d", imageIndexOff);
		if(imageIndexOn!=0) {
			if(imageIndexOff!=0) {
				files = new File[2];
				files[0]=new File(absolutePath+"\\"+formattedOn+".png");
				files[1]=new File(absolutePath+"\\"+formattedOff+".png");
			}
			else {
				files = new File[2];
				files[0]=new File(absolutePath+"\\"+formattedOn+".png");
			}
		}
		else {
			if(imageIndexOff!=0) {
				files = new File[1];
				files[0]=new File(absolutePath+"\\"+formattedOff+".png");
			}
			else {
				files = new File[0];
			}
		}
		return files;
	}

	public void changeImageIndexOff(int imageIndex) {
		imageIndexOff=imageIndex;
	}

	public void changeImageIndexOn(int imageIndex) {
		imageIndexOn=imageIndex;
	}

	public String getName() {
		return name;
	}

	public void changeAlignment(String alignment) {
		coordinates.setAlignment(alignment);
	}

	public void changeOrder(int order) {
		coordinates.setOrder(order);
	}

	public void changeBoxWidth(int boxWidth) {
		coordinates.setBoxWidth(boxWidth);
	}

	public Point getLocation() {
		return coordinates.getLocation();
	}

}
