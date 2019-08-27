package display;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import elements.BGItem;
import elements.Element;
import elements.activity.Calories;
import elements.activity.Distance;
import elements.activity.Pulse;
import elements.activity.Steps;
import elements.activity.StepsGoal;
import elements.date.AmPm;
import elements.date.MonthAndDay;
import elements.date.OneLineMonthAndDay;
import elements.date.WeekDay;
import elements.status.BatteryIcon;
import elements.status.BatteryText;
import elements.status.Switch;
import elements.time.HoursGroup;
import elements.time.MinutesGroup;
import elements.time.SecondsGroup;
import elements.time.TwoDigits;
import helpers.StaticHelpers;
import main.MiBand4Editor;

public class EditMenu implements ActionListener {
	
	private JPopupMenu menu;
	private Component parent;
	private Element element;
	private MiBand4Editor main;
	
	public EditMenu(Component parent, MiBand4Editor main) {
		menu = new JPopupMenu();
		
		{
			JMenu edit = new JMenu(main.getInLang("editMenu_edit"));
			
			if(parent.getName().startsWith(TwoDigits.class.toString()) || 
					parent.getName().startsWith(WeekDay.class.toString()) ||
					parent.getName().startsWith(BatteryIcon.class.toString())) {
				JMenuItem hours = new JMenuItem(main.getInLang("editMenu_edit_imageIndex"));
				hours.addActionListener(this);
				edit.add(hours);
				
				JMenuItem minutes = new JMenuItem(main.getInLang("editMenu_edit_imageCount"));
				minutes.addActionListener(this);
				edit.add(minutes);
			}
			else if(parent.getName().startsWith(AmPm.class.toString())) {
				JMenuItem indexAm = new JMenuItem(main.getInLang("editMenu_edit_imageIndexAm"));
				indexAm.addActionListener(this);
				edit.add(indexAm);
				
				JMenuItem indexPm = new JMenuItem(main.getInLang("editMenu_edit_imageIndexPm"));
				indexPm.addActionListener(this);
				edit.add(indexPm);
			}
			else if(parent.getName().startsWith(OneLineMonthAndDay.class.toString()) ||
					parent.getName().startsWith(Steps.class.toString()) ||
					parent.getName().startsWith(StepsGoal.class.toString()) || 
					parent.getName().startsWith(Calories.class.toString()) ||
					parent.getName().startsWith(Pulse.class.toString()) ||
					parent.getName().startsWith(Distance.class.toString()) ||
					parent.getName().startsWith(BatteryText.class.toString())) {
				JMenuItem alignment = new JMenuItem(main.getInLang("editMenu_edit_alignment"));
				alignment.addActionListener(this);
				edit.add(alignment);
				
				JMenuItem spacing = new JMenuItem(main.getInLang("editMenu_edit_spacing"));
				spacing.addActionListener(this);
				edit.add(spacing);
				
				JMenuItem imageIndex = new JMenuItem(main.getInLang("editMenu_edit_imageIndex"));
				imageIndex.addActionListener(this);
				edit.add(imageIndex);
				
				JMenuItem imagesCount = new JMenuItem(main.getInLang("editMenu_edit_imageCount"));
				imagesCount.addActionListener(this);
				edit.add(imagesCount);
			}
			else if(parent.getName().startsWith(BGItem.class.toString())) {
				JMenuItem file = new JMenuItem(main.getInLang("editMenu_edit_file"));
				file.addActionListener(this);
				edit.add(file);
			}
			
			if(parent.getName().startsWith(OneLineMonthAndDay.class.toString())) {
				JMenuItem delimiterIndex = new JMenuItem(main.getInLang("editMenu_edit_delimiterIndex"));
				delimiterIndex.addActionListener(this);
				edit.add(delimiterIndex);
				
				JMenuItem twoDigitsMonth = new JMenuItem(main.getInLang("editMenu_edit_twoDigitsMonth"));
				twoDigitsMonth.addActionListener(this);
				edit.add(twoDigitsMonth);
				
				JMenuItem twoDigitsDay = new JMenuItem(main.getInLang("editMenu_edit_twoDigitsDay"));
				twoDigitsDay.addActionListener(this);
				edit.add(twoDigitsDay);
			}
			if(parent.getName().startsWith(Calories.class.toString())) {
				JMenuItem delimiterIndex = new JMenuItem(main.getInLang("editMenu_edit_delimiterIndex"));
				delimiterIndex.addActionListener(this);
				edit.add(delimiterIndex);
			}
			if(parent.getName().startsWith(Pulse.class.toString())) {
				JMenuItem delimiterIndex = new JMenuItem(main.getInLang("editMenu_edit_noDataIndex"));
				delimiterIndex.addActionListener(this);
				edit.add(delimiterIndex);
			}
			if(parent.getName().startsWith(Distance.class.toString())) {
				JMenuItem decimalPointIndex = new JMenuItem(main.getInLang("editMenu_edit_decimalPointIndex"));
				decimalPointIndex.addActionListener(this);
				edit.add(decimalPointIndex);

				JMenuItem suffixIndex = new JMenuItem(main.getInLang("editMenu_edit_suffixIndex"));
				suffixIndex.addActionListener(this);
				edit.add(suffixIndex);
			}
			if(parent.getName().startsWith(Switch.class.toString())) {
				JMenuItem indexOn = new JMenuItem(main.getInLang("editMenu_edit_imageIndexOn"));
				indexOn.addActionListener(this);
				edit.add(indexOn);

				JMenuItem indexOff = new JMenuItem(main.getInLang("editMenu_edit_imageIndexOff"));
				indexOff.addActionListener(this);
				edit.add(indexOff);

				JMenuItem boxWidth = new JMenuItem(main.getInLang("editMenu_edit_boxWidth"));
				boxWidth.addActionListener(this);
				edit.add(boxWidth);

				JMenuItem alignment = new JMenuItem(main.getInLang("editMenu_edit_alignment"));
				alignment.addActionListener(this);
				edit.add(alignment);

				JMenuItem order = new JMenuItem(main.getInLang("editMenu_edit_order"));
				order.addActionListener(this);
				edit.add(order);
			}
			
			menu.add(edit);
		}
		
		JMenuItem delete = new JMenuItem(main.getInLang("editMenu_delete"));
		delete.addActionListener(this);
		menu.add(delete);
		
		this.parent = parent;
		this.main = main;
		
		if(parent.getName().startsWith(BGItem.class.toString())) {
			element = main.bgItems.get(Integer.parseInt(parent.getName().split("\\|\\|")[1]));
		}
		else {
			element = main.elements.get(Integer.parseInt(parent.getName().split("\\|\\|")[1]));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String title = e.getActionCommand();
		main.lastClick.x=parent.getX();
		main.lastClick.y=parent.getY();
		
		if(title.equals(main.getInLang("editMenu_edit_imageIndex"))) {
			editImageIndex();
		}
		else if(title.equals(main.getInLang("editMenu_edit_imageCount"))) {
			editImageCount();
		}
		else if(title.equals(main.getInLang("editMenu_edit_imageIndexAm"))) {
			editImageIndexAM();
		}
		else if(title.equals(main.getInLang("editMenu_edit_imageIndexOn"))) {
			editImageIndexOn();
		}
		else if(title.equals(main.getInLang("editMenu_edit_imageIndexOff"))) {
			editImageIndexOff();
		}
		else if(title.equals(main.getInLang("editMenu_edit_imageIndexPm"))) {
			editImageIndexPM();
		}
		else if(title.equals(main.getInLang("editMenu_edit_alignment"))) {
			editAlignment();
		}
		else if(title.equals(main.getInLang("editMenu_edit_spacing"))) {
			editSpacing();
		}
		else if(title.equals(main.getInLang("editMenu_edit_delimiterIndex"))) {
			editDelimiterIndex();
		}
		else if(title.equals(main.getInLang("editMenu_edit_noDataIndex"))) {
			editNoDataIndex();
		}
		else if(title.equals(main.getInLang("editMenu_edit_twoDigitsMonth"))) {
			editTwoDigitsMonth();
		}
		else if(title.equals(main.getInLang("editMenu_edit_twoDigitsDay"))) {
			editTwoDigitsDay();
		}
		else if(title.equals(main.getInLang("editMenu_edit_suffixIndex"))) {
			editSuffixIndex();
		}
		else if(title.equals(main.getInLang("editMenu_edit_decimalPointIndex"))) {
			editDecimalPointIndex();
		}
		else if(title.equals(main.getInLang("editMenu_edit_boxWidth"))) {
			editBoxWidth();
		}
		else if(title.equals(main.getInLang("editMenu_edit_order"))) {
			editOrder();
		}
		else if(title.equals(main.getInLang("editMenu_edit_file"))) {
			editFile();
		}
		else if(title.equals(main.getInLang("editMenu_delete"))) {
			deleteElement();
		}
		menu.setSelected(null);
	}

	private void editImageIndex() {
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(main.getInLang("edit_imageIndex")));
		try {
			parent.getParent().remove(parent);
			main.elements.remove(element);
			if(element instanceof HoursGroup) {
				((HoursGroup)element).changeImageIndex(imageIndex);
				main.addElement(element);
			}
			else if(element instanceof MinutesGroup) {
				((MinutesGroup)element).changeImageIndex(imageIndex);
				main.addElement(element);
			}
			else if(element instanceof SecondsGroup) {
				((SecondsGroup)element).changeImageIndex(imageIndex);
				main.addElement(element);
			}
			else if(element instanceof MonthAndDay) {
				((MonthAndDay)element).changeImageIndex(imageIndex);
				main.addElement(element,parent.getWidth(),parent.getHeight());
			}
			else if(element instanceof WeekDay) {
				((WeekDay)element).changeImageIndex(imageIndex);
				main.addElement(element);
			}
			else if(element instanceof Steps) {
				((Steps)element).changeImageIndex(imageIndex);
				main.addElement(element,parent.getWidth(),parent.getHeight());
			}
			else if(element instanceof StepsGoal) {
				((StepsGoal)element).changeImageIndex(imageIndex);
				main.addElement(element,parent.getWidth(),parent.getHeight());
			}
			else if(element instanceof Calories) {
				((Calories)element).changeImageIndex(imageIndex);
				main.addElement(element,parent.getWidth(),parent.getHeight());
			}
			else if(element instanceof Pulse) {
				((Pulse)element).changeImageIndex(imageIndex);
				main.addElement(element,parent.getWidth(),parent.getHeight());
			}
			else if(element instanceof Distance) {
				((Distance)element).changeImageIndex(imageIndex);
				main.addElement(element,parent.getWidth(),parent.getHeight());
			}
			else if(element instanceof BatteryIcon) {
				((BatteryIcon)element).changeImageIndex(imageIndex);
				main.addElement(element);
			}
			else if(element instanceof BatteryText) {
				((BatteryText)element).changeImageIndex(imageIndex);
				main.addElement(element,parent.getWidth(),parent.getHeight());
			}
			else {
				System.out.println("Not found: "+element.getClass()); //FIXME Real ErrorMessage
			}
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_multi_title")+e1.getMessage(), main.getInLang("error_imageNotFound_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_multi_title")+e1.getMessage(), main.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void editImageCount() {
		int imageCount = Integer.parseInt(JOptionPane.showInputDialog(main.getInLang("edit_imageCount")));
		try {
			parent.getParent().remove(parent);
			main.elements.remove(element);
			if(element instanceof HoursGroup) {
				((HoursGroup)element).changeImageCount(imageCount);
				main.addElement(element);
			}
			else if(element instanceof MinutesGroup) {
				((MinutesGroup)element).changeImageCount(imageCount);
				main.addElement(element);
			}
			else if(element instanceof SecondsGroup) {
				((SecondsGroup)element).changeImageCount(imageCount);
				main.addElement(element);
			}
			else if(element instanceof MonthAndDay) {
				((MonthAndDay)element).changeImageCount(imageCount);
				main.addElement(element,parent.getWidth(),parent.getHeight());
			}
			else if(element instanceof WeekDay) {
				((WeekDay)element).changeImageCount(imageCount);
				main.addElement(element);
			}
			else if(element instanceof Steps) {
				((Steps)element).changeImageCount(imageCount);
				main.addElement(element,parent.getWidth(),parent.getHeight());
			}
			else if(element instanceof StepsGoal) {
				((StepsGoal)element).changeImageCount(imageCount);
				main.addElement(element,parent.getWidth(),parent.getHeight());
			}
			else if(element instanceof Calories) {
				((Calories)element).changeImageCount(imageCount);
				main.addElement(element,parent.getWidth(),parent.getHeight());
			}
			else if(element instanceof Pulse) {
				((Pulse)element).changeImageCount(imageCount);
				main.addElement(element,parent.getWidth(),parent.getHeight());
			}
			else if(element instanceof Distance) {
				((Distance)element).changeImageCount(imageCount);
				main.addElement(element,parent.getWidth(),parent.getHeight());
			}
			else if(element instanceof BatteryIcon) {
				((BatteryIcon)element).changeImageCount(imageCount);
				main.addElement(element);
			}
			else if(element instanceof BatteryText) {
				((BatteryText)element).changeImageCount(imageCount);
				main.addElement(element,parent.getWidth(),parent.getHeight());
			}
			else {
				System.out.println("Not found: "+element.getClass()); //FIXME Real ErrorMessage
			}
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_multi_title")+e1.getMessage(), main.getInLang("error_imageNotFound_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_multi_title")+e1.getMessage(), main.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void editImageIndexAM() {
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(main.getInLang("edit_imageIndexAm")));
		try {
			parent.getParent().remove(parent);
			main.elements.remove(element);
			((AmPm)element).changeImageIndexAm(imageIndex);
			main.addElement(element);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_single_title")+e1.getMessage(), main.getInLang("error_imageNotFound_single_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_single_title")+e1.getMessage(), main.getInLang("error_io_single_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void editImageIndexPM() {
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(main.getInLang("edit_imageIndexPm")));
		try {
			parent.getParent().remove(parent);
			main.elements.remove(element);
			((AmPm)element).changeImageIndexPm(imageIndex);
			main.addElement(element);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_single_title")+e1.getMessage(), main.getInLang("error_imageNotFound_single_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_single_title")+e1.getMessage(), main.getInLang("error_io_single_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void editImageIndexOff() {
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(main.getInLang("edit_imageIndexOff")));
		try {
			parent.getParent().remove(parent);
			main.elements.remove(element);
			((Switch)element).changeImageIndexOff(imageIndex);
			main.addElement(element);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_single_title")+e1.getMessage(), main.getInLang("error_imageNotFound_single_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_single_title")+e1.getMessage(), main.getInLang("error_io_single_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}

	private void editImageIndexOn() {
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(main.getInLang("edit_imageIndexOn")));
		try {
			parent.getParent().remove(parent);
			main.elements.remove(element);
			((Switch)element).changeImageIndexOn(imageIndex);
			main.addElement(element);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_single_title")+e1.getMessage(), main.getInLang("error_imageNotFound_single_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_single_title")+e1.getMessage(), main.getInLang("error_io_single_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void editAlignment() {
		String alignment = StaticConfigurators.askForAlignment(main);
		try {
			parent.getParent().remove(parent);
			main.elements.remove(element);
			if(element instanceof MonthAndDay) {
				((MonthAndDay)element).changeAlignment(alignment);
			}
			else if(element instanceof Steps) {
				((Steps)element).changeAlignment(alignment);
			}
			else if(element instanceof StepsGoal) {
				((StepsGoal)element).changeAlignment(alignment);
			}
			else if(element instanceof Calories) {
				((Calories)element).changeAlignment(alignment);
			}
			else if(element instanceof Pulse) {
				((Pulse)element).changeAlignment(alignment);
			}
			else if(element instanceof Distance) {
				((Distance)element).changeAlignment(alignment);
			}
			else if(element instanceof Switch) {
				((Switch)element).changeAlignment(alignment);
			}
			else if(element instanceof BatteryText) {
				((BatteryText)element).changeAlignment(alignment);
			}
			main.addElement(element,parent.getWidth(),parent.getHeight());
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_multi_title")+e1.getMessage(), main.getInLang("error_imageNotFound_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_multi_title")+e1.getMessage(), main.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void editSpacing() {
		int spacing = Integer.parseInt(JOptionPane.showInputDialog(main.getInLang("edit_spacing")));
		try {
			parent.getParent().remove(parent);
			main.elements.remove(element);
			if(element instanceof MonthAndDay) {
				((MonthAndDay)element).changeSpacing(spacing);
			}
			else if(element instanceof Steps) {
				((Steps)element).changeSpacing(spacing);
			}
			else if(element instanceof StepsGoal) {
				((StepsGoal)element).changeSpacing(spacing);
			}
			else if(element instanceof Calories) {
				((Calories)element).changeSpacing(spacing);
			}
			else if(element instanceof Pulse) {
				((Pulse)element).changeSpacing(spacing);
			}
			else if(element instanceof Distance) {
				((Distance)element).changeSpacing(spacing);
			}
			else if(element instanceof BatteryText) {
				((BatteryText)element).changeSpacing(spacing);
			}
			main.addElement(element,parent.getWidth(),parent.getHeight());
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_multi_title")+e1.getMessage(), main.getInLang("error_imageNotFound_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_multi_title")+e1.getMessage(), main.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void editDelimiterIndex() {
		int delimiterIndex = Integer.parseInt(JOptionPane.showInputDialog(main.getInLang("edit_delimiterIndex")));
		try {
			parent.getParent().remove(parent);
			main.elements.remove(element);
			if(element instanceof MonthAndDay) {
				((MonthAndDay)element).changeDelimiterIndex(delimiterIndex);
			}
			else if(element instanceof Calories) {
				((Calories)element).changeDelimiterIndex(delimiterIndex);
			}
			main.addElement(element,parent.getWidth(),parent.getHeight());
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_multi_title")+e1.getMessage(), main.getInLang("error_imageNotFound_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_multi_title")+e1.getMessage(), main.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void editNoDataIndex() {
		int noData = Integer.parseInt(JOptionPane.showInputDialog(main.getInLang("edit_noDataIndex")));
		try {
			parent.getParent().remove(parent);
			main.elements.remove(element);
			if(element instanceof Pulse) {
				((Pulse)element).changeNoDataImageIndex(noData);
			}
			main.addElement(element,parent.getWidth(),parent.getHeight());
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_multi_title")+e1.getMessage(), main.getInLang("error_imageNotFound_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_multi_title")+e1.getMessage(), main.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void editTwoDigitsMonth() {
		boolean twoDigitsMonth = StaticConfigurators.askForBoolean(main.getInLang("edit_twoDigitsMonth_title"),main.getInLang("edit_twoDigitsMonth"),main.getInLang("edit_accept"));
		try {
			parent.getParent().remove(parent);
			main.elements.remove(element);
			((MonthAndDay)element).changeTwoDigitsMonth(twoDigitsMonth);
			main.addElement(element,parent.getWidth(),parent.getHeight());
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_multi_title")+e1.getMessage(), main.getInLang("error_imageNotFound_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_multi_title")+e1.getMessage(), main.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void editTwoDigitsDay() {
		boolean twoDigitsDay = StaticConfigurators.askForBoolean(main.getInLang("edit_twoDigitsDay_title"),main.getInLang("edit_twoDigitsDay"),main.getInLang("edit_accept"));
		try {
			parent.getParent().remove(parent);
			main.elements.remove(element);
			((MonthAndDay)element).changeTwoDigitsDay(twoDigitsDay);
			main.addElement(element,parent.getWidth(),parent.getHeight());
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_multi_title")+e1.getMessage(), main.getInLang("error_imageNotFound_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_multi_title")+e1.getMessage(), main.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void editSuffixIndex() {
		int suffixIndex = Integer.parseInt(JOptionPane.showInputDialog(main.getInLang("edit_suffixIndex")));
		try {
			parent.getParent().remove(parent);
			main.elements.remove(element);
			if(element instanceof Distance) {
				((Distance)element).changeSuffixImageIndex(suffixIndex);
			}
			main.addElement(element,parent.getWidth(),parent.getHeight());
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_multi_title")+e1.getMessage(), main.getInLang("error_imageNotFound_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_multi_title")+e1.getMessage(), main.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void editDecimalPointIndex() {
		int decimalPointIndex = Integer.parseInt(JOptionPane.showInputDialog(main.getInLang("edit_decimalPointIndex")));
		try {
			parent.getParent().remove(parent);
			main.elements.remove(element);
			if(element instanceof Distance) {
				((Distance)element).changeDecimalPointImageIndex(decimalPointIndex);
			}
			main.addElement(element,parent.getWidth(),parent.getHeight());
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_multi_title")+e1.getMessage(), main.getInLang("error_imageNotFound_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_multi_title")+e1.getMessage(), main.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}

	private void editBoxWidth() {
		int boxWidth = Integer.parseInt(JOptionPane.showInputDialog(main.getInLang("edit_boxWidth")));
		try {
			parent.getParent().remove(parent);
			main.elements.remove(element);
			if(element instanceof Switch) {
				((Switch)element).changeBoxWidth(boxWidth);
			}
			main.addElement(element,parent.getWidth(),parent.getHeight());
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_multi_title")+e1.getMessage(), main.getInLang("error_imageNotFound_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_multi_title")+e1.getMessage(), main.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}

	private void editOrder() {
		int order = Integer.parseInt(JOptionPane.showInputDialog(main.getInLang("edit_order")));
		try {
			parent.getParent().remove(parent);
			main.elements.remove(element);
			if(element instanceof Switch) {
				((Switch)element).changeOrder(order);
			}
			main.addElement(element,parent.getWidth(),parent.getHeight());
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_multi_title")+e1.getMessage(), main.getInLang("error_imageNotFound_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_multi_title")+e1.getMessage(), main.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void editFile() {
		StaticHelpers.setWinLookAndFeel();
		JFileChooser f = new JFileChooser();
		f.setCurrentDirectory(new File("."));
		f.showOpenDialog(null);
		File file = f.getSelectedFile();
		StaticHelpers.setJavaLookAndFeel();
		try {
			parent.getParent().remove(parent);
			BGItem item = (BGItem) element;
			main.bgItems.remove(item);
			item.setFile(file);
			main.addBGItem(item);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_imageNotFound_multi_title")+e1.getMessage(), main.getInLang("error_imageNotFound_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, main.getInLang("error_io_multi_title")+e1.getMessage(), main.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void deleteElement() {
		if(element instanceof BGItem) {
			main.bgItems.remove(element);
			parent.getParent().remove(parent);
			main.mainMenu.setEnabled(element,true);
			main.renameComponentsSinceBGItem(parent);
			main.refresh();
		}
		else {
			main.elements.remove(element);
			parent.getParent().remove(parent);
			main.mainMenu.setEnabled(element,true);
			main.renameComponentsSince(parent);
			main.refresh();
		}
	}

	public void show(Component component, int x, int y) {
		menu.show(component, x, y);
	}

}
