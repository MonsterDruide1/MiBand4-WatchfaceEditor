package display;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import elements.BGItem;
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
import elements.time.TwoDigits;
import main.MiBand4Editor;

public class Mover implements MouseListener, MouseMotionListener {

    private boolean resize = false;
    private boolean drag = false;
    private int initX = 0;
    private int initY = 0;

    private Component component = null;
    private SnapPanel panel;

    private float resizeThreshold = 0.75f;
    
    private MiBand4Editor parent;

    public Mover(SnapPanel panel, MiBand4Editor parent) {
        this.panel = panel;
        this.parent=parent;
    }

    public void register(Component c) {
        c.addMouseListener(this);
        c.addMouseMotionListener(this);
    }

    public void mouseDragged(MouseEvent arg0) {
        if (drag) {
            Point dest = arg0.getLocationOnScreen();
            dest = new Point(dest.x - initX, dest.y - initY);
            
            Point delta = new Point(dest.x-component.getX(),dest.y-component.getY());
            
            if(parent.lastSelected == null) {
            	component.setBounds(dest.x, dest.y, component.getWidth(), component.getHeight());
                
                if(component.getName().startsWith(BGItem.class.toString())) {
                	parent.bgItems.get(Integer.parseInt(component.getName().split("\\|\\|")[1])).setCoords(dest.x/3, dest.y/3);
                }
                else {
                    parent.elements.get(Integer.parseInt(component.getName().split("\\|\\|")[1])).setCoords(dest.x/3, dest.y/3);
                }
            	return;
            }
            
        	for(JPanel panel : parent.lastSelected) {
        		dest = new Point(delta.x+panel.getX(),delta.y+panel.getY());
                
        		panel.setBounds(dest.x, dest.y, panel.getWidth(), panel.getHeight());
                
                if(panel.getName().startsWith(BGItem.class.toString())) {
                	parent.bgItems.get(Integer.parseInt(panel.getName().split("\\|\\|")[1])).setCoords(dest.x/3, dest.y/3);
                }
                else {
                    parent.elements.get(Integer.parseInt(panel.getName().split("\\|\\|")[1])).setCoords(dest.x/3, dest.y/3);
                }
        	}
            
            component.getParent().repaint();
            panel.revalidate();
            component.getParent().getParent().repaint();
        }

        if (resize) {
            Point dest = arg0.getLocationOnScreen();
            dest = new Point(dest.x - initX, dest.y - initY);

            component.setBounds(component.getX(), component.getY(), (int) arg0.getPoint().getX(), (int) arg0.getPoint().getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        Point p = arg0.getLocationOnScreen();


        component = arg0.getComponent();

        initX = p.x - component.getX();
        initY = p.y - component.getY();

        if (arg0.getPoint().getX() > component.getWidth() * resizeThreshold && arg0.getPoint().getY() > component.getHeight() * resizeThreshold) {
        	if(component.getName().startsWith(TwoDigits.class.toString())) {
            	drag=true;
            }
        	else if(component.getName().startsWith(AmPm.class.toString())) {
        		drag=true;
        	}
        	else if(component.getName().startsWith(WeekDay.class.toString())) {
        		drag=true;
        	}
        	else if(component.getName().startsWith(BGItem.class.toString())) {
        		drag=true;
        	}
        	else if(component.getName().startsWith(Switch.class.toString())) {
        		drag=true;
        	}
        	else if(component.getName().startsWith(BatteryIcon.class.toString())) {
        		drag=true;
        	}
            else {
            	resize = true;
            }
        }
        else
            drag = true;
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    	if(resize) {
    		Point p = new Point((int)((((double)arg0.getPoint().getX())+((double)component.getX()))/3),(int)(((double)arg0.getPoint().getY())+((double)component.getY()))/3);
    		System.out.println(p);
    		if(component.getName().startsWith(OneLineMonthAndDay.class.toString())) {
            	MonthAndDay e = (MonthAndDay)parent.elements.get(Integer.parseInt(component.getName().split("\\|\\|")[1]));
            	component.getParent().remove(component);
            	try {
            		JPanel panel = (JPanel) e.getPreview((int)arg0.getPoint().getX(),(int)arg0.getPoint().getY());
                	e.resizeToCoords((int)p.getX(), (int)p.getY());
            		panel.setName(component.getName());
					parent.addComponent(panel,component.getLocation());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
            }
    		else if(component.getName().startsWith(Steps.class.toString()) && (!component.getName().startsWith(StepsGoal.class.toString()))) {
            	Steps e = (Steps)parent.elements.get(Integer.parseInt(component.getName().split("\\|\\|")[1]));
            	component.getParent().remove(component);
            	try {
            		JPanel panel = (JPanel) e.getPreview((int)arg0.getPoint().getX(),(int)arg0.getPoint().getY());
                	e.resizeToCoords((int)p.getX(), (int)p.getY());
            		panel.setName(component.getName());
					parent.addComponent(panel,component.getLocation());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
            }
    		else if(component.getName().startsWith(StepsGoal.class.toString())) {
    			StepsGoal e = (StepsGoal)parent.elements.get(Integer.parseInt(component.getName().split("\\|\\|")[1]));
            	component.getParent().remove(component);
            	System.out.println(e.getLocation());
            	try {
            		JPanel panel = (JPanel) e.getPreview((int)arg0.getPoint().getX(),(int)arg0.getPoint().getY());
                	e.resizeToCoords((int)p.getX(), (int)p.getY());
            		panel.setName(component.getName());
					parent.addComponent(panel,component.getLocation());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
            }
    		else if(component.getName().startsWith(Calories.class.toString())) {
    			Calories e = (Calories)parent.elements.get(Integer.parseInt(component.getName().split("\\|\\|")[1]));
            	component.getParent().remove(component);
            	try {
            		JPanel panel = (JPanel) e.getPreview((int)arg0.getPoint().getX(),(int)arg0.getPoint().getY());
                	e.resizeToCoords((int)p.getX(), (int)p.getY());
            		panel.setName(component.getName());
					parent.addComponent(panel,component.getLocation());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
            }
    		else if(component.getName().startsWith(Pulse.class.toString())) {
    			Pulse e = (Pulse)parent.elements.get(Integer.parseInt(component.getName().split("\\|\\|")[1]));
            	component.getParent().remove(component);
            	try {
            		JPanel panel = (JPanel) e.getPreview((int)arg0.getPoint().getX(),(int)arg0.getPoint().getY());
                	e.resizeToCoords((int)p.getX(), (int)p.getY());
            		panel.setName(component.getName());
					parent.addComponent(panel,component.getLocation());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
            }
    		else if(component.getName().startsWith(Distance.class.toString())) {
    			Distance e = (Distance)parent.elements.get(Integer.parseInt(component.getName().split("\\|\\|")[1]));
            	component.getParent().remove(component);
            	try {
            		JPanel panel = (JPanel) e.getPreview((int)arg0.getPoint().getX(),(int)arg0.getPoint().getY());
                	e.resizeToCoords((int)p.getX(), (int)p.getY());
            		panel.setName(component.getName());
					parent.addComponent(panel,component.getLocation());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
            }
    		else if(component.getName().startsWith(BatteryText.class.toString())) {
    			BatteryText e = (BatteryText)parent.elements.get(Integer.parseInt(component.getName().split("\\|\\|")[1]));
            	component.getParent().remove(component);
            	try {
            		JPanel panel = (JPanel) e.getPreview((int)arg0.getPoint().getX(),(int)arg0.getPoint().getY());
                	e.resizeToCoords((int)p.getX(), (int)p.getY());
            		panel.setName(component.getName());
					parent.addComponent(panel,component.getLocation());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, parent.getInLang("error_io_multi")+e1.getMessage(), parent.getInLang("error_io_multi_title"), JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
            }
    	}
        drag = false;
        resize = false;
        if (component != null) {
        	
            component.setLocation((int) (component.getX() - Math.round(((double)(component.getX()%panel.getSnapDelta())) / ((double)panel.getSnapDelta()))),
            		(int) (component.getY() - Math.round(((double)(component.getY()%panel.getSnapDelta())) / ((double)panel.getSnapDelta()))));
            component = null;
        }
        parent.refresh();
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {}
    @Override
    public void mouseClicked(MouseEvent arg0) {}
    @Override
    public void mouseEntered(MouseEvent arg0) {}
    @Override
    public void mouseExited(MouseEvent arg0) {}
}
