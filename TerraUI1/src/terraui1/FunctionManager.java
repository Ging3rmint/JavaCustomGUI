/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terraui1;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import static java.lang.System.err;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;


/**
 *
 * @author Nate
 */
public class FunctionManager {

    private static final JToolBar editBar = ComponentManager.editBar;
    private static final JFrame frame = ComponentManager.frame;
    private static final JDesktopPane desktopPane = ComponentManager.desktopPane;
    private static final JComboBox panelChooser = ComponentManager.panelChooser;
    private static final Map<JInternalFrame, List<Component>> componentMap = ComponentManager.componentMap;
    private static final ActionManager actionManager = new ActionManager();
    private static final MouseManager mouseManager = new MouseManager();
    private static final KeyManager keyManager = new KeyManager();
    
    

    protected static ImageIcon getScaledImage(ImageIcon icon, int w, int h){
        if (icon == null){
            return null;
        }
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if(icon.getIconWidth() > w)
        {
          nw = w;
          nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if(nh > h)
        {
          nh = h;
          nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }
    protected static void changeImage(Component comp, boolean trueSize, File file) {
        ImageIcon newIcon = new ImageIcon(file.toString());

        try {
            if (trueSize) {
                Method setImageMethod = comp.getClass().getDeclaredMethod("setImage", ImageIcon.class);
                setImageMethod.invoke(comp, newIcon);
                System.out.println("Set " + comp.getName() + " (Default Size) Image to " + file);
            } else {
                Method getDimensionMethod = comp.getClass().getDeclaredMethod("getPreferredSize");
                Dimension d = (Dimension) getDimensionMethod.invoke(comp);
                int prefHeight = d.height;
                int prefWidth = d.width;
                resizeImageComp(comp, newIcon, prefWidth, prefHeight);
            }

        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
        }

    }

    protected static void resizeImageComp(Component comp, ImageIcon icon, int width, int height) {

        Image img = icon.getImage();
        Image resdImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        ImageIcon resdImgIcon = new ImageIcon(resdImg);
        
        try{
            Method setImageMethod = comp.getClass().getDeclaredMethod("setImage", ImageIcon.class);
            setImageMethod.invoke(comp, resdImgIcon);
        }catch (Exception e){}

    }

    protected static void moveToBack(Component comp) {
        JLayeredPane layerPane = ComponentManager.getLayeredPane();
        layerPane.moveToBack(comp);
        System.out.println(layerPane.getPosition(comp));
    }

    protected static void moveToFront(Component comp) {
        JLayeredPane layerPane = ComponentManager.getLayeredPane();
        layerPane.moveToFront(comp);
        System.out.println(layerPane.getPosition(comp));
    }
    protected static void deleteComponent(Component comp){
        JInternalFrame iFrame = desktopPane.getSelectedFrame();
        if (iFrame != null){
            JLayeredPane panel = ComponentManager.getLayeredPane();
            panel.remove(comp);
            iFrame.updateUI();
            ComponentManager.componentPara.remove(comp.getName());
        }
    }
    protected static void addImage() {
        setComponentLayerPosition();
        JInternalFrame iFrame = desktopPane.getSelectedFrame();
        if (iFrame != null) {

            int totalC = ComponentManager.getTotalComponent();
            CustomImageBox imgBox = new CustomImageBox("ImagePanel "+ totalC ,null);
            JLayeredPane panel = ComponentManager.getLayeredPane();
            componentMap.get(iFrame).add(imgBox);
            panel.add(imgBox);
            panel.moveToFront(imgBox);
            iFrame.updateUI();

            

            ComponentManager.registerComponent(imgBox);
        }

    }
    protected static void loadUI_ImageBox(String imgDir, String xPos, String yPos, String cWidth, String cHeight, String cLayer, String cFrame, String id){
        if (!"".equals(cFrame)){
            ImageIcon imgIcon = null;
            JInternalFrame iFrame = ComponentManager.getJInternalFrame(cFrame);           
            JLayeredPane layerPane = ComponentManager.getLayeredPaneFromFrame (iFrame);   
            if (!"".equals(imgDir)){
                imgIcon = new ImageIcon(imgDir);  
            }else{
                err.println("Load Missing imgDir Data");
                return;
            }
            if ("".equals(id)){
                err.println("Load Missing ID Data");
                return;       
            }
            CustomImageBox imgBox = new CustomImageBox(id, imgIcon);
            if (!"".equals(xPos) || !"".equals(yPos)){
                imgBox.setLocation(Integer.parseInt(xPos),Integer.parseInt(yPos));
            }else{
                err.println("Load Missing Position XY Data");
                return;
            }
            layerPane.add(imgBox);
             if (!"".equals(cLayer)){
                layerPane.setLayer(imgBox, 0,Integer.parseInt(cLayer));
            }else{
                layerPane.moveToFront(imgBox);
                err.println("Load Missing Layer Position Data");
            }  
            //iFrame.updateUI();            
            componentMap.get(iFrame).add(imgBox);
            ComponentManager.registerComponent(imgBox);
        }else{
            err.println("Load Missing Frame Data");
        }
        
    }
    protected static void loadUI_CustomButton(String imgDir, String xPos, String yPos, String cWidth, String cHeight, String cText, String cCmd, String cLayer, String cFrame, String id){
        
        if (!"".equals(cFrame)){
            Image img = null;
            JInternalFrame iFrame = ComponentManager.getJInternalFrame(cFrame);           
            JLayeredPane layerPane = ComponentManager.getLayeredPaneFromFrame (iFrame);
            if (!"".equals(imgDir)){
                ImageIcon imgIcon = new ImageIcon(imgDir);  
                img = imgIcon.getImage();
            }else{
                err.println("Load Missing imgDir Data");
                return;
            }
            if ("".equals(id)){
                err.println("Load Missing ID Data");
                return;       
            }
            CustomButton customBtn = new CustomButton(id, img);
            if (!"".equals(xPos) || !"".equals(yPos)){
                customBtn.setLocation(Integer.parseInt(xPos),Integer.parseInt(yPos));
            }else{
                err.println("Load Missing Position XY Data");
                return;
            }
            if (!"".equals(cText)){
                customBtn.setText(cText);
            }else{
                err.println("Load Missing Text Data");
                return;  
            }

            customBtn.setFontSize(30); //to add parameter to change font size
            customBtn.addActionListener(actionManager);
            layerPane.add(customBtn);
            
            if (!"".equals(cLayer)){
                layerPane.setLayer(customBtn, 0,Integer.parseInt(cLayer));
            }else{
                layerPane.moveToFront(customBtn);
                err.println("Load Missing Layer Position Data");
            }             
            
            componentMap.get(iFrame).add(customBtn);
            ComponentManager.registerComponent(customBtn);
        }else{
            err.println("Load Missing Frame Data");
        }
    }
    protected static void setComponentLayerPosition(){
        JInternalFrame iFrame = desktopPane.getSelectedFrame();
        List <Component> cList = componentMap.get(iFrame);
        JLayeredPane layerPane = ComponentManager.getLayeredPaneFromFrame (iFrame);
        for (Component c : cList){
            int layer = layerPane.getPosition(c);
            int newLayer = layer - 1;
            layerPane.setLayer(c, 0, newLayer);
            
        }
    }
    protected static void addButton() {
        setComponentLayerPosition();
        JInternalFrame iFrame = desktopPane.getSelectedFrame();
        
        if (iFrame != null) {
            int totalC = ComponentManager.getTotalComponent();
            int frameTotalC = ComponentManager.getTotalCustomButtonFromFrame();
            CustomButton customBtn = new CustomButton("CustomButton " + totalC, null);
            JLayeredPane layerPane = ComponentManager.getLayeredPaneFromFrame (iFrame);
            customBtn.setLocation(0, 0);
            customBtn.setText("CustomButton " + frameTotalC);
            customBtn.setFontSize(30);
            customBtn.addActionListener(actionManager);
            layerPane.add(customBtn);
            layerPane.moveToFront(customBtn);
 
            
            componentMap.get(iFrame).add(customBtn);
            iFrame.updateUI();

            
            
            ComponentManager.registerComponent(customBtn);
            
        }

    }

    protected static void editMode() {
        if (editBar.isVisible()) {
            editBar.setVisible(false);
            frame.setTitle(ComponentManager.myTitle);
            for (JInternalFrame iFrame : desktopPane.getAllFrames()) {
                desktopPane.getDesktopManager().deiconifyFrame(iFrame);
                iFrame.setResizable(false);
                iFrame.setMaximizable(false);
                iFrame.setIconifiable(false);
                List<Component> cList = ComponentManager.componentMap.get(iFrame);
                cList.forEach((comp) -> {
                    try{
                        Method setSelectMethod = comp.getClass().getDeclaredMethod("select", boolean.class);
                        setSelectMethod.invoke(comp, false);
                    }catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e){}
                   
                });
            }

        } else {
            editBar.setVisible(true);
            frame.setTitle(ComponentManager.myTitle + " (Edit Mode)");
            for (JInternalFrame iFrame : desktopPane.getAllFrames()) {
                iFrame.setResizable(true);
                iFrame.setMaximizable(true);
                iFrame.setIconifiable(true);
                List<Component> cList = ComponentManager.componentMap.get(iFrame);
                cList.forEach((comp) -> {
                    try{
                        Method setSelectMethod = comp.getClass().getDeclaredMethod("select", boolean.class);
                        setSelectMethod.invoke(comp, false);
                    }catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e){}
                });
            }

        }
    }

