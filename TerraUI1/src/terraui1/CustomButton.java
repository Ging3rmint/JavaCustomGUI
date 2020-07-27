/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terraui1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javafx.geometry.Bounds;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

/**
 *
 * @author Nate
 */
public class CustomButton extends JLayeredPane{

    private Image img = null;
    private ImageIcon imgIcon = null;
    private static final Vector listeners = new Vector();
    private static final MouseManager mouseManager = new MouseManager();
    private JLabel btnText = null;
    private boolean selected = false;
    private int BorderSize = 5;
    
    private String imgPath = "src/Images/ImageButton.png";
    private String id = "";
    private String cmd = "";
    private boolean isHeld = false;
    private int posX, posY;
    
    protected static String title = null;
    protected static boolean isClicked = false;
    
    protected boolean isLocked = false;

    
    private final javax.swing.border.Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, BorderSize);
    private final javax.swing.border.Border redBorder = BorderFactory.createLineBorder(Color.RED, BorderSize);

    public CustomButton(String title, Image img) {
        setName(title);
        setLayout(new BorderLayout());
        btnText = new JLabel("", SwingConstants.CENTER);
        add(btnText, BorderLayout.CENTER);

        if (img == null) {
            imgIcon = new ImageIcon(imgPath);
        }else{
            imgIcon = new ImageIcon(img);
        }
        img = imgIcon.getImage();
        this.img = img;
        addMouseListener(mouseManager);
        addMouseMotionListener(mouseManager);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getParent().getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawRect(0, 0, getWidth() - 2, getHeight() - 2);
        g2D.drawImage(img, 0, 0, getWidth() - 1, getHeight() - 1, this);
    }


    public void setImage(ImageIcon newIcon){
        img = newIcon.getImage();
        imgIcon = newIcon;
        Graphics g = this.getGraphics();
        super.paint(g);
    }
    protected void setImgPath (String path){
        imgPath = path;
    }

    protected void setTextVisible(boolean state){
        btnText.setVisible(state);
    }
    
    protected void setText(String text) {
        btnText.setText(text);
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
    protected void setBorderSize(int size) {
        BorderSize = size;
        System.out.println("set border size " + size);
    }     
    
    protected void setLock(boolean lock){
        isLocked = lock;
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
    
    protected boolean mouseHeld(){
        return isHeld;
    }
    
    protected boolean textIsVisible(){
        return btnText.isVisible();
    }
    protected String getText() {

        return btnText.getText();
    }
    
    protected ImageIcon getImageIcon() {
        return imgIcon;
    }
    protected Font getFontSize() {
        return btnText.getFont();
    }
    
    protected int getBorderSize() {
        return BorderSize;
    }
    
    protected String getCmd(){
        return cmd;
    }

    protected void setFontSize(float size) {
        btnText.setFont(btnText.getFont().deriveFont(size));

    }
    protected void assignCmd (String modbuscmd){
        cmd = modbuscmd;
    }

    protected void addActionListener(ActionListener listener) {
        listeners.addElement(listener);
    }

    protected void removeActionListener(ActionListener listener) {
        listeners.removeElement(listener);
    }

    protected void fireEvent(ActionEvent event) {
        for (int i = 0; i < listeners.size(); i++) {
            ActionListener listener = (ActionListener) listeners.elementAt(i);
            listener.actionPerformed(event);
        }
    }
    
    protected boolean isSelected(){
        return selected;
    }
    
    protected boolean isLocked(){
        return isLocked;
    }
        
    protected int myPosX(){
        return posX;
    }
    
    protected int myPosY(){
        return posY;
    }
            
    protected void select(boolean state) {
        if (state && ComponentManager.editBar.isVisible()) {
            selected = true;
            setBorder(redBorder);
        } else if (state && !ComponentManager.editBar.isVisible()) {
            selected = true;
            setBorder(greenBorder);
        } else {
            selected = false;
            setBorder(null);
        }
    }
    

    
    public Dimension getPreferredSize() {
        return new Dimension(img.getWidth(this), img.getHeight(this));
    }


}
