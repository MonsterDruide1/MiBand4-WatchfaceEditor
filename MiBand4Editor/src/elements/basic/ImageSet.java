package elements.basic;

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

public class ImageSet extends Element{
	
	private int x;
	private int y;
	private int imageIndex;
	private int imagesCount;
	
	public ImageSet(int x,int y,int imageIndex,int imagesCount, boolean ignoreDimension) throws IOException, UnequalDimensionsException {
		if(!ignoreDimension) {
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
		}
		
		this.x=x;
		this.y=y;
		this.imageIndex=imageIndex;
		this.imagesCount=imagesCount;
	}

	public ImageSet(JSONObject jsonObject) {
		x=jsonObject.getInt("X");
		y=jsonObject.getInt("Y");
		imageIndex=jsonObject.getInt("ImageIndex");
		imagesCount=jsonObject.getInt("ImagesCount");
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
	
	public void changeImageIndex(int newImageIndex) {
		imageIndex = newImageIndex;
	}
	
	public void changeImageCount(int newImageCount) {
		imagesCount = newImageCount;
	}

	@Override
	public JPanel getPreview() throws IOException { //FIXME UNUSED
		return null;
	}

	@Override
	public void setCoords(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	@Override
	public String getJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append("        \"X\": "+x+",\r\n");
		builder.append("        \"Y\": "+y+",\r\n");
		builder.append("        \"ImageIndex\": "+imageIndex+",\r\n");
		builder.append("        \"ImagesCount\": "+imagesCount+"\r\n");
		return builder.toString();
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

	public Point getLocation() {
		return new Point(x,y);
	}

	public JLabel getLastImage() throws IOException {
		int realNo = imageIndex+imagesCount-1;
		return StaticHelpers.getJLabelFromImage(realNo);
	}

}
