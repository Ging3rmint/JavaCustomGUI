/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terraui1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;


/**
 *
 * @author Nate
 */
public class CustomImageBox extends JLayeredPane{

    private Image img = null;
    private ImageIcon imgIcon = null;
    private final javax.swing.border.Border redBorder = BorderFactory.createLineBorder(Color.RED, 5);
    private static final MouseManager mouseManager = new MouseManager();
    private boolean selected = false;
    
    private String imgPath = "src/Images/ATEIS LOGO.jpg";
    private String id = "";
    private int posX, posY;
    private boolean isHeld = false;
    private boolean isLocked = false;
    
    public CustomImageBox(String title, ImageIcon icon) {       
        setName(title);        
        if (icon == null) {
            icon = new ImageIcon(imgPath);
        }
        this.imgIcon = icon;
        img = imgIcon.getImage();
        addMouseListener(mouseManager);
        addMouseMotionListener(mouseManager);
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        //setBackground(new Color (0,0,0,0));
        g.setColor(new Color (0,0,0,0));
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawRect(0, 0, getWidth() - 2, getHeight() - 2);
        g2D.drawImage(img, 0, 0, getWidth() - 1, getHeight() - 1, this);
    }
    
    protected void setImgPath(String path){
        imgPath = path;                
    }
    
    protected void setPosX(int x){
        posX = x;
    }

    protected void setPosY(int y){
        posY = y;
    }
    
    protected void mouseHold(boolean h){
        isHeld = h;
    }  
    
    protected void setLock(boolean lock) {
        isLocked = lock;
    }  
    protected void select(boolean state) {
        if (state && ComponentManager.editBar.isVisible()) {
            selected = true;
            setBorder(redBorder);
        } else {
            selected = false;
            setBorder(null);
        }
    }
    public void setImage(ImageIcon newIcon){
        img = newIcon.getImage();
        imgIcon = newIcon;
        Graphics g = this.getGraphics();
        super.paint(g);
        
    }
    
    protected String getImgPath(){
        return imgPath;
    }
    protected int getPosX(){
        return posX;
    }
    
    protected int getPosY(){
        return posY;
    }
    
    protected String getID(){
        return getName();
    }
        
    protected int myPosX(){
        return posX;
    }
    
    protected int myPosY(){
        return posY;
    }
        
    protected boolean isSelected(){
        return selected;
    }
    
    protected boolean isLocked(){
        return isLocked;
    }   
    
    protected ImageIcon getImageIcon(){
        return imgIcon;
    }
    
    
    protected boolean mouseHeld(){
        return isHeld;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(img.getWidth(this), img.getHeight(this));
    }
}
