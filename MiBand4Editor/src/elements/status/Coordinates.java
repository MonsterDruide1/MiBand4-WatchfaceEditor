package elements.status;

import java.awt.Point;

import org.json.JSONObject;

public class Coordinates {
	
	int x;
	int y;
	int boxWidth;
	String alignment;
	int order;
	
	public Coordinates(int x, int y, int boxWidth, String alignment, int order) {
		this.x=x;
		this.y=y;
		this.boxWidth=boxWidth;
		this.alignment=alignment;
		this.order=order;
	}

	public Coordinates(JSONObject jsonObject) {
		x=jsonObject.getInt("X");
		y=jsonObject.getInt("Y");
		boxWidth=jsonObject.getInt("BoxWidth");
		alignment=jsonObject.getString("Alignment");
		order=jsonObject.getInt("Order");
	}

	public void setCoords(int x, int y) {
		this.x=x;
		this.y=y;
	}

	public String getJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append("      \"Coordinates\": {\r\n");
		builder.append("        \"X\": "+x+",\r\n");
		builder.append("        \"Y\": "+y+",\r\n");
		builder.append("        \"BoxWidth\": "+boxWidth+",\r\n");
		builder.append("        \"Alignment\": \""+alignment+"\",\r\n");
		builder.append("        \"Order\": "+order+",\r\n");
		builder.append("      }");
		return builder.toString();
	}

	public void setAlignment(String alignment) {
		this.alignment=alignment;
	}

	public void setOrder(int order) {
		this.order=order;
	}

	public void setBoxWidth(int boxWidth) {
		this.boxWidth=boxWidth;
	}

	public Point getLocation() {
		return new Point(x,y);
	}

}
