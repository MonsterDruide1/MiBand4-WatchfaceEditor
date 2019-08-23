package display;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import elements.BGItem;
import helpers.Builder;
import helpers.StaticHelpers;
import main.MiBand4Editor;

public class TopMenu implements ActionListener{
	
	//TODO Zuletzt Verwendet,     List of Warnings,         Pfad des Programms
	
	MiBand4Editor parent;
	
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenu settingsMenu;
	
	public TopMenu(MiBand4Editor parent) {
		this.parent = parent;
		menuBar = new JMenuBar();
		
		{
			fileMenu = new JMenu(parent.getInLang("topMenu_file"));

			JMenuItem newProj = getMenuItem("topMenu_file_new");
			newProj.setMnemonic(KeyEvent.VK_N);
			newProj.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
			fileMenu.add(newProj);

			JMenuItem open = getMenuItem("topMenu_file_open");
			open.setMnemonic(KeyEvent.VK_O);
			open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
			fileMenu.add(open);

			JMenuItem save = getMenuItem("topMenu_file_save");
			save.setMnemonic(KeyEvent.VK_S);
			save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
			fileMenu.add(save);
			
			JMenuItem saveAs = getMenuItem("topMenu_file_saveAs");
			saveAs.setMnemonic(KeyEvent.VK_S);
			saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
			fileMenu.add(saveAs);
			
			JMenuItem generate = getMenuItem("topMenu_file_generate");
			generate.setMnemonic(KeyEvent.VK_G);
			generate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,ActionEvent.CTRL_MASK));
			fileMenu.add(generate);
			
