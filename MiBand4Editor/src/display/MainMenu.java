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
import javax.swing.filechooser.FileFilter;

import elements.BGItem;
import elements.Element;
import elements.activity.Calories;
import elements.activity.Distance;
import elements.activity.Pulse;
import elements.activity.Steps;
import elements.activity.StepsGoal;
import elements.date.AmPm;
import elements.date.MonthAndDay;
import elements.date.WeekDay;
import elements.status.BatteryIcon;
import elements.status.BatteryText;
import elements.status.Switch;
import elements.time.HoursGroup;
import elements.time.MinutesGroup;
import elements.time.SecondsGroup;
import helpers.Builder;
import helpers.StaticHelpers;
import helpers.UnequalDimensionsException;
import main.MiBand4Editor;

public class MainMenu implements ActionListener {
	
	private JPopupMenu menu;
	private MiBand4Editor parent;
	
	public MainMenu(MiBand4Editor parent) {
		this.parent = parent;
		menu = new JPopupMenu();
		
		{
			
			JMenu add = new JMenu(parent.getInLang("mainMenu_add"));
			
			{
				JMenu time = new JMenu(parent.getInLang("mainMenu_add_time"));
				
				time.add(item("mainMenu_add_time_hours"));
				time.add(item("mainMenu_add_time_minutes"));
				time.add(item("mainMenu_add_time_seconds"));
				
				add.add(time);
			}
			
			{
				JMenu date = new JMenu(parent.getInLang("mainMenu_add_date"));
				
				date.add(item("mainMenu_add_date_monthAndDay"));
				date.add(item("mainMenu_add_date_weekday"));
				date.add(item("mainMenu_add_date_dayAmPm"));
				
				add.add(date);
			}
			
			{
				JMenu activity = new JMenu(parent.getInLang("mainMenu_add_activity"));
				
				activity.add(item("mainMenu_add_activity_steps"));
				activity.add(item("mainMenu_add_activity_stepsGoal"));;
				activity.add(item("mainMenu_add_activity_calories"));
				activity.add(item("mainMenu_add_activity_pulse"));
				activity.add(item("mainMenu_add_activity_distance"));
				
				add.add(activity);
			}
			
			{
				JMenu status = new JMenu(parent.getInLang("mainMenu_add_status"));
				
				{
					JMenu battery = new JMenu(parent.getInLang("mainMenu_add_status_battery"));
					
					battery.add(item("mainMenu_add_status_battery_text"));
					battery.add(item("mainMenu_add_status_battery_icon"));
					
					status.add(battery);
				}

				status.add(item("mainMenu_add_status_doNotDisturb"));
				status.add(item("mainMenu_add_status_bluetooth"));
				status.add(item("mainMenu_add_status_lock"));
				
				add.add(status);
			}
			
			JMenuItem bgitem = new JMenuItem(parent.getInLang("mainMenu_add_bgitem"));
			bgitem.addActionListener(this);
			add.add(bgitem);

			menu.add(add);
			
			JMenuItem generate = new JMenuItem(parent.getInLang("mainMenu_generate"));
			generate.addActionListener(this);
			menu.add(generate);
		}
	}
	
	public JMenuItem item(String sourceName) {
		JMenuItem lock = new JMenuItem(parent.getInLang(sourceName));
		lock.addActionListener(this);
		return lock;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String title = e.getActionCommand();
		
		if(title.equals(parent.getInLang("mainMenu_add_time_hours"))) {
			addTimeHours(false);
		}
		else if(title.equals(parent.getInLang("mainMenu_add_time_minutes"))) {
			addTimeMinutes(false);
		}
		else if(title.equals(parent.getInLang("mainMenu_add_time_seconds"))) {
			addTimeSeconds(false);
		}

		else if(title.equals(parent.getInLang("mainMenu_add_date_dayAmPm"))) {
			addDateDayAmPm();
		}
		else if(title.equals(parent.getInLang("mainMenu_add_date_monthAndDay"))) {
			addDateMonthAndDay();
		}
		else if(title.equals(parent.getInLang("mainMenu_add_date_weekday"))) {
			addDateWeekday(false);
		}
		else if(title.equals(parent.getInLang("mainMenu_add_activity_steps"))) {
			addActivitySteps();
		}
		else if(title.equals(parent.getInLang("mainMenu_add_activity_stepsGoal"))) {
			addActivityStepGoal();
		}
		else if(title.equals(parent.getInLang("mainMenu_add_activity_calories"))) {
			addActivityCalories();
		}
		else if(title.equals(parent.getInLang("mainMenu_add_activity_pulse"))) {
			addActivityPulse();
		}
		else if(title.equals(parent.getInLang("mainMenu_add_activity_distance"))) {
			addActivityDistance();
		}
		else if(title.equals(parent.getInLang("mainMenu_add_status_battery_text"))) {
			addStatusBatteryText();
		}
		else if(title.equals(parent.getInLang("mainMenu_add_status_battery_icon"))) {
			addStatusBatteryIcon(false);
		}
		else if(title.equals(parent.getInLang("mainMenu_add_status_doNotDisturb"))) {
			addStatusDoNotDisturb();
		}
		else if(title.equals(parent.getInLang("mainMenu_add_status_bluetooth"))) {
			addStatusBluetooth();
		}
		else if(title.equals(parent.getInLang("mainMenu_add_status_lock"))) {
			addStatusLock();
		}
		
		else if(title.equals(parent.getInLang("mainMenu_add_bgitem"))) {
			addBGItem();
		}
		
		else if(title.equals(parent.getInLang("mainMenu_generate"))) {
			generate();
		}
		menu.setSelected(null);
	}

