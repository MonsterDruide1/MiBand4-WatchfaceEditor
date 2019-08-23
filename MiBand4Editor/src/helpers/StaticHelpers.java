package helpers;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import elements.BGItem;
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
import main.MiBand4Editor;

public class StaticHelpers {
	
	public static Dimension getImageDimension(File imgFile) throws IOException {
		  int pos = imgFile.getName().lastIndexOf(".");
		  String suffix = imgFile.getName().substring(pos + 1);
		  Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
		  while(iter.hasNext()) {
		    ImageReader reader = iter.next();
		    try {
		      ImageInputStream stream = new FileImageInputStream(imgFile);
		      reader.setInput(stream);
		      int width = reader.getWidth(reader.getMinIndex());
		      int height = reader.getHeight(reader.getMinIndex());
		      return new Dimension(width, height);
		    } catch (IOException e) {
		      throw new IOException(imgFile.getAbsolutePath()+e);
		    } finally {
		      reader.dispose();
		    }
		  }

		  throw new FileNotFoundException(imgFile.getAbsolutePath());
		}
	
	public static JLabel getJLabelFromImage(int index) throws IOException {
		return getJLabelFromImage(String.format("%04d", index));
	}
	
	public static JLabel getJLabelFromImage(String formatted) throws IOException {
		try {
			return new JLabel(new ImageIcon(getBufferedImage(formatted)));
		}
		catch(IOException e) {
			throw new IOException(formatted+".png"+e);
		}
	}
	
	public static Image getBufferedImage(String formatted) throws IOException{
		try(FileInputStream stream = new FileInputStream(new File(MiBand4Editor.currentPath.getAbsolutePath()+"/"+formatted+".png"))){
			BufferedImage image = ImageIO.read(stream);
			Image im = new ImageIcon(image).getImage();
			im = im.getScaledInstance(image.getWidth()*3, image.getHeight()*3, Image.SCALE_SMOOTH);
			stream.close();
			image.flush();
			im.flush();
			System.gc();
			return im;
		}
	}
	
	public static Image getUnscaledBufferedImage(String formatted) throws IOException{
		try(FileInputStream stream = new FileInputStream(new File(MiBand4Editor.currentPath.getAbsolutePath()+"/"+formatted+".png"))){
			BufferedImage image = ImageIO.read(stream);
			return image;
		}
	}
	
	public HashMap<String, String> language() {
		HashMap<String, String> language = null;
		String[] files = new File("src/xml/lang").list();
		for(int i=0;i<files.length;i++) {
			if(files[i].split(".xml")[0].equals(System.getProperty("user.language"))) {
				language = parseLanguage("lang/"+files[i]);
			}
		}
		if(language==null) {
			language = parseLanguage("lang/en.xml");
		}
		return language;
	}
	
