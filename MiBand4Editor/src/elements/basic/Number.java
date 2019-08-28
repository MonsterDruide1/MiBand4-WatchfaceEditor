package elements.basic;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

public class Number extends Element{
	
	int topLeftX;
	int topLeftY;
	int bottomRightX;
	int bottomRightY;
	String alignment;
	int spacing;
	int imageIndex;
	int imagesCount;

	public Number(int x, int y, String textAlignment, int spacing, int imageIndex, int imagesCount,int width) throws IOException, UnequalDimensionsException {
		Dimension d = null;
		for(int i=imageIndex;i<imageIndex+imagesCount;i++) {
			String iFormatted = String.format("%04d", i);
			if(i==imageIndex) {
				d = StaticHelpers.getImageDimension(new File(MiBand4Editor.currentPath+"/"+iFormatted+".png"));
			}
			else {
				Dimension newD = StaticHelpers.getImageDimension(new File(MiBand4Editor.currentPath+"/"+iFormatted+".png"));
				if(!d.equals(newD)) {
					throw new UnequalDimensionsException(d+" unequals "+newD+" at image "+iFormatted+".png");
				}
			}
		}
		this.topLeftX=x;
		this.topLeftY=y;
		this.bottomRightX=x+(d.width)*width;
		this.bottomRightY=y+d.height;
		this.alignment=textAlignment;
		this.spacing=spacing;
		this.imageIndex=imageIndex;
		this.imagesCount=imagesCount;
	}

	public Number(JSONObject jsonObject) {
		topLeftX=jsonObject.getInt("TopLeftX");
		topLeftY=jsonObject.getInt("TopLeftY");
		bottomRightX=jsonObject.getInt("BottomRightX");
		bottomRightY=jsonObject.getInt("BottomRightY");
		alignment=jsonObject.getString("Alignment");
		spacing=jsonObject.getInt("Spacing");
		imageIndex=jsonObject.getInt("ImageIndex");
		imagesCount=jsonObject.getInt("ImagesCount");
	}

	@Override
	public JPanel getPreview() throws IOException { //UNUSED
		return null;
	}

	@Override
	public void setCoords(int x, int y) {
		int changeX = topLeftX-x;
		int changeY = topLeftY-y;
		
		topLeftX-=changeX;
		topLeftY-=changeY;
		bottomRightX-=changeX;
		bottomRightY-=changeX;
	}

	public String getJSON(String title) {
		StringBuilder builder = new StringBuilder();
		if(title!=null) {
			builder.append("        \""+title+"\": {\r\n");
		}
		builder.append("          \"TopLeftX\": "+topLeftX+",\r\n");
		builder.append("          \"TopLeftY\": "+topLeftY+",\r\n");
		builder.append("          \"BottomRightX\": "+bottomRightX+",\r\n");
		builder.append("          \"BottomRightY\": "+bottomRightY+",\r\n");
		builder.append("          \"Alignment\": \""+alignment+"\""+",\r\n");
		builder.append("          \"Spacing\": "+spacing+",\r\n");
		builder.append("          \"ImageIndex\": "+imageIndex+",\r\n");
		builder.append("          \"ImagesCount\": "+imagesCount+",\r\n");
		if(title!=null) {
			builder.append("        }");
		}
		return builder.toString();
	}
	
	@Override
	public String getJSON() {
		return getJSON("Number");
	}

	@Override
	public File[] getUsedImgs() {
		File[] files = new File[imagesCount];
		String absolutePath = MiBand4Editor.currentPath.getAbsolutePath();
		
		for(int i=imageIndex;i<imageIndex+imagesCount;i++) {
			String iFormatted = String.format("%04d", i);
			files[i-imageIndex]=new File(absolutePath+"\\"+iFormatted+".png");
		}
		
		return files;
	}

	public JLabel getImage(int index) throws IOException {
		int realNo = imageIndex+index;
		return StaticHelpers.getJLabelFromImage(realNo);
	}
	
	public Dimension getSize() {
		try {
			return new Dimension(getImage(0).getHeight()*3,getImage(0).getWidth()*3);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void resizeToCoords(int x, int y) {
		bottomRightX=x;
		bottomRightY=y;
	}

	public void changeImageIndex(int imageIndex) {
		this.imageIndex=imageIndex;
	}

	public void changeImageCount(int imageCount) {
		imagesCount=imageCount;
	}
	
	public void changeAlignment(String alignment) {
		this.alignment=alignment;
	}
	
	public void changeSpacing(int spacing) {
		this.spacing=spacing;
	}

	public int getSpacing() {
		return spacing;
	}

	public String getAlignment() {
		return alignment;
	}

	public Object[] getAlignmentConstraints() {
		Object[] o = new Object[2]; //Top/Bottom -> Left/Right
		if(alignment.startsWith("Top")) {
			o[0]=BorderLayout.PAGE_START;
		}else if(alignment.startsWith("Center")) {
			o[0]=BorderLayout.CENTER;
		}else if(alignment.startsWith("Bottom")) {
			o[0]=BorderLayout.PAGE_END;
		}
		
		if(alignment.endsWith("Center")) {
			if(!alignment.equals("VCenter")) {
				o[1]=BorderLayout.CENTER;
			}
		}
		else if(alignment.endsWith("Left")) {
			o[1]=BorderLayout.LINE_START;
		}
		else if(alignment.endsWith("Right")) {
			o[1]=BorderLayout.LINE_END;
		}

		if(o[0]==null) {
			o[0]=BorderLayout.CENTER;
		}
		if(o[1]==null) {
			o[1]=BorderLayout.CENTER;
		}
		return o;
	}

	public Point getBottomRightPoint() {
		return new Point(bottomRightX,bottomRightY);
	}

	public Point getLocation() {
		return new Point(topLeftX,topLeftY);
	}

}
