package helpers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

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
import main.MiBand4Editor;

import java.awt.Image;

public class Builder {
	
	public void generateJSON(ArrayList<Element> elements,int bgX, int bgY) throws IOException {
		StringBuilder builder = new StringBuilder("{\r\n" + 
				"  \"Background\": {\r\n" + 
				"    \"Image\": {\r\n" + 
				"      \"X\": "+bgX+",\r\n" + 
				"      \"Y\": "+bgY+",\r\n" + 
				"      \"ImageIndex\": 0\r\n" + 
				"    }\r\n" + 
				"  }");
		
		ArrayList<Element> timeElements = new ArrayList<Element>();
		ArrayList<Element> dateElements = new ArrayList<Element>();
		ArrayList<Element> activityElements = new ArrayList<Element>();
		ArrayList<Element> statusElements = new ArrayList<Element>();
		ArrayList<Element> batteryElements = new ArrayList<Element>();
		
		for(Element element : elements) {
			if(element instanceof HoursGroup || element instanceof MinutesGroup || element instanceof SecondsGroup) {
				timeElements.add(element);
			}
			else if(element instanceof AmPm || element instanceof MonthAndDay || element instanceof WeekDay) {
				dateElements.add(element);
			}
			else if(element instanceof Steps || element instanceof StepsGoal || element instanceof Calories || element instanceof Pulse || element instanceof Distance) {
				activityElements.add(element);
			}
			else if(element instanceof Switch) {
				statusElements.add(element);
			}
			else if(element instanceof BatteryText || element instanceof BatteryIcon) {
				batteryElements.add(element);
			}
		}
		
		boolean first=true;
		
		builder.append(",\r\n"
				+ "  \"Time\": {\r\n");
		for(Element element : timeElements) {
			if(!first) {
				builder.append(",\r\n");
			}
			builder.append(element.getJSON());
			first=false;
		}
		builder.append("\r\n  }");
		
		first=true;
		
		builder.append(",\r\n"
				+ "  \"Date\": {\r\n");
		for(Element element : dateElements) {
			if(!first) {
				builder.append(",\r\n");
			}
			builder.append(element.getJSON());
			first=false;
		}
		builder.append("\r\n  }");
		
		first=true;
		
		builder.append(",\r\n"
				+ "  \"Activity\": {\r\n");
		for(Element element : activityElements) {
			if(!first) {
				builder.append(",\r\n");
			}
			builder.append(element.getJSON());
			first=false;
		}
		builder.append("\r\n  }");
		
		first=true;
		
		builder.append(",\r\n"
				+ "  \"Status\": {\r\n");
		for(Element element : statusElements) {
			if(!first) {
				builder.append(",\r\n");
			}
			builder.append(element.getJSON());
			first=false;
		}
		if(!first) {
			builder.append(",\r\n");
		}
		builder.append("    \"Battery\": {\r\n");
		builder.append("      \"BatteryConfig\": {\r\n");
		builder.append("        \"BoxWidth\": 0,\r\n");
		builder.append("        \"Alignment\": 0,\r\n");
		builder.append("        \"Order\": 0\r\n");
		builder.append("      }\r\n");
		for(Element element : batteryElements) {
			builder.append(",\r\n");
			builder.append(element.getJSON());
		}
		builder.append("\r\n    }");
		builder.append("\r\n  }");
		
		builder.append("\r\n}");
		
		System.out.println(builder.toString());
		
		PrintWriter pw = new PrintWriter(new FileWriter(new File(MiBand4Editor.currentPath.getAbsolutePath()+"/watchface.json")));
		pw.write(builder.toString());
		pw.flush();
		pw.close();
		
		pw = new PrintWriter(new FileWriter(new File(MiBand4Editor.currentPath.getAbsolutePath()+"/real_watchface.json")));
		pw.write(builder.toString());
		pw.flush();
		pw.close();
	}
	
