package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import display.EditMenu;
import display.MainMenu;
import display.Mover;
import display.SnapPanel;
import display.TopMenu;
import elements.BGItem;
import elements.Element;
import elements.activity.Calories;
import elements.activity.Distance;
import elements.activity.Pulse;
import elements.activity.Steps;
import elements.activity.StepsGoal;
import elements.date.AmPm;
import elements.date.OneLineMonthAndDay;
import elements.date.SeparateDay;
import elements.date.SeparateMonth;
import elements.date.WeekDay;
import elements.status.BatteryIcon;
import elements.status.BatteryText;
import elements.status.Switch;
import elements.time.TwoDigits;
import helpers.StaticHelpers;

public class MiBand4Editor implements MouseListener, KeyListener{
	
    public Point lastClick = new Point(-1, -1);
    
    public SnapPanel panel;
    private Mover mover;
    private JFrame main;
    public MainMenu mainMenu;
    private TopMenu topMenu;
    
    public ArrayList<Element> elements = new ArrayList<>();
    public ArrayList<BGItem> bgItems = new ArrayList<>();
    
    private HashMap<String,String> language;

    public JPanel[] selected=null;
    public JPanel[] lastSelected=null;
    
    public static File currentPath;

	public static File toolPath;
    
    public boolean ctrlPressed = false;

	public static void main(String[] args) {
		new MiBand4Editor();
	}
	