    protected static void selectPanel(JInternalFrame iFrame) {
        desktopPane.getDesktopManager().deiconifyFrame(iFrame);
        desktopPane.moveToFront(iFrame);

        try {
            iFrame.setSelected(true);
        } catch (PropertyVetoException e) {

        }
        desktopPane.updateUI();
    }

    protected static void addPanel(String pName) {
        int totalItem = panelChooser.getItemCount() + 1;
        String panelName = "";
        boolean create = true;
        if ("".equals(pName)){
            UIManager.put("OptionPane.messageFont", (ComponentManager.font));
            panelName = JOptionPane.showInputDialog(frame, "Input panel name.", "Panel " + totalItem);
            System.out.println(panelName);
            if (panelName == null || panelName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Name cannot be empty.", "ERROR", JOptionPane.ERROR_MESSAGE);
                create = false;
            }
        }else{
            panelName = pName;
        }

        if (create){
            if (((DefaultComboBoxModel) panelChooser.getModel()).getIndexOf(panelName) == -1) {
                JInternalFrame iFrame = new JInternalFrame(panelName, true, false, true, true);
                JLayeredPane panel = new JLayeredPane();
                panel.setLayout((LayoutManager) new DragLayout());
                panel.addMouseListener(mouseManager);
                panel.setName(panelName);

                iFrame.setResizable(true);
                iFrame.setName(panelName);
                iFrame.add(new JScrollPane(panel));
                iFrame.getContentPane().addKeyListener(keyManager);
                iFrame.addMouseListener(mouseManager);
                desktopPane.add(iFrame).setVisible(true);
                desktopPane.getDesktopManager().maximizeFrame(iFrame);
                panelChooser.addItem(panelName);
                panelChooser.setSelectedItem(panelName);
                List<Component> list = new ArrayList<>();
                list.add(panel);
                componentMap.put(iFrame, list);
                String position = Integer.toString(((DefaultComboBoxModel) panelChooser.getModel()).getIndexOf(panelName));
                ComponentManager.internalFramePara.put(position, panelName);
                System.out.println(ComponentManager.internalFramePara.get(panelName));
                //panel.addMouseListener(eventManager);
                //newInternalFrame.addMouseListener(eventManager);       
            } else {
                JOptionPane.showMessageDialog(null, "Name exist! Please enter panel name and try again.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