	public void runBuilder() throws IOException, InterruptedException {
		String command = "\""+MiBand4Editor.toolPath+"\\WatchFace.exe\"";
		command+=" \""+MiBand4Editor.currentPath.getAbsolutePath()+"\\watchface.json\"";
		Process p = Runtime.getRuntime().exec(command);
		new Thread(new Runnable() {
			@Override
			public void run() {
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line = "";
				try {
					while((line = br.readLine())!=null) {
						System.out.println("OUT"+line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		p.waitFor();
	}

	public File[][] disableUnneededImgs(ArrayList<Element> elements) throws IOException {
		String absolutePath = MiBand4Editor.currentPath.getAbsolutePath();
		new File(absolutePath+"\\TEMP\\").mkdir();
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				String[] parts = name.split("\\.");
				if(parts.length!=2) {
					return false;
				}
				try {
					int no = Integer.parseInt(parts[0]);
					if(no==0) {
						return false;
					}
				}
				catch(NumberFormatException e) {
					return false;
				}
				if(!parts[1].equals("png")) {
					return false;
				}
				return true;
			}
		};
		File[] filesArray = new File(absolutePath).listFiles(filter);
		ArrayList<File> files = new ArrayList<File>(Arrays.asList(filesArray));
		ArrayList<File[]> newFiles = new ArrayList<File[]>();
		
		for(Element element : elements) {
			File[] usedImgs = element.getUsedImgs();
			for(File usedImg : usedImgs) {
				files.remove(usedImg);
			}
		}
		for(File file : files) {
			String[] list = file.getAbsolutePath().split("\\\\");
			String newPath = absolutePath+"\\TEMP\\"+list[list.length-1];
			Files.move(Paths.get(file.getAbsolutePath()), Paths.get(newPath));
			newFiles.add(new File[] {new File(file.getAbsolutePath()),new File(newPath)});
		}
		
		File[] remainingFiles = new File(absolutePath).listFiles(filter);
		Arrays.sort(remainingFiles);
		System.out.println(remainingFiles.length);
		for(int i=1;i<=remainingFiles.length;i++) {
			File f = remainingFiles[i-1];
			String[] path = f.getAbsolutePath().split("\\\\");
			String name = path[path.length-1].split("\\.")[0];
			int no = Integer.parseInt(name);
			System.out.println(path[path.length-1]);
			if(no!=i) {
				System.out.println("MOVE DOWN: "+no+" to "+i);
				String iFormatted = String.format("%04d", i);
				Files.move(Paths.get(f.getAbsolutePath()), Paths.get(absolutePath+"\\"+iFormatted+".png"));
				newFiles.add(new File[] {new File(f.getAbsolutePath()),new File(absolutePath+"\\"+iFormatted+".png")});
				//Replace in JSON
				Path pathJSON = Paths.get(MiBand4Editor.currentPath.getAbsolutePath()+"/watchface.json");

				String content = new String(Files.readAllBytes(pathJSON));
				content = content.replaceAll("\"ImageIndex\": "+no, "\"ImageIndex\": "+i);
				content = content.replaceAll("\"DelimiterImageIndex\": "+no, "\"DelimiterImageIndex\": "+i);
				content = content.replaceAll("\"NoDataImageIndex\": "+no, "\"NoDataImageIndex\": "+i);
				content = content.replaceAll("\"SuffixImageIndex\": "+no, "\"SuffixImageIndex\": "+i);
				content = content.replaceAll("\"DecimalPointImageIndex\": "+no, "\"DecimalPointImageIndex\": "+i);
				content = content.replaceAll("\"ImageIndexAMEN\": "+no, "\"ImageIndexAMEN\": "+i);
				content = content.replaceAll("\"ImageIndexPMEN\": "+no, "\"ImageIndexPMEN\": "+i);
				content = content.replaceAll("\"ImageIndexAMCN\": "+no, "\"ImageIndexAMCN\": "+i);
				content = content.replaceAll("\"ImageIndexPMCN\": "+no, "\"ImageIndexPMCN\": "+i);
				content = content.replaceAll("\"ImageIndexOn\": "+no, "\"ImageIndexOn\": "+i);
				content = content.replaceAll("\"ImageIndexOff\": "+no, "\"ImageIndexOff\": "+i);
				Files.write(pathJSON, content.getBytes());
			}
		}
		
		
		//Reverse array
		File[][] array = newFiles.toArray(new File[0][2]);
		for(int i=0;i<array.length/2;i++) {
			File[] tempF = array[i];
			array[i] = array[array.length-i-1];
			array[array.length-i-1] = tempF;
		}
		
		return array; //OldPath -> NewPath
	}
	
	public void reenableImgs(File[][] imgs) throws IOException {
		for(File[] img : imgs) {
			Files.move(Paths.get(img[1].getAbsolutePath()),Paths.get(img[0].getAbsolutePath()));
		}
		String absolutePath = MiBand4Editor.currentPath.getAbsolutePath();
		new File(absolutePath+"\\TEMP\\").delete();
	}

	public void generateBackground(BGItem[] items) throws IOException {
		Image background = StaticHelpers.getUnscaledBufferedImage("background");
		BufferedImage all = new BufferedImage(background.getWidth(null),background.getHeight(null),BufferedImage.TYPE_BYTE_INDEXED);

		Graphics g = all.getGraphics();
		g.drawImage(background, 0, 0, null);
		for(BGItem item : items) {
			g.drawImage(item.getImage().getScaledInstance(item.getImage().getWidth(null)/3, item.getImage().getHeight(null)/3, Image.SCALE_SMOOTH), item.getX(), item.getY(), null);
		}
		
		String absolutePath = MiBand4Editor.currentPath.getAbsolutePath()+"\\0000.png";
		
		BufferedImage allReduced = new BufferedImage(all.getWidth(),all.getHeight(),BufferedImage.TYPE_INT_RGB);
		g=allReduced.getGraphics();
		g.drawImage(all, 0, 0, null);
		
		ImageIO.write(allReduced, "PNG", new File(absolutePath));
		
		StringBuilder builder = new StringBuilder();
		boolean first=true;
		for(BGItem item : items) {
			if(!first) {
				builder.append("||\r\n");
			}
			builder.append(item.getJSON());
			first=false;
		}
		
		PrintWriter pw = new PrintWriter(new FileWriter(new File(MiBand4Editor.currentPath.getAbsolutePath()+"\\bgItems.list")));
		pw.write(builder.toString());
		pw.flush();
		pw.close();
	}

	public static void writeEmptyWatchface(File file) throws IOException {
		StringBuilder builder = new StringBuilder("{\r\n" + 
				"  \"Background\": {\r\n" + 
				"    \"Image\": {\r\n" + 
				"      \"X\": 0,\r\n" + 
				"      \"Y\": 0,\r\n" + 
				"      \"ImageIndex\": 0\r\n" + 
				"    }\r\n" + 
				"  }");
		
		builder.append(",\r\n"
				+ "  \"Time\": {\r\n");
		builder.append("\r\n  }");
		
		builder.append(",\r\n"
				+ "  \"Date\": {\r\n");
		builder.append("\r\n  }");
		
		builder.append(",\r\n"
				+ "  \"Activity\": {\r\n");
		builder.append("\r\n  }");
		
		builder.append(",\r\n"
				+ "  \"Status\": {\r\n");
		builder.append("\r\n  }");
		
		builder.append("\r\n}");
		
		PrintWriter pw = new PrintWriter(new FileWriter(file));
		pw.write(builder.toString());
		pw.flush();
		pw.close();
	}

}
