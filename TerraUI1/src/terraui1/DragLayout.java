/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terraui1;

import java.awt.*;

/**
 *
 * @author Nate
 */
public final class DragLayout implements LayoutManager, java.io.Serializable{
    private boolean usePreferredSize;
    
    public DragLayout(){
        this(true);
    }
    
    public DragLayout(boolean usePreferredSize){
        setUsePreferredSize (usePreferredSize);
    }
    
    public void setUsePreferredSize(boolean usePreferredSize){
        this.usePreferredSize = usePreferredSize;
    }
    
    public boolean isUsePreferredSize(){
        return usePreferredSize;
    }
    
    @Override
    public void addLayoutComponent(String name, Component comp){}
    
    @Override
    public void removeLayoutComponent(Component component){}
    
    @Override
    public Dimension minimumLayoutSize(Container parent){
        synchronized (parent.getTreeLock()){
            return preferredLayoutSize(parent);
        }
    }
    
    @Override
    public Dimension preferredLayoutSize(Container parent){
        synchronized (parent.getTreeLock()){
            return getLayoutSize(parent);
        }
    }
    
    private Dimension getLayoutSize(Container parent){
        Insets parentInsets = parent.getInsets();
        int x = parentInsets.left;
        int y = parentInsets.top;
        int width = 0;
        int height = 0;
        
        for (Component component: parent.getComponents()){
            if (component.isVisible()){
                Point p = component.getLocation();
                Dimension d = getActualSize(component);
                x = Math.min(x, p.x);
                y = Math.min(y, p.y);
                width = Math.max(width, p.x + d.width);
                height = Math.max(height, p.y + d.height);
                
            }
            
        }
        if (x < parentInsets.left)
            width += parentInsets.left - x;
        
        if (y < parentInsets.top)
            height += parentInsets.top - y;
        
        width += parentInsets.right;
        height += parentInsets.bottom;
        Dimension d = new Dimension(width, height);
        
        return d;
    }
    
    private Dimension getActualSize(Component component){
        if (usePreferredSize)
            return component.getPreferredSize();
        
        Dimension d = component.getSize();
        
        if (d.width == 0 || d.height == 0)
            return component.getPreferredSize();
        else
            return d;
    }
    
    @Override
    public void layoutContainer(Container parent){
        synchronized(parent.getTreeLock()){
            Insets parentInsets = parent.getInsets();
            int x = parentInsets.left;
            int y = parentInsets.top;
            
            for (Component component: parent.getComponents()){
                if (component.isVisible()){
                    Point location = component.getLocation();
                    x = Math.min(x, location.x);
                    y = Math.min(y, location.y);
                }
            }
            
            x = (x < parentInsets.left) ? parentInsets.left - x: 0;
            y = (y < parentInsets.top) ? parentInsets.top - y: 0;
            
            for (Component component: parent.getComponents()){
                if (component.isVisible()){
                    Point p = component.getLocation();
                    Dimension d = getActualSize(component);
                    
                    component.setBounds(p.x + x, p.y + y, d.width, d.height);
                            
                }
            }
        }
    }
    @Override
    public String toString(){
        return "[" + getClass().getName() + "]";
    }
            
}
