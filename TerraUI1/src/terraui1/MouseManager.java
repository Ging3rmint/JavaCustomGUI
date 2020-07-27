/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terraui1;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * @author Nate
 */
public class MouseManager implements MouseListener, MouseMotionListener {

    private final Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
    private final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);

    @Override
    public void mouseClicked(MouseEvent me) {
        Component comp = (Component) me.getSource();
        if (me.getButton() == MouseEvent.BUTTON1) {
            try {
                Method isSelectMethod = comp.getClass().getDeclaredMethod("isSelected");
                Method setSelectMethod = comp.getClass().getDeclaredMethod("select", boolean.class);
                Boolean select = (Boolean) isSelectMethod.invoke(comp);
                System.out.println(select);

                if (select) {
                    setSelectMethod.invoke(comp, false);

                } else {
                    setSelectMethod.invoke(comp, true);
                }
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                //System.out.println(e);
            }
            if (!ComponentManager.ctrlPressed && ComponentManager.editBar.isVisible()) {
                List<Component> cList = ComponentManager.componentMap.get(ComponentManager.desktopPane.getSelectedFrame());
                cList.stream().filter((item) -> (!item.equals(comp))).forEachOrdered((item) -> {
                    if (item instanceof CustomButton || item instanceof CustomImageBox) {
                        try {
                            Method setSelectMethod = item.getClass().getDeclaredMethod("select", boolean.class);
                            setSelectMethod.invoke(item, false);
                        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                            //System.out.println(e);
                        }
                    }
                });
            }

        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        Component comp = (Component) me.getSource();      
        List<Component> cList = ComponentManager.getAllSelectedComp();
        if (ComponentManager.editBar.isVisible() && me.getButton() == MouseEvent.BUTTON1) {
            cList.forEach((item) -> {
                try{
                    Method isHeldMethod = item.getClass().getDeclaredMethod("mouseHold",boolean.class);
                    Method posXMethod = item.getClass().getDeclaredMethod("setPosX",int.class);
                    Method posYMethod = item.getClass().getDeclaredMethod("setPosY",int.class);
                    
                    isHeldMethod.invoke(item, true);
                    posXMethod.invoke(item, me.getX());
                    posYMethod.invoke(item, me.getY());
                }catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e){}
            });
            
        } else if (ComponentManager.editBar.isVisible() && me.getButton() == MouseEvent.BUTTON3) {
            boolean sel = false;
            try{
                Method isSelectMethod = comp.getClass().getDeclaredMethod("isSelected");
                Boolean select = (Boolean) isSelectMethod.invoke(comp);
                if (select){
                    sel = true;
                }
            }catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e){}

            if (sel){
                ComponentSettingDialog settingDialog = new ComponentSettingDialog(ComponentManager.frame,comp);
                settingDialog.buttonSettingDialog();  
                
            }

        }

    }

    @Override
    public void mouseReleased(MouseEvent me) {
        List<Component> cList = ComponentManager.componentMap.get(ComponentManager.desktopPane.getSelectedFrame());
        cList.forEach((item) -> {
            try{
                Method isHeldMethod = item.getClass().getDeclaredMethod("mouseHold",boolean.class);

                isHeldMethod.invoke(item, false);
                ComponentManager.registerComponent(item);
            }catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e){}
        });
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        Component comp = (Component) me.getSource();
        if (comp instanceof CustomButton || comp instanceof CustomImageBox) {
            ComponentManager.desktopPane.getSelectedFrame().setCursor(handCursor);
        }
    }

    @Override
    public void mouseExited(MouseEvent me) {
        ComponentManager.desktopPane.getSelectedFrame().setCursor(defaultCursor);
    }

    @Override
    public void mouseDragged(MouseEvent me) {

        List<Component> cList = ComponentManager.componentMap.get(ComponentManager.desktopPane.getSelectedFrame());
        cList.forEach((item) -> {
            try{
                Method checkHeldMethod = item.getClass().getDeclaredMethod("mouseHeld");
                Method checkLockedMethod = item.getClass().getDeclaredMethod("isLocked");
                Method getPosXMethod = item.getClass().getDeclaredMethod("myPosX");
                Method getPosYMethod = item.getClass().getDeclaredMethod("myPosY");

                
                boolean isHeld = (boolean) checkHeldMethod.invoke(item);
                boolean isLocked = (boolean) checkLockedMethod.invoke(item);
                int posX = (int) getPosXMethod.invoke(item);
                int posY = (int) getPosYMethod.invoke(item);      
                
                if (isHeld && !isLocked) {
                    int newPointX = (me.getX() + item.getX()) - posX;
                    int newPointY = (me.getY() + item.getY()) - posY;
                    
                    if (newPointX < 0) {
                        newPointX = 0;
                    }

                    if (newPointY < 0) {
                        newPointY = 0;
                    }
                    System.out.println(newPointX + " " + newPointY);
                    item.setLocation(newPointX, newPointY);

                }
                
                
            }catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e){System.out.println(e);}                   
            
        });

        ComponentManager.desktopPane.getSelectedFrame().revalidate();
    }

    @Override
    public void mouseMoved(MouseEvent me) {
    }

}