	public static HashMap<String, String> parseLanguage(String name) {
		try {
			Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("src/xml/"+name);
			Element docEle = dom.getDocumentElement();
			NodeList nl = docEle.getElementsByTagName("value");
			if(nl != null && nl.getLength() > 0) {
				HashMap<String, String> returns = new HashMap<String, String>();
				for(int i = 0 ; i < nl.getLength();i++) {
					Element el = (Element)nl.item(i);
					returns.put(el.getAttribute("name"), el.getTextContent());
				}
				return returns;
			}
		}catch(FileNotFoundException e) {
			try {
				System.out.println("JAR-MODE");
				InputStream is = MiBand4Editor.class.getClassLoader().getResourceAsStream("src/xml/"+name);
				Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
				Element docEle = dom.getDocumentElement();
				NodeList nl = docEle.getElementsByTagName("value");
				if(nl != null && nl.getLength() > 0) {
					HashMap<String, String> returns = new HashMap<String, String>();
					for(int i = 0 ; i < nl.getLength();i++) {
						Element el = (Element)nl.item(i);
						returns.put(el.getAttribute("name"), el.getTextContent());
					}
					return returns;
				}
			}
			catch(Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setWinLookAndFeel() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e){
			e.printStackTrace();
		}
	    catch (UnsupportedLookAndFeelException e) {
	        e.printStackTrace();
	     }
	}
	
	public static void setJavaLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e){
			e.printStackTrace();
		}
	    catch (UnsupportedLookAndFeelException e) {
	        e.printStackTrace();
	     }
	}

	public static void parseWatchface(MiBand4Editor parent, File folder) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(folder.getAbsolutePath()+"/real_watchface.json")));
		String full = "";
		String line = "";
		while((line=br.readLine())!=null) {
			full+=line;
		}
		br.close();
		
		JSONObject object = new JSONObject(full);
		Iterator<String> keys = object.keys();
		
		while(keys.hasNext()) {
			String key=keys.next();
			
			if(key.equals("Time")) {
				JSONObject time = object.getJSONObject(key);
				Iterator<String> timeKeys = time.keys();
				
				while(timeKeys.hasNext()) {
					String timeKey=timeKeys.next();

					if(timeKey.equals("Hours")) {
						HoursGroup group = new HoursGroup(time.getJSONObject(timeKey));
						Point loc = new Point((int)group.getLocation().getX()*3,(int)group.getLocation().getY()*3);
						parent.addElement(group,loc);
						parent.mainMenu.setEnabled(group, false);
					}
					else if(timeKey.equals("Minutes")) {
						MinutesGroup group = new MinutesGroup(time.getJSONObject(timeKey));
						Point loc = new Point((int)group.getLocation().getX()*3,(int)group.getLocation().getY()*3);
						parent.addElement(group,loc);
						parent.mainMenu.setEnabled(group, false);
					}
					else if(timeKey.equals("Seconds")) {
						SecondsGroup group = new SecondsGroup(time.getJSONObject(timeKey));
						Point loc = new Point((int)group.getLocation().getX()*3,(int)group.getLocation().getY()*3);
						parent.addElement(group,loc);
						parent.mainMenu.setEnabled(group, false);
					}
				}
			}
			else if(key.equals("Date")) {
				JSONObject date = object.getJSONObject(key);
				Iterator<String> dateKeys = date.keys();
				
				while(dateKeys.hasNext()) {
					String dateKey=dateKeys.next();

					if(dateKey.equals("MonthAndDay")) {
						MonthAndDay group = new MonthAndDay(date.getJSONObject(dateKey));
						Point loc = new Point((int)group.getLocation().getX()*3,(int)group.getLocation().getY()*3);
						int w=(int) ((group.getBottomRightPoint().getX()*3) - loc.getX());
						int h=(int) ((group.getBottomRightPoint().getY()*3) - loc.getY());
						parent.addElement(group,w,h,loc);
						parent.mainMenu.setEnabled(group, false);
					}
					else if(dateKey.equals("WeekDay")) {
						WeekDay weekDay = new WeekDay(date.getJSONObject(dateKey));
						Point loc = new Point((int)weekDay.getLocation().getX()*3,(int)weekDay.getLocation().getY()*3);
						parent.addElement(weekDay,loc);
						parent.mainMenu.setEnabled(weekDay, false);
					}
					else if(dateKey.equals("DayAmPm")) {
						AmPm dayAmPm = new AmPm(date.getJSONObject(dateKey));
						Point loc = new Point((int)dayAmPm.getLocation().getX()*3,(int)dayAmPm.getLocation().getY()*3);
						parent.addElement(dayAmPm,loc);
						parent.mainMenu.setEnabled(dayAmPm, false);
					}
				}
			}
			else if(key.equals("Activity")) {
				JSONObject date = object.getJSONObject(key);
				Iterator<String> dateKeys = date.keys();
				
				while(dateKeys.hasNext()) {
					String dateKey=dateKeys.next();

					if(dateKey.equals("Steps")) {
						Steps group = new Steps(date.getJSONObject(dateKey));
						Point loc = new Point((int)group.getLocation().getX()*3,(int)group.getLocation().getY()*3);
						int w=(int) ((group.getBottomRightPoint().getX()*3) - loc.getX());
						int h=(int) ((group.getBottomRightPoint().getY()*3) - loc.getY());
						parent.addElement(group,w,h,loc);
						parent.mainMenu.setEnabled(group, false);
					}
					else if(dateKey.equals("StepsGoal")) {
						StepsGoal group = new StepsGoal(date.getJSONObject(dateKey));
						Point loc = new Point((int)group.getLocation().getX()*3,(int)group.getLocation().getY()*3);
						int w=(int) ((group.getBottomRightPoint().getX()*3) - loc.getX());
						int h=(int) ((group.getBottomRightPoint().getY()*3) - loc.getY());
						parent.addElement(group,w,h,loc);
						parent.mainMenu.setEnabled(group, false);
					}
					else if(dateKey.equals("Calories")) {
						Calories group = new Calories(date.getJSONObject(dateKey));
						Point loc = new Point((int)group.getLocation().getX()*3,(int)group.getLocation().getY()*3);
						int w=(int) ((group.getBottomRightPoint().getX()*3) - loc.getX());
						int h=(int) ((group.getBottomRightPoint().getY()*3) - loc.getY());
						parent.addElement(group,w,h,loc);
						parent.mainMenu.setEnabled(group, false);
					}
					else if(dateKey.equals("Pulse")) {
						Pulse group = new Pulse(date.getJSONObject(dateKey));
						Point loc = new Point((int)group.getLocation().getX()*3,(int)group.getLocation().getY()*3);
						int w=(int) ((group.getBottomRightPoint().getX()*3) - loc.getX());
						int h=(int) ((group.getBottomRightPoint().getY()*3) - loc.getY());
						parent.addElement(group,w,h,loc);
						parent.mainMenu.setEnabled(group, false);
					}
					else if(dateKey.equals("Distance")) {
						Distance group = new Distance(date.getJSONObject(dateKey));
						Point loc = new Point((int)group.getLocation().getX()*3,(int)group.getLocation().getY()*3);
						int w=(int) ((group.getBottomRightPoint().getX()*3) - loc.getX());
						int h=(int) ((group.getBottomRightPoint().getY()*3) - loc.getY());
						parent.addElement(group,w,h,loc);
						parent.mainMenu.setEnabled(group, false);
					}
				}
			}
			else if(key.equals("Status")) {
				JSONObject status = object.getJSONObject(key);
				Iterator<String> statusKeys = status.keys();
				
				while(statusKeys.hasNext()) {
					String statusKey=statusKeys.next();
					
					if(statusKey.equals("Battery")) {
						JSONObject battery = object.getJSONObject(statusKey);
						Iterator<String> batteryKeys = battery.keys();
						
						while(batteryKeys.hasNext()) {
							String batteryKey=batteryKeys.next();
							
							if(batteryKey.equals("Text")) {
								BatteryText group = new BatteryText(battery.getJSONObject(batteryKey));
								Point loc = new Point((int)group.getLocation().getX()*3,(int)group.getLocation().getY()*3);
								int w=(int) ((group.getBottomRightPoint().getX()*3) - loc.getX());
								int h=(int) ((group.getBottomRightPoint().getY()*3) - loc.getY());
								parent.addElement(group,w,h,loc);
								parent.mainMenu.setEnabled(group, false);
							}
							else if(statusKey.equals("Icon")) {
								BatteryIcon group = new BatteryIcon(battery.getJSONObject(batteryKey));
								Point loc = new Point((int)group.getLocation().getX()*3,(int)group.getLocation().getY()*3);
								parent.addElement(group,loc);
								parent.mainMenu.setEnabled(group, false);
							}
						}
					}

					if(statusKey.equals("DoNotDisturb")) {
						Switch group = new Switch("DoNotDisturb",status.getJSONObject(statusKey));
						Point loc = new Point((int)group.getLocation().getX()*3,(int)group.getLocation().getY()*3);
						parent.addElement(group,loc);
						parent.mainMenu.setEnabled(group, false);
					}
					else if(statusKey.equals("Bluetooth")) {
						Switch group = new Switch("Bluetooth",status.getJSONObject(statusKey));
						Point loc = new Point((int)group.getLocation().getX()*3,(int)group.getLocation().getY()*3);
						parent.addElement(group,loc);
						parent.mainMenu.setEnabled(group, false);
					}
					else if(statusKey.equals("Lock")) {
						Switch group = new Switch("Lock",status.getJSONObject(statusKey));
						Point loc = new Point((int)group.getLocation().getX()*3,(int)group.getLocation().getY()*3);
						parent.addElement(group,loc);
						parent.mainMenu.setEnabled(group, false);
					}
				}
			}
		}
		br = new BufferedReader(new FileReader(new File(folder.getAbsolutePath()+"/bgItems.list")));
		full = "";
		line = "";
		while((line=br.readLine())!=null) {
			full+="\r\n"+line;
		}
		br.close();
		
		if(full.length() == 0) {
			return;
		}
		
		String[] items = full.split("\\|\\|");
		for(String item : items) {
			BGItem bg = new BGItem(item);
			Point loc = new Point(bg.getLocation().x*3,bg.getLocation().y*3);
			System.out.println(bg.getLocation());
			System.out.println(loc);
			parent.addBGItem(bg,loc);
		}
	}
	
}
