/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terraui1;

/**
 *
 * @author Nate
 */
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class ComponentSettingDialog extends JDialog implements ActionListener, PropertyChangeListener {
    private String result1 = null;
    private String result2 = null;
    private String result3 = null;
    private String result4 = null;
    private String result5 = null;
    private String result6 = null;
    private String result7 = null;

    
    private Component comp;
    private final JTextField textField1, textField2,
            textField3, textField4, textField5, textField6;

    private final JTextArea textArea1;

    private final JButton btn1, btn2, btn3, btn4, btn5;

    private final JCheckBox cB1, cB2, cB3, cB4;

    private JOptionPane optionPane;
    private String btnText1 = null;
    private final String btnText2 = "Cancel";

    private String[] results;
    //private static final JDesktopPane desktopPane = ComponentManager.desktopPane;
    private final List<Component> selComps = ComponentManager.getAllSelectedComp();
    private final Component[] c = new Component[16];   
    private boolean multiComp = false;

    protected String[] getResults() {
        results = new String[]{result1, result2, result3, result4, result5, result6, result7};
        return results;
    }

    public ComponentSettingDialog(JFrame aFrame, Component comp) {
        super(aFrame, true);
        setResizable(false);
        UIManager.put("OptionPane.messageFont", (ComponentManager.font));
        UIManager.put("OptionPane.optionFont", (ComponentManager.font));
        
        this.comp = comp;
        
        //System.out.println(comp);
        
        textField1 = new JTextField();
        textField2 = new JTextField();
        textField3 = new JTextField();
        textField4 = new JTextField();
        textField5 = new JTextField();
        textField6 = new JTextField();
        
        textArea1 = new JTextArea();
        
        cB1 = new JCheckBox("Enable Movement"); 
        cB2 = new JCheckBox("Use New Image Size");
        cB3 = new JCheckBox("Stretch Image To Fit");
        cB4 = new JCheckBox("Use Default Image");
        
        btn1 = new JButton();
        btn2 = new JButton();
        btn3 = new JButton();
        btn4 = new JButton();
        btn5 = new JButton();
        
        c[0] = textField1; c[1] = textField2; c[2] = textField3; c[3] = textField4;
        c[4] = textField5; c[5] = textField6; c[6] = textArea1; c[7] = cB1; c[8] = cB2; 
        c[9] = cB3; c[10] = btn1; c[11] = btn2; c[12] = btn3; c[13] = btn4; c[14] = btn5;
        c[15] = cB4;

        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textField2.getText().length() >= 20) {
                    e.consume();
                }
            }
        });
        
        textArea1.setColumns(20);
        textArea1.setRows(10);
        textArea1.setLineWrap(true);
        textArea1.setWrapStyleWord(true);
        
        initComponentFont();
        
    }
    protected void initComponentFont(){
        for (Component cc: c){
            cc.setFont(ComponentManager.font);
            //cc.setVisible(false);
        }
    }
    protected void setDialogIcon(ImageIcon imgIcon){      
        imgIcon = FunctionManager.getScaledImage(imgIcon, 300, 300);
        optionPane.setIcon(imgIcon);
    }
    protected void buttonSettingDialog() {
        ImageIcon icon = null;  
        boolean isLocked = false;
        btnText1 = "Confirm";

        String labelStr1 = null;
        String labelStr2 = null;
        String labelStr3 = null;
        String labelStr4 = "Size Width:";
        String labelStr5 = "Size Height:";
        String labelStr6 = null;
        
        JScrollPane scrollPane = new JScrollPane(textArea1,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        textField1.setEditable(false);
               
        multiComp = selComps.size() > 1;
        
        textField3.setText(Integer.toString(comp.getBounds().x));
        textField4.setText(Integer.toString(comp.getBounds().y)); 
        
        if (!multiComp) {
            labelStr2 = "Position X:";
            labelStr3 = "Position Y:";

        }else{
            textField3.setText("0");
            textField4.setText("0"); 
            labelStr2 = "Shift Position X (Current X: " + Integer.toString(comp.getBounds().x) + ")";
            labelStr3 = "Shift Position Y (Current Y: " + Integer.toString(comp.getBounds().y) + ")";
        }
        

        
        try {
            Method getImageMethod = comp.getClass().getDeclaredMethod("getImageIcon");
            Method getLockedMethod = comp.getClass().getDeclaredMethod("isLocked");
            icon = (ImageIcon) getImageMethod.invoke(comp);
            isLocked = (boolean) getLockedMethod.invoke(comp);

        } catch (Exception e) {}
        
        if (!isLocked) {
            cB1.setSelected(true);
        }       
                        
        if (sameComponentType()){
    
                      
            if (comp instanceof CustomButton) {
                setTitle("Button Setting");    
                labelStr1 = "Button Text:";
                labelStr6 = "Modbus Command:";
                cB3.setVisible(false);             
            } else if (comp instanceof CustomImageBox) {
                setTitle("Image Setting");    
                scrollPane.setVisible(false);
            }
        }
        else {
            setTitle("Multi-Component Setting"); //get value of the one selected
            cB2.setVisible(false);
            btn3.setVisible(false);
            textField1.setVisible(false);
            textField2.setVisible(false);
            scrollPane.setVisible(false);
            cB3.setVisible(false);
            //System.out.println(sameComponentType());
        }
        
        if (ComponentManager.componentPara.containsKey(comp.getName())) {
            List<String> compPara = ComponentManager.componentPara.get(comp.getName());
                if (compPara.size() >= 5) {
                    textField1.setText(compPara.get(0)); //get image directory                    
                    textField5.setText(compPara.get(3)); //get width 
                    textField6.setText(compPara.get(4)); //get height 
                }
                if (compPara.size() >= 6) {
                    textField2.setText(compPara.get(5)); //get button text  
                }
                if (compPara.size() == 7) {
                    textArea1.setText(compPara.get(6)); //get Modbus command
                }
        }    

        btn1.setText("MoveToFront");
        btn2.setText("MoveToBack");
        btn3.setText("Change Image");
        btn4.setText("Copy");
        btn5.setText("Delete");



        Object[] array = {cB1, btn3,cB4, textField1,cB3,cB2, btn1, btn2, btn4, btn5, 
            labelStr1, textField2, labelStr2, textField3, labelStr3, textField4,
            labelStr4, textField5, labelStr5, textField6, labelStr6, scrollPane};

        Object[] options = {btnText1, btnText2};

        optionPane = new JOptionPane(array,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                options,
                options[0]);
        
        setDialogIcon(icon);
        setContentPane(optionPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                optionPane.setValue(JOptionPane.CLOSED_OPTION);
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent ce) {
                textField1.requestFocusInWindow();
            }
        });

        textField1.addActionListener(this);
        textField2.addActionListener(this);
        textField3.addActionListener(this);
        textField4.addActionListener(this);
        textField5.addActionListener(this);
        textField6.addActionListener(this);
        //textArea1.addActionListener(this);

        btn1.addActionListener(this);
        btn2.addActionListener(this);
        btn3.addActionListener(this);
        btn4.addActionListener(this);
        btn5.addActionListener(this);

        cB1.addActionListener(this);
        cB2.addActionListener(this);
        cB3.addActionListener(this);
        cB4.addActionListener(this);
        
        optionPane.addPropertyChangeListener(this);
        pack();
        setVisible(true);

    }
    protected boolean sameComponentType(){
        for (Component c: selComps){
            String vComp = c.getClass().getName();
            String sComp = selComps.get(0).getClass().getName();
            if (!vComp.equals(sComp)){
                return false;
            }
        }
        return true;
    }


    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();
        switch (cmd) {

            case "MoveToFront": // multi use
                selComps.forEach((aComp) -> {
                    FunctionManager.moveToFront(aComp);
                });
                break;

            case "MoveToBack": //multi use
                selComps.forEach((aComp) -> {
                    FunctionManager.moveToBack(aComp);
                });
                break;

            case "Change Image": //multi use
                final JFileChooser fc = new JFileChooser();
                FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
                ComponentManager.setFileChooserFont(fc.getComponents());
                fc.setFileFilter(imageFilter);
                int returnVal = fc.showDialog(ComponentManager.frame, "Select Image");
                if (returnVal == JFileChooser.APPROVE_OPTION){
                    File file = fc.getSelectedFile();
                    ImageIcon imgIcon = new ImageIcon (file.toString());
                    textField1.setText(file.toString());
                    setDialogIcon(imgIcon);
                }
                break;

            case "Copy": //multi use
                break;
            case "Delete": // multi use
                FunctionManager.deleteComponent(comp);
                clearAndHide();
                break;
                
            case "Stretch Image To Fit":       // only for image box 
                //CustomImageBox b = (CustomImageBox) selComp;
                if (cB3.isSelected()){
                    cB2.setSelected(false);
                    cB2.setEnabled(false);
                    textField5.setEnabled(false);
                    textField6.setEnabled(false);
                }else{
                    cB2.setEnabled(true);
                    textField5.setEnabled(true);
                    textField6.setEnabled(true);
                }
                break;
            case "Use New Image Size":      
                if (cB2.isSelected()){
                    cB3.setSelected(false);
                    cB3.setEnabled(false);
                    textField5.setEnabled(false);
                    textField6.setEnabled(false);
                }else{
                    cB3.setEnabled(true);
                    textField5.setEnabled(true);
                    textField6.setEnabled(true);
                }
                break;   
            case "Enable Movement": // multi use
                selComps.forEach((Component aComp) -> {
                    try{
                        Method setLockedMethod = aComp.getClass().getDeclaredMethod("setLock", boolean.class);
                        setLockedMethod.invoke(aComp, !cB1.isSelected());
                    }catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e){}
                });

                break;
            case "Confirm":
                optionPane.setValue(btnText1);
               
                break;
            default:
                break;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (isVisible()
                && (e.getSource() == optionPane)
                && (JOptionPane.VALUE_PROPERTY.equals(prop)
                || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {

            Object value = optionPane.getValue();
            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                return;
            }
            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

            buttonParameterCheck(value);

        }

    }

    private void buttonParameterCheck(Object value) {
        if (btnText1.equals(value)) {
            result1 = textField1.getText(); // imageIcon Directory
            result2 = textField2.getText(); // button text (not valid for imagebox)
            result3 = textField3.getText(); // x position
            result4 = textField4.getText(); // y position
            result5 = textField5.getText(); // width
            result6 = textField6.getText(); // height
            result7 = textArea1.getText(); // modbus command (not valid for imagebox)

            ImageIcon imgIcon = new ImageIcon(result1);
            //File selectedImageFile = new File(result1);
            
            selComps.forEach((Component aComp) -> {
                ImageIcon img = null;
                try{
                    Method setTextMethod = aComp.getClass().getDeclaredMethod("setText", String.class); 
                    Method setImagePathMethod = aComp.getClass().getDeclaredMethod("setImgPath", String.class);
                    
                    setImagePathMethod.invoke(aComp,result1);
                    setTextMethod.invoke(aComp, result2);
                }catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {}
                
                
                if (multiComp) {
                    int shiftX = Integer.parseInt(result3);
                    int shiftY = Integer.parseInt(result4);
                    int newBoundX = shiftX + aComp.getBounds().x;
                    int newBoundY = shiftY + aComp.getBounds().y;
                    aComp.setLocation(newBoundX, newBoundY);

                } else {
                    aComp.setLocation(Integer.parseInt(result3), Integer.parseInt(result4));
                }            
                
                //FunctionManager.changeImage(aComp, cB2.isSelected(), selectedImageFile); 
                
                if (cB2.isSelected()){
                    FunctionManager.resizeImageComp(aComp, imgIcon, imgIcon.getIconWidth(), imgIcon.getIconHeight());
                    if (aComp instanceof CustomImageBox){
                        result5 = Integer.toString(imgIcon.getIconWidth());
                        result6 = Integer.toString(imgIcon.getIconHeight());                       
                    }
                }else if (cB3.isSelected()){
                    aComp.setLocation(0, 0);
                    FunctionManager.resizeImageComp(aComp, imgIcon, ComponentManager.desktopPane.getSelectedFrame().getWidth() - 23, ComponentManager.desktopPane.getSelectedFrame().getHeight() - 37);
                    result6 = Integer.toString(ComponentManager.desktopPane.getSelectedFrame().getHeight() - 37);
                    result5 = Integer.toString(ComponentManager.desktopPane.getSelectedFrame().getWidth() - 23);               
                }else{
                    FunctionManager.resizeImageComp(aComp, imgIcon, Integer.parseInt(result5), Integer.parseInt(result6));
                }
                
 

                JLayeredPane layeredPane = ComponentManager.getLayeredPane();
                String iFrameName = ComponentManager.getInternalFrameName();
                ComponentManager.registerComponent(aComp);

                aComp.repaint();
                ComponentManager.desktopPane.getSelectedFrame().updateUI();
            });
                /*
            CustomButton btn = (CustomButton) selComps.get(0);
            btn.setText(result2); 
                 */

            clearAndHide();
        } else {
            result1 = null;
            result2 = null;
            result3 = null;
            result4 = null;
            result5 = null;
            result6 = null;
            clearAndHide();
        }
    }


    private void clearAndHide() {
        textField1.setText(null);
        textField2.setText(null);
        textField3.setText(null);
        textField4.setText(null);
        textField5.setText(null);
        setVisible(false);
    }   
}