	public MiBand4Editor() {
		readLanguage("en");
        currentPath=new File("data/");
		main = new JFrame();
		try {
			panel = new SnapPanel(this);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
        panel.addMouseListener(this);
        panel.addKeyListener(this);
        main.addKeyListener(this);
        mover = new Mover(panel,this);
        mainMenu = new MainMenu(this);
        topMenu = new TopMenu(this);

        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setLayout(null);
        main.add(panel);
        
        toolPath=new File("MiBandWFTool/");
        
		try {
			while(!isValidToolpath(toolPath)) {
				JOptionPane.showMessageDialog(null, getInLang("select_toolPath"));
				StaticHelpers.setWinLookAndFeel();
				JFileChooser f = new JFileChooser();
				f = new JFileChooser();
				f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				f.setAcceptAllFileFilterUsed(false);
				f.showOpenDialog(null);
				toolPath=f.getSelectedFile();
				StaticHelpers.setJavaLookAndFeel();
			}
		} catch (HeadlessException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
        try {
        	if(currentPath.exists()) {
    			StaticHelpers.parseWatchface(this,currentPath);
        	}
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        topMenu.applyTo(main);
        main.setSize(366,772);
        main.setLocationRelativeTo(null);
        main.setResizable(false);
        main.setVisible(true);
	}
	
	public boolean isValidToolpath(File toolPath) throws IOException {
		if(!toolPath.exists()) {
			return false;
		}
		if(!toolPath.isDirectory()) {
			return false;
		}
		
		Process p = Runtime.getRuntime().exec(toolPath.getAbsolutePath()+"\\Watchface.exe");
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = br.readLine();
		if(line.equals("  Mi Band 4 Watchface Edited by PG 1.3.9")) {
			return true;
		}
		return false;
	}

	public void readLanguage(String shortName) {
		language = StaticHelpers.parseLanguage("lang/"+shortName+".xml");
	}
	
    public void addElement(Element e, int w, int h) throws IOException {
    	addElement(e,w,h,lastClick);
    }
	
    public void addComponent(Component c, Point location) {
        c.addMouseListener(this);
        
        c.setBounds(location.x, location.y, c.getWidth(), c.getHeight());
        
        panel.add(c);
        mover.register(c);

        panel.setVisible(true);
        main.setVisible(true);
        
        refresh();
    }
    
    public void addElement(Element e) throws IOException {
    	addElement(e,lastClick);
    }
    
    public void addElement(Element e,Point location) throws IOException {
    	int index = elements.size();
    	elements.add(e);
    	
    	JPanel p = e.getPreview();
    	p.setName(p.getName()+"||"+index);
    	
    	addComponent(p,location);
    }
    
    public void addElement(Element e, int w, int h, Point location) throws IOException {
    	int index = elements.size();
    	elements.add(e);

    	if(e instanceof OneLineMonthAndDay) {
        	JPanel p = (JPanel) ((OneLineMonthAndDay)e).getPreview(w,h);
        	p.setName(p.getName()+"||"+index);
        	addComponent(p,location);
    	}
    	else if(e instanceof SeparateMonth) {
        	JPanel p = (JPanel) ((SeparateMonth)e).getPreview(w,h);
        	p.setName(p.getName()+"||"+index);
        	addComponent(p,location);
    	}
    	else if(e instanceof SeparateDay) {
        	JPanel p = (JPanel) ((SeparateDay)e).getPreview(w,h);
        	p.setName(p.getName()+"||"+index);
        	addComponent(p,location);
    	}
    	else if(e instanceof Steps){
    		JPanel p = ((Steps)e).getPreview(w, h);
        	p.setName(p.getName()+"||"+index);
        	addComponent(p,location);
    	}
    	else if(e instanceof StepsGoal){
    		JPanel p = ((StepsGoal)e).getPreview(w, h);
        	p.setName(p.getName()+"||"+index);
        	addComponent(p,location);
    	}
    	else if(e instanceof Calories){
    		JPanel p = ((Calories)e).getPreview(w, h);
        	p.setName(p.getName()+"||"+index);
        	addComponent(p,location);
    	}
    	else if(e instanceof Pulse){
    		JPanel p = ((Pulse)e).getPreview(w, h);
        	p.setName(p.getName()+"||"+index);
        	addComponent(p,location);
    	}
    	else if(e instanceof Distance){
    		JPanel p = ((Distance)e).getPreview(w, h);
        	p.setName(p.getName()+"||"+index);
        	addComponent(p,location);
    	}
    	else if(e instanceof Switch) {
    		JPanel p = ((Switch)e).getPreview();
        	p.setName(p.getName()+"||"+index);
        	addComponent(p,location);
    	}
    	else if(e instanceof BatteryText) {
    		JPanel p = ((BatteryText)e).getPreview(w,h);
        	p.setName(p.getName()+"||"+index);
        	addComponent(p,location);
    	}
    	else {
    		System.out.println("AddElement notFound "+e.getClass());
    	}
    }
	
    private void handleModify(MouseEvent e) {
        Component c = e.getComponent();
        
        if(c.getName().startsWith(TwoDigits.class.toString()) || 
        		c.getName().startsWith(AmPm.class.toString()) || 
        		c.getName().startsWith(OneLineMonthAndDay.class.toString()) ||
        		c.getName().startsWith(BGItem.class.toString()) ||
        		c.getName().startsWith(WeekDay.class.toString()) ||
        		c.getName().startsWith(Steps.class.toString()) || 
        		c.getName().startsWith(StepsGoal.class.toString()) ||
        		c.getName().startsWith(Calories.class.toString()) ||
        		c.getName().startsWith(Pulse.class.toString()) ||
        		c.getName().startsWith(Distance.class.toString()) ||
        		c.getName().startsWith(Switch.class.toString()) ||
        		c.getName().startsWith(BatteryText.class.toString()) ||
        		c.getName().startsWith(BatteryIcon.class.toString())) {
        	new EditMenu(c,this).show(panel, lastClick.x+e.getComponent().getLocation().x, lastClick.y+e.getComponent().getLocation().y);
        }
    }
    
    public void refresh() {
    	panel.revalidate();
    	panel.repaint();
        main.revalidate();
        main.repaint();
    }

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
        	if(!e.getComponent().getName().equals("SnapPanel")) {
        		if(e.getComponent().getKeyListeners().length==0) {
            		if(ctrlPressed) {
            			JPanel[] newSelected = new JPanel[lastSelected.length+1];
						System.arraycopy(lastSelected, 0, newSelected, 0, lastSelected.length);
            			newSelected[newSelected.length-1]=(JPanel)e.getComponent();
            			
            			setSelected(newSelected);
            		}
            		else {
            			setSelected(new JPanel[] {(JPanel)e.getComponent()});
            		}
        		}
        		else if(ctrlPressed) {
        			JPanel[] newSelected = new JPanel[lastSelected.length-1];
        			JPanel searched = (JPanel) e.getComponent();
        			int indexMinus=0;
        			for(int i=0;i<lastSelected.length;i++) {
        				if(lastSelected[i].equals(searched)) {
        					indexMinus=1;
        				}
        				else {
            				newSelected[i-indexMinus]=lastSelected[i];
        				}
        			}
        			
        			setSelected(newSelected);
        		}
        	}
        	else if((!panel.selecting) && (!ctrlPressed)){
        		setSelected(null);
        	}
        }
	}
	@Override
	public void mouseReleased(MouseEvent e) {
        lastClick = e.getPoint();

        if (e.getButton() == MouseEvent.BUTTON3 && e.getComponent().getName().equals("SnapPanel"))
            mainMenu.show(e.getComponent(), e.getX(), e.getY());
        if (e.getButton() == MouseEvent.BUTTON3) {
            handleModify(e);
        }
    }
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	public void setSelected(JPanel[] components) {
		if(lastSelected!=null) {
			for(JPanel panel : lastSelected) {
				panel.setBorder(null);
				panel.removeKeyListener(this);
				panel.transferFocusUpCycle();
				panel.setBounds(panel.getX()+1, panel.getY()+1, panel.getWidth()-2, panel.getHeight()-2);
			}
    		lastSelected=null;
    		refresh();
		}
		if(components!=null) {
			lastSelected = new JPanel[components.length];
			for(int i=0;i<components.length;i++) {
				lastSelected[i] = components[i];
				lastSelected[i].setBorder(BorderFactory.createLineBorder(Color.BLUE));
				lastSelected[i].setBounds(lastSelected[i].getX()-1, lastSelected[i].getY()-1, lastSelected[i].getWidth()+2, lastSelected[i].getHeight()+2);
				lastSelected[i].addKeyListener(this);
				lastSelected[i].requestFocus();
			}
			refresh();
		}
		selected=lastSelected;
	}

	public void setPreviewSelected(JPanel[] components) {
		if(selected!=null) {
			for(JPanel panel : selected) {
				panel.setBorder(null);
				panel.removeKeyListener(this);
				panel.transferFocusUpCycle();
				panel.setBounds(panel.getX()+1, panel.getY()+1, panel.getWidth()-2, panel.getHeight()-2);
			}
			selected=null;
    		refresh();
		}
		if(components!=null) {
			selected = new JPanel[components.length];
			for(int i=0;i<components.length;i++) {
				selected[i] = components[i];
				selected[i].setBorder(BorderFactory.createLineBorder(Color.BLUE));
				selected[i].setBounds(selected[i].getX()-1, selected[i].getY()-1, selected[i].getWidth()+2, selected[i].getHeight()+2);
				selected[i].addKeyListener(this);
				selected[i].requestFocus();
			}
			refresh();
		}
	}
	
	public String getInLang(String key) {
		return language.get(key);
	}

	public void addBGItem(BGItem e, Point point) throws IOException {
    	int index = bgItems.size();
    	bgItems.add(e);
    	
    	JPanel p = e.getPreview();
    	p.setName(p.getName()+"||"+index);
    	
    	addComponent(p,point);
	}

	public void addBGItem(BGItem e) throws IOException {
		addBGItem(e,lastClick);
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			for(JPanel panel : lastSelected) {
				panel.setBounds(panel.getX()+3,panel.getY(),panel.getWidth(),panel.getHeight());
				elements.get(Integer.parseInt(panel.getName().split("\\|\\|")[1])).setCoords((panel.getX()+3)/3, (panel.getY())/3);
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			for(JPanel panel : lastSelected) {
				panel.setBounds(panel.getX()-3,panel.getY(),panel.getWidth(),panel.getHeight());
				elements.get(Integer.parseInt(panel.getName().split("\\|\\|")[1])).setCoords((panel.getX()-3)/3, (panel.getY())/3);
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			for(JPanel panel : lastSelected) {
				panel.setBounds(panel.getX(),panel.getY()-3,panel.getWidth(),panel.getHeight());
				elements.get(Integer.parseInt(panel.getName().split("\\|\\|")[1])).setCoords((panel.getX())/3, (panel.getY()-3)/3);
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			for(JPanel panel : lastSelected) {
				panel.setBounds(panel.getX(),panel.getY()+3,panel.getWidth(),panel.getHeight());
				elements.get(Integer.parseInt(panel.getName().split("\\|\\|")[1])).setCoords((panel.getX())/3, (panel.getY()+3)/3);
			}
		}
		ctrlPressed = e.isControlDown();
		refresh();
	}
	@Override
	public void keyReleased(KeyEvent e) {
		ctrlPressed = e.isControlDown();
	}

	public void restartToProject(File folder) {
		elements.clear();
		bgItems.clear();
		lastSelected=null;
		panel.removeAll();
		mainMenu.enableAll();
		
		currentPath=folder;
		
		try {
			StaticHelpers.parseWatchface(this,folder);
			panel.readBGCoords();
		} catch (IOException e) {
			e.printStackTrace();
		}
		panel.bgNotFoundShown=false;
		refresh();
	}

	public void renameComponentsSince(Component componentDeleted) {
		int noOfDeleted = Integer.parseInt(componentDeleted.getName().split("\\|\\|")[1]);
		for(Component component : panel.getComponents()) {
			if(Integer.parseInt(component.getName().split("\\|\\|")[1])>noOfDeleted && !(component.getName().split("\\|\\|")[0].equals(BGItem.class.toString()))) {
				component.setName(component.getName().split("\\|\\|")[0]+"||"+(Integer.parseInt(component.getName().split("\\|\\|")[1])-1));
			}
		}
	}

	public void renameComponentsSinceBGItem(Component componentDeleted) {
		int noOfDeleted = Integer.parseInt(componentDeleted.getName().split("\\|\\|")[1]);
		for(Component component : panel.getComponents()) {
			if(Integer.parseInt(component.getName().split("\\|\\|")[1])>noOfDeleted && component.getName().split("\\|\\|")[0].equals(BGItem.class.toString())) {
				component.setName(component.getName().split("\\|\\|")[0]+"||"+(Integer.parseInt(component.getName().split("\\|\\|")[1])-1));
			}
		}
	}

	public void restartAfterLanguageChange() {
		topMenu=new TopMenu(this);
		topMenu.applyTo(main);
		mainMenu=new MainMenu(this);
		refresh();
	}
}
