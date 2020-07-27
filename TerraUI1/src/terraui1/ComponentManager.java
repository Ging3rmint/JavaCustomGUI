/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terraui1;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

/**
 *
 * @author Nate
 */
public class ComponentManager {
    protected static JFrame frame;
    protected static String myTitle;
    protected static final Map <JInternalFrame, List<Component>> componentMap = new HashMap <>(); 
    protected static final HashMap <String, String> internalFramePara = new HashMap <> ();
    protected static final HashMap <String, List<String>> componentPara = new HashMap <>(); 
    protected static final JMenuBar menuBar = new JMenuBar();
    protected static JMenu menu = new JMenu();
    protected static final JToolBar editBar = new JToolBar();
    protected static final JComboBox panelChooser = new JComboBox();
    protected static final JDesktopPane desktopPane = new JDesktopPane();
    protected static boolean ctrlPressed = false;
    protected static Font font = new Font("Arial", Font.PLAIN, 16);
    
    
    protected static void setFileChooserFont(Component[] comp){
        for (Component comp1 : comp) {
            if (comp1 instanceof Container) {
                setFileChooserFont(((Container) comp1).getComponents());
            }
            try {
                comp1.setFont(font);          
            }catch(Exception e){}//do nothing  
        }
    }
    protected static JInternalFrame getFrameFromMap (Component cc) {
        JInternalFrame iFrame = null;
        for (Map.Entry<JInternalFrame, List<Component>> m : componentMap.entrySet()) {
             List<Component> comps =  (List<Component>) m.getValue();
             for (Component c:comps){
                 if (c == cc){
                     iFrame = m.getKey();
                     break;
                 }
             }
        }
        return iFrame;    
    }
    
    protected static JInternalFrame getJInternalFrame(String frameName){
        JInternalFrame thisFrame = null;
        for (JInternalFrame iFrame:desktopPane.getAllFrames()){
            if (iFrame.getName().equals(frameName)){
                thisFrame = iFrame;
                break;
            }
        }
        return thisFrame;
    }
    protected static String getInternalFrameName (){
        JInternalFrame iFrame = desktopPane.getSelectedFrame();
        String iFrameName = iFrame.getName();
        return iFrameName;
    }
    protected static JLayeredPane getLayeredPaneFromFrame (JInternalFrame frame){
        List<Component> cList = componentMap.get(frame);
        JLayeredPane panel = (JLayeredPane) cList.get(0);
        
        return panel;
    }
    protected static JLayeredPane getLayeredPane (){
        JInternalFrame iFrame = desktopPane.getSelectedFrame();
        List<Component> cList = componentMap.get(iFrame);
        JLayeredPane panel = (JLayeredPane) cList.get(0);
        
        return panel;
    }
    protected static List<Component> getAllSelectedComp (){
        List<Component> sCList = new ArrayList <>();
        JInternalFrame iFrame = desktopPane.getSelectedFrame();
        List<Component> cList = componentMap.get(iFrame);
        cList.forEach((Component comp) -> {
            try {
                Method isSelectMethod = comp.getClass().getDeclaredMethod("isSelected");
                boolean select = (boolean) isSelectMethod.invoke(comp);
                if (select) {
                    sCList.add(comp);
                }
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException toBeHandled) {
            }
        });        
        return sCList;
    }
    protected static int getTotalComponent(){
        int i = 0;
        for (Map.Entry<JInternalFrame, List<Component>> m : componentMap.entrySet()) {
            List<Component> cList = componentMap.get(m.getKey());
            for (Component c: cList){
                i ++;
            }
        }
        return i;
    }
    protected static int getTotalImageBoxFromFrame () {
        int i = 1;
        JInternalFrame iFrame = desktopPane.getSelectedFrame();

        List<Component> cList = componentMap.get(iFrame);
        
        i = cList.stream().filter((comp) -> (comp instanceof CustomImageBox)).map((_item) -> 1).reduce(i, Integer::sum);
        return i;
    }
    protected static int getTotalCustomButtonFromFrame () {
        int i = 1;
        JInternalFrame iFrame = desktopPane.getSelectedFrame();

        List<Component> cList = componentMap.get(iFrame);
        
        i = cList.stream().filter((comp) -> (comp instanceof CustomButton)).map((_item) -> 1).reduce(i, Integer::sum);
        return i;
    }
    protected static void registerJInternalFrame (String frameName, String framePos){
        internalFramePara.put(frameName, framePos);
    }
    protected static void registerComponent (Component comp) {
        
        List<String> compPara = new ArrayList<>();
        JInternalFrame iFrame = getFrameFromMap(comp);
        JLayeredPane lPane = getLayeredPaneFromFrame(iFrame);
        String cLayer = Integer.toString(lPane.getPosition(comp));
        System.out.println(comp.getName() + " " + cLayer);
        String imgPath = null;
        String text = null;
        String cmd = null;
        String id = null;
        Dimension cSize = null;
        
        try{
            Method getPreferredSizeMethod = comp.getClass().getDeclaredMethod("getPreferredSize");
            Method getIDMethod = comp.getClass().getDeclaredMethod("getID");
            Method getImagePathMethod = comp.getClass().getDeclaredMethod("getImgPath");
            
            id = (String) getIDMethod.invoke(comp);
            imgPath = (String) getImagePathMethod.invoke(comp);
            cSize = (Dimension) getPreferredSizeMethod.invoke(comp);
        }catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e){}
        try{
            Method getTextMethod = comp.getClass().getDeclaredMethod("getText");
            Method getCmdMethod = comp.getClass().getDeclaredMethod("getCmd");
            cmd = (String) getCmdMethod.invoke(comp);
            text = (String) getTextMethod.invoke(comp);
        }catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e){}
        
        int xPos = comp.getX();
        int yPos = comp.getY();
        int cWidth = cSize.width;
        int cHeight = cSize.height;

        String cls = comp.getClass().toString();
        
        
        compPara.add(imgPath); //0
        compPara.add(Integer.toString(xPos)); //1
        compPara.add(Integer.toString(yPos)); //2
        compPara.add(Integer.toString(cWidth)); //3
        compPara.add(Integer.toString(cHeight)); //4
        compPara.add(text); //5
        compPara.add(cmd); //6
        compPara.add(cLayer); //7
        compPara.add(iFrame.getName()); //8
        compPara.add(cls); //9
        compPara.add(id); //10
        
        componentPara.put(id, compPara);
        
    }
    
}