			JMenuItem importOpen = getMenuItem("topMenu_file_import");
			importOpen.setMnemonic(KeyEvent.VK_I);
			importOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,ActionEvent.CTRL_MASK));
			fileMenu.add(importOpen);
			
			menuBar.add(fileMenu);
		}
		
		{
			settingsMenu = new JMenu(parent.getInLang("topMenu_settings"));
			
			JMenuItem language = getMenuItem("topMenu_settings_language");
			settingsMenu.add(language);
			
			JMenuItem toolsPath = getMenuItem("topMenu_settings_toolPath");
			settingsMenu.add(toolsPath);
			
			menuBar.add(settingsMenu);
		}
	}

	private JMenuItem getMenuItem(String string) {
		JMenuItem item = new JMenuItem(parent.getInLang(string));
		item.addActionListener(this);
		return item;
	}

	public void applyTo(JFrame main) {
		main.setJMenuBar(menuBar);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String title = e.getActionCommand();
		if(title.equals(parent.getInLang("topMenu_file_new"))) {
			newProject();
		}
		else if(title.equals(parent.getInLang("topMenu_file_open"))) {
			openProject();
		}
		else if(title.equals(parent.getInLang("topMenu_file_save"))) {
			save();
		}
		else if(title.equals(parent.getInLang("topMenu_file_saveAs"))) {
			saveAs();
		}
		else if(title.equals(parent.getInLang("topMenu_file_generate"))) {
			generate();
		}
		else if(title.equals(parent.getInLang("topMenu_file_import"))) {
			importOpen();
		}

		else if(title.equals(parent.getInLang("topMenu_settings_language"))) {
			languageSettings();
		}
		else if(title.equals(parent.getInLang("topMenu_settings_toolPath"))) {
			toolPathSettings();
		}
		
		menuBar.setSelected(null);
	}

	private void newProject() {
		StaticHelpers.setWinLookAndFeel();
		JFileChooser f = new JFileChooser();
		f.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if(f.isDirectory()) {
					return true;
				}
				return false;
			}
			@Override
			public String getDescription() {
				return null;
			}
		});
		f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		f.setAcceptAllFileFilterUsed(false);
		f.showOpenDialog(null);
		StaticHelpers.setJavaLookAndFeel();
		File selected = f.getSelectedFile();
		if(selected.list().length>0) {
			JOptionPane.showMessageDialog(null, parent.getInLang("error_dirNotEmpty"));
			newProject();
			return;
		}
		
		BufferedImage background = new BufferedImage(120,240,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = background.createGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, 120, 240);
		
		try {
			ImageIO.write(background, "png", new File(selected.getAbsolutePath()+"\\background.png"));
			
			File bgItemsList = new File(selected.getAbsolutePath()+"\\bgItems.list");
			if(!bgItemsList.exists()) {
				bgItemsList.createNewFile();
			}
			
			Builder.writeEmptyWatchface(new File(selected.getAbsolutePath()+"\\real_watchface.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		parent.restartToProject(selected);
	}

	private void openProject() {
		StaticHelpers.setWinLookAndFeel();
		JFileChooser f = new JFileChooser();
		f.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if(f.isDirectory()) {
					return true;
				}
				return false;
			}
			@Override
			public String getDescription() {
				return null;
			}
		});
		f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		f.setAcceptAllFileFilterUsed(false);
		f.showOpenDialog(null);
		StaticHelpers.setJavaLookAndFeel();
		parent.restartToProject(f.getSelectedFile());
	}
	
	private void save() {
		try {
			Builder builder = new Builder();
			builder.generateBackground(parent.bgItems.toArray(new BGItem[0]));
			builder.generateJSON(parent.elements);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void saveAs() {
		StaticHelpers.setWinLookAndFeel();
		JFileChooser f = new JFileChooser();
		f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		f.setAcceptAllFileFilterUsed(false);
		f.showOpenDialog(null);
		StaticHelpers.setJavaLookAndFeel();
		File selected = f.getSelectedFile();
		
		for(File file : MiBand4Editor.currentPath.listFiles()) {
			String[] name = file.getAbsolutePath().split("\\\\");
			try {
				Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(selected.getAbsolutePath()+"\\"+name[name.length-1]));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		MiBand4Editor.currentPath = selected;
		Builder builder = new Builder();
		try {
			builder.generateBackground(parent.bgItems.toArray(new BGItem[0]));
			builder.generateJSON(parent.elements);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		parent.restartToProject(selected);
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

	private void importOpen() {
		StaticHelpers.setWinLookAndFeel();
		JFileChooser f = new JFileChooser();
		f.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if(f.isDirectory()) {
					return true;
				}
				String[] name = f.getAbsolutePath().split("\\.");
				System.out.println(name[name.length-1]);
				if(name[name.length-1].equals("bin")) {
					return true;
				}
				return false;
			}
			@Override
			public String getDescription() {
				return null;
			}
		});
		f.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		f.setAcceptAllFileFilterUsed(false);
		f.showOpenDialog(null);
		StaticHelpers.setJavaLookAndFeel();
		File selected = f.getSelectedFile();
		String[] name = selected.getAbsolutePath().split("\\.");
		if(name[name.length-1].equals("bin")) {
			try {
				String command = "\""+MiBand4Editor.toolPath+"\\WatchFace.exe\"";
				command+=" \""+selected.getAbsolutePath()+"\"";
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
			catch(Exception e) {
				e.printStackTrace();
			}
			selected = new File(selected.getAbsolutePath().substring(0,selected.getAbsolutePath().length()-4));
		}
		if(selected.isDirectory()) {
			File[] jsons = selected.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File f, String fName) {
					System.out.println(fName);
					String[] name = fName.split("\\.");
					System.out.println(name[name.length-1]);
					if(name[name.length-1].equals("json")) {
						System.out.println("YES");
						return true;
					}
					return false;
				}
			});
			File correct = jsons[0];
			try {
				Files.copy(Paths.get(correct.getAbsolutePath()),Paths.get(selected.getAbsolutePath()+"\\real_watchface.json"));
				Files.move(Paths.get(correct.getAbsolutePath()),Paths.get(selected.getAbsolutePath()+"\\watchface.json"));
				
				File bgItemsList = new File(selected.getAbsolutePath()+"\\bgItems.list");
				if(!bgItemsList.exists()) {
					bgItemsList.createNewFile();
				}
				Files.copy(Paths.get(selected.getAbsolutePath()+"\\0000.png"),Paths.get(selected.getAbsolutePath()+"\\background.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		parent.restartToProject(selected);
	}

	private void languageSettings() {
		JDialog dialog = new JDialog();
		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel,BoxLayout.Y_AXIS);
		panel.setLayout(layout);
		dialog.setTitle(parent.getInLang("settings_language_title"));
		
		JLabel label = new JLabel(parent.getInLang("settings_language_text"));
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JComboBox<String> box = new JComboBox<String>();
		box.addItem("English");
		box.addItem("Deutsch");
		box.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String shortName = "";
				switch(box.getSelectedIndex()) {
				case 0:
					shortName="en";
					break;
				case 1:
					shortName="de";
					break;
				}
				parent.readLanguage(shortName);
				label.setText(parent.getInLang("settings_language_text"));
			}
		});
		box.setAlignmentX(Component.CENTER_ALIGNMENT);
		box.setSelectedItem(parent.getInLang("ownLanguageName"));
		
		JButton button = new JButton(parent.getInLang("settings_language_ok"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		panel.add(label);
		panel.add(box);
		panel.add(button);
		dialog.add(panel);
		
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);
		dialog.setVisible(true);
		parent.restartAfterLanguageChange();
	}
	
	public void toolPathSettings() {
		StaticHelpers.setWinLookAndFeel();
		JFileChooser f = new JFileChooser();
		f.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if(f.isDirectory()) {
					return true;
				}
				return false;
			}
			@Override
			public String getDescription() {
				return null;
			}
		});
		f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		f.setAcceptAllFileFilterUsed(false);
		f.showOpenDialog(null);
		StaticHelpers.setJavaLookAndFeel();
		MiBand4Editor.toolPath=f.getSelectedFile();
	}

}
