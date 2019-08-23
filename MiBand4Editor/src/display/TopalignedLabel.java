package display;

import java.awt.Component;

import javax.swing.JLabel;

public class TopalignedLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	
	public TopalignedLabel(JLabel image) {
		super(image.getIcon());
	}

	@Override
	public Component.BaselineResizeBehavior getBaselineResizeBehavior() {
	    return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
	}
	
	@Override
	public int getBaseline(int width, int height) {
		return 0;
	}

}
