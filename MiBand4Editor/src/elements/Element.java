package elements;

import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

public abstract class Element {

	public abstract JPanel getPreview() throws IOException;
	
	public abstract void setCoords(int x, int y);

	public abstract String getJSON();

	public abstract File[] getUsedImgs();
}