	private void addTimeHours(boolean ignoreDimensions) {
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageIndex")));
		int imageCount = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageCount")));
		try {
			HoursGroup group = new HoursGroup(parent.lastClick.x/3,parent.lastClick.y/3,imageIndex,imageCount,ignoreDimensions);
			parent.addElement(group);
			JPopupMenu time = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[0].getSubElements()[0];
			((JMenuItem)time.getSubElements()[0]).setEnabled(false);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_imageNotFount_multi")+e1.getMessage(), parent.getInLang("error_imageNotFount_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (UnequalDimensionsException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_unequalDimension")+e1.getMessage(), parent.getInLang("error_unequalDimension_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
			addTimeHours(true);
		}
	}
	
	private void addTimeMinutes(boolean ignoreDimensions) {
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageIndex")));
		int imageCount = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageCount")));
		try {
			MinutesGroup group = new MinutesGroup(parent.lastClick.x/3,parent.lastClick.y/3,imageIndex,imageCount,ignoreDimensions);
			parent.addElement(group);
			JPopupMenu time = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[0].getSubElements()[0];
			((JMenuItem)time.getSubElements()[1]).setEnabled(false);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_imageNotFount_multi")+e1.getMessage(), parent.getInLang("error_imageNotFount_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (UnequalDimensionsException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_unequalDimension")+e1.getMessage(), parent.getInLang("error_unequalDimension_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
			addTimeMinutes(ignoreDimensions);
		}
	}
	
	private void addTimeSeconds(boolean ignoreDimensions) {
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageIndex")));
		int imageCount = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageCount")));
		try {
			SecondsGroup group = new SecondsGroup(parent.lastClick.x/3,parent.lastClick.y/3,imageIndex,imageCount,ignoreDimensions);
			parent.addElement(group);
			JPopupMenu time = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[0].getSubElements()[0];
			((JMenuItem)time.getSubElements()[2]).setEnabled(false);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_imageNotFount_multi")+e1.getMessage(), parent.getInLang("error_imageNotFount_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (UnequalDimensionsException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_unequalDimension")+e1.getMessage(), parent.getInLang("error_unequalDimension_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
			addTimeSeconds(true);
		}
	}
	
	private void addDateDayAmPm() {
		int imageIndexAm = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageIndexAm")));
		int imageIndexPm = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageIndexPm")));
		try {
			AmPm group = new AmPm(parent.lastClick.x/3,parent.lastClick.y/3,imageIndexAm,imageIndexPm);
			parent.addElement(group);
			JPopupMenu date = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[1].getSubElements()[0];
			((JMenuItem)date.getSubElements()[2]).setEnabled(false);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_imageNotFount_multi")+e1.getMessage(), parent.getInLang("error_imageNotFount_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (UnequalDimensionsException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_unequalDimension")+e1.getMessage(), parent.getInLang("error_unequalDimension_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void addDateMonthAndDay() {
		String alignment = StaticConfigurators.askForAlignment(parent);
		int spacing = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_spacing")));
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageIndex")));
		int imageCount = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageCount")));
		int delimiterIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_delimiterIndex")));
		boolean twoDigitsMonth = StaticConfigurators.askForBoolean(parent.getInLang("edit_twoDigitsMonth_title"),parent.getInLang("edit_twoDigitsMonth"),parent.getInLang("edit_accept"));
		boolean twoDigitsDay = StaticConfigurators.askForBoolean(parent.getInLang("edit_twoDigitsDay_title"),parent.getInLang("edit_twoDigitsDay"),parent.getInLang("edit_accept"));
		try {
			MonthAndDay group = new MonthAndDay(parent.lastClick.x/3, parent.lastClick.y/3, alignment, spacing, imageIndex, imageCount, delimiterIndex, twoDigitsMonth, twoDigitsDay);
			parent.addElement(group);
			JPopupMenu date = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[1].getSubElements()[0];
			((JMenuItem)date.getSubElements()[0]).setEnabled(false);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_imageNotFount_multi")+e1.getMessage(), parent.getInLang("error_imageNotFount_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (UnequalDimensionsException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_unequalDimension")+e1.getMessage(), parent.getInLang("error_unequalDimension_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void addDateWeekday(boolean ignoreDimensions) {
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageIndex")));
		int imageCount = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageCount")));
		try {
			WeekDay group = new WeekDay(parent.lastClick.x/3,parent.lastClick.y/3,imageIndex,imageCount,ignoreDimensions);
			parent.addElement(group);
			JPopupMenu date = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[1].getSubElements()[0];
			((JMenuItem)date.getSubElements()[1]).setEnabled(false);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_imageNotFount_multi")+e1.getMessage(), parent.getInLang("error_imageNotFount_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (UnequalDimensionsException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_unequalDimension")+e1.getMessage(), parent.getInLang("error_unequalDimension_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
			addDateWeekday(true);
		}
	}
	
	private void addActivitySteps() {
		String alignment = StaticConfigurators.askForAlignment(parent);
		int spacing = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_spacing")));
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageIndex")));
		int imageCount = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageCount")));
		try {
			Steps group = new Steps(parent.lastClick.x/3, parent.lastClick.y/3, alignment, spacing, imageIndex, imageCount);
			parent.addElement(group);
			JPopupMenu activity = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[2].getSubElements()[0];
			((JMenuItem)activity.getSubElements()[0]).setEnabled(false);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_imageNotFount_multi")+e1.getMessage(), parent.getInLang("error_imageNotFount_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (UnequalDimensionsException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_unequalDimension")+e1.getMessage(), parent.getInLang("error_unequalDimension_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void addActivityStepGoal() {
		String alignment = StaticConfigurators.askForAlignment(parent);
		int spacing = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_spacing")));
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageIndex")));
		int imageCount = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageCount")));
		try {
			StepsGoal group = new StepsGoal(parent.lastClick.x/3, parent.lastClick.y/3, alignment, spacing, imageIndex, imageCount);
			parent.addElement(group);
			JPopupMenu activity = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[2].getSubElements()[0];
			((JMenuItem)activity.getSubElements()[1]).setEnabled(false);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_imageNotFount_multi")+e1.getMessage(), parent.getInLang("error_imageNotFount_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (UnequalDimensionsException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_unequalDimension")+e1.getMessage(), parent.getInLang("error_unequalDimension_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void addActivityCalories() {
		String alignment = StaticConfigurators.askForAlignment(parent);
		int spacing = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_spacing")));
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageIndex")));
		int imageCount = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageCount")));
		int delimiterIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_delimiterIndex")));
		try {
			Calories group = new Calories(parent.lastClick.x/3, parent.lastClick.y/3, alignment, spacing, imageIndex, imageCount, delimiterIndex);
			parent.addElement(group);
			JPopupMenu activity = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[2].getSubElements()[0];
			((JMenuItem)activity.getSubElements()[2]).setEnabled(false);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_imageNotFount_multi")+e1.getMessage(), parent.getInLang("error_imageNotFount_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (UnequalDimensionsException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_unequalDimension")+e1.getMessage(), parent.getInLang("error_unequalDimension_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void addActivityPulse() {
		String alignment = StaticConfigurators.askForAlignment(parent);
		int spacing = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_spacing")));
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageIndex")));
		int imageCount = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageCount")));
		int noDataIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_noDataIndex")));
		try {
			Pulse group = new Pulse(parent.lastClick.x/3, parent.lastClick.y/3, alignment, spacing, imageIndex, imageCount, noDataIndex);
			parent.addElement(group);
			JPopupMenu activity = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[2].getSubElements()[0];
			((JMenuItem)activity.getSubElements()[3]).setEnabled(false);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_imageNotFount_multi")+e1.getMessage(), parent.getInLang("error_imageNotFount_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (UnequalDimensionsException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_unequalDimension")+e1.getMessage(), parent.getInLang("error_unequalDimension_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void addActivityDistance() {
		String alignment = StaticConfigurators.askForAlignment(parent);
		int spacing = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_spacing")));
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageIndex")));
		int imageCount = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageCount")));
		int decimalPointIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_decimalPointIndex")));
		int suffixIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_suffixIndex")));
		try {
			Distance group = new Distance(parent.lastClick.x/3, parent.lastClick.y/3, alignment, spacing, imageIndex, imageCount, decimalPointIndex, suffixIndex);
			parent.addElement(group);
			JPopupMenu activity = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[2].getSubElements()[0];
			((JMenuItem)activity.getSubElements()[4]).setEnabled(false);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_imageNotFount_multi")+e1.getMessage(), parent.getInLang("error_imageNotFount_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (UnequalDimensionsException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_unequalDimension")+e1.getMessage(), parent.getInLang("error_unequalDimension_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void addStatusBatteryText() {
		String alignment = StaticConfigurators.askForAlignment(parent);
		int spacing = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_spacing")));
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageIndex")));
		int imageCount = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageCount")));
		try {
			BatteryText group = new BatteryText(parent.lastClick.x/3, parent.lastClick.y/3, alignment, spacing, imageIndex, imageCount);
			parent.addElement(group);
			JPopupMenu status = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[3].getSubElements()[0];
			((JMenuItem)status.getSubElements()[0].getSubElements()[0].getSubElements()[0]).setEnabled(false);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_imageNotFount_multi")+e1.getMessage(), parent.getInLang("error_imageNotFount_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (UnequalDimensionsException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_unequalDimension")+e1.getMessage(), parent.getInLang("error_unequalDimension_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void addStatusBatteryIcon(boolean ignoreDimensions) {
		int imageIndex = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageIndex")));
		int imageCount = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageCount")));
		try {
			BatteryIcon group = new BatteryIcon(parent.lastClick.x/3,parent.lastClick.y/3,imageIndex,imageCount,ignoreDimensions);
			parent.addElement(group);
			JPopupMenu status = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[3].getSubElements()[0];
			((JMenuItem)status.getSubElements()[0].getSubElements()[0].getSubElements()[1]).setEnabled(false);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_imageNotFount_multi")+e1.getMessage(), parent.getInLang("error_imageNotFount_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (UnequalDimensionsException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_unequalDimension")+e1.getMessage(), parent.getInLang("error_unequalDimension_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
			addStatusBatteryIcon(true);
		}
	}

	private void addStatus(String name, int menuNr) {
		int boxWidth = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_boxWidth")));
		String alignment = StaticConfigurators.askForAlignment(parent);
		int order = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_order")));
		int imageIndexOn = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageIndexOn")));
		int imageIndexOff = Integer.parseInt(JOptionPane.showInputDialog(parent.getInLang("edit_imageIndexOff")));
		try {
			Switch group = new Switch(name,parent.lastClick.x/3, parent.lastClick.y/3, boxWidth, alignment, order, imageIndexOn, imageIndexOff);
			parent.addElement(group);
			JPopupMenu status = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[3].getSubElements()[0];
			((JMenuItem)status.getSubElements()[menuNr]).setEnabled(false);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_imageNotFount_multi")+e1.getMessage(), parent.getInLang("error_imageNotFount_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}

	private void addStatusDoNotDisturb() {
		addStatus("DoNotDisturb",1);
	}
	
	private void addStatusBluetooth() {
		addStatus("Bluetooth",2);
	}
	
	private void addStatusLock() {
		addStatus("Lock",3);
	}
	
	private void addBGItem() {
		StaticHelpers.setWinLookAndFeel();
		JFileChooser f = new JFileChooser();
		f.setCurrentDirectory(new File("."));
		f.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				String[] name = f.getAbsolutePath().split("\\.");
				if(f.isDirectory()) {
					return true;
				}
				return name[name.length - 1].equals("png");
			}
			@Override
			public String getDescription() {
				return null;
			}
		});
		f.setAcceptAllFileFilterUsed(false);
		f.showOpenDialog(null);
		File file = f.getSelectedFile();
		StaticHelpers.setJavaLookAndFeel();
		try {
			BGItem group = new BGItem(file,parent.bgItems.size(),parent.lastClick.x/3, parent.lastClick.y/3);
			parent.addBGItem(group);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_imageNotFount_multi")+e1.getMessage(), parent.getInLang("error_imageNotFount_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	private void generate() {
		try {
			Builder builder = new Builder();
			builder.generateBackground(parent.bgItems.toArray(new BGItem[0]));
			builder.generateJSON(parent.elements);
			File[][] disabled = builder.disableUnneededImgs(parent.elements);
			builder.runBuilder();
			builder.reenableImgs(disabled);
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void show(Component component, int x, int y) {
		menu.show(component, x, y);
	}

	public void setEnabled(Element element, boolean enabled) {
		JPopupMenu time = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[0].getSubElements()[0];
		JPopupMenu date = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[1].getSubElements()[0];
		JPopupMenu activity = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[2].getSubElements()[0];
		JPopupMenu status = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[3].getSubElements()[0];
		if(element instanceof HoursGroup) {
			((JMenuItem)time.getSubElements()[0]).setEnabled(enabled);
		}
		else if(element instanceof MinutesGroup) {
			((JMenuItem)time.getSubElements()[1]).setEnabled(enabled);
		}
		else if(element instanceof SecondsGroup) {
			((JMenuItem)time.getSubElements()[2]).setEnabled(enabled);
		}
		else if(element instanceof AmPm) {
			((JMenuItem)date.getSubElements()[2]).setEnabled(enabled);
		}
		else if(element instanceof MonthAndDay) {
			((JMenuItem)date.getSubElements()[0]).setEnabled(enabled);
		}
		else if(element instanceof WeekDay) {
			((JMenuItem)date.getSubElements()[1]).setEnabled(enabled);
		}
		else if(element instanceof Steps) {
			((JMenuItem)activity.getSubElements()[0]).setEnabled(enabled);
		}
		else if(element instanceof StepsGoal) {
			((JMenuItem)activity.getSubElements()[1]).setEnabled(enabled);
		}
		else if(element instanceof Calories) {
			((JMenuItem)activity.getSubElements()[2]).setEnabled(enabled);
		}
		else if(element instanceof Pulse) {
			((JMenuItem)activity.getSubElements()[3]).setEnabled(enabled);
		}
		else if(element instanceof Distance) {
			((JMenuItem)activity.getSubElements()[4]).setEnabled(enabled);
		}
		else if(element instanceof BatteryText) {
			((JMenuItem)status.getSubElements()[0].getSubElements()[0].getSubElements()[0]).setEnabled(enabled);
		}
		else if(element instanceof BatteryIcon) {
			((JMenuItem)status.getSubElements()[0].getSubElements()[0].getSubElements()[1]).setEnabled(enabled);
		}
		else if(element instanceof Switch) {
			Switch switchElement = (Switch)element;
			switch(switchElement.getName()) {
			case "DoNotDisturb":
				((JMenuItem)status.getSubElements()[1]).setEnabled(enabled);
				break;
			case "Bluetooth":
				((JMenuItem)status.getSubElements()[2]).setEnabled(enabled);
				break;
			case "Lock":
				((JMenuItem)status.getSubElements()[3]).setEnabled(enabled);
				break;
			}
		}
	}

	public void enableAll() {
		JPopupMenu time = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[0].getSubElements()[0];
		JPopupMenu date = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[1].getSubElements()[0];
		JPopupMenu activity = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[2].getSubElements()[0];
		JPopupMenu status = (JPopupMenu) menu.getSubElements()[0].getSubElements()[0].getSubElements()[3].getSubElements()[0];

		for(int i=0;i<3;i++) {
			((JMenuItem)time.getSubElements()[i]).setEnabled(true);
		}
		for(int i=0;i<3;i++) {
			((JMenuItem)date.getSubElements()[i]).setEnabled(true);
		}
		for(int i=0;i<4;i++) {
			((JMenuItem)activity.getSubElements()[i]).setEnabled(true);
		}
		for(int i=1;i<4;i++) {
			((JMenuItem)status.getSubElements()[i]).setEnabled(true);
		}
		((JMenuItem)status.getSubElements()[0].getSubElements()[0].getSubElements()[0]).setEnabled(true);
		((JMenuItem)status.getSubElements()[0].getSubElements()[0].getSubElements()[1]).setEnabled(true);
	}

}
