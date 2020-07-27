/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terraui1;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Nate
 */
public final class DynamicOptionDialog extends JDialog implements ActionListener, PropertyChangeListener {

    private String result1 = null;
    private String result2 = null;
    private String result3 = null;
    private String result4 = null;
    private String result5 = null;
    private String result6 = null;
    private String result7 = null;

    
    private String funcType = null;

    private final JTextField textField1, textField2,
            textField3, textField4, textField5, textField6;

    private final JTextArea textArea1;

    private final JButton btn1, btn2, btn3, btn4, btn5;

    private final JCheckBox cB1, cB2, cB3;

    private JOptionPane optionPane;
    private String btnText1 = null;
    private File selectedImageFile = null;
    private final String btnText2 = "Cancel";

    private String[] results;
    //private static final JDesktopPane desktopPane = ComponentManager.desktopPane;
    private final List<Component> selComps = ComponentManager.getAllSelectedComp();
    private final Component[] c = new Component[15];   
    private boolean multiComp = false;

    protected String[] getResults() {
        results = new String[]{result1, result2, result3, result4, result5, result6, result7};
        return results;
    }

    public DynamicOptionDialog(JFrame aFrame) {
        super(aFrame, true);
        setResizable(false);
        UIManager.put("OptionPane.messageFont", (ComponentManager.font));
        UIManager.put("OptionPane.optionFont", (ComponentManager.font));
        
        
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
        
        btn1 = new JButton();
        btn2 = new JButton();
        btn3 = new JButton();
        btn4 = new JButton();
        btn5 = new JButton();
        
        c[0] = textField1; c[1] = textField2; c[2] = textField3; c[3] = textField4;
        c[4] = textField5; c[5] = textField6; c[6] = textArea1; c[7] = cB1; c[8] = cB2; 
        c[9] = cB3; c[10] = btn1; c[11] = btn2; c[12] = btn3; c[13] = btn4; c[14] = btn5;
        

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
        
        initComponents();
        
    }
    protected void initComponents(){
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
        btnText1 = "Confirm";
        funcType = "button";

        String labelStr1 = null;
        String labelStr2 = "Position X:";
        String labelStr3 = "Position Y:";
        String labelStr4 = "Size Width:";
        String labelStr5 = "Size Height:";
        String labelStr6 = null;
        
        JScrollPane scrollPane = new JScrollPane(textArea1,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        textField1.setEditable(false);
        
        multiComp = selComps.size() > 1;

        if (!multiComp) {
            setTitle("Component Setting");           
            if (selComps.get(0) instanceof CustomButton) {
                labelStr1 = "Button Text:";
                labelStr6 = "Modbus Command:";
                CustomButton b = (CustomButton) selComps.get(0);
                icon = b.getImageIcon(); 
                
                textField1.setText(b.getImageIcon().toString()); 
                textField2.setText(b.getText());
                textField3.setText(Integer.toString(b.getBounds().x));
                textField4.setText(Integer.toString(b.getBounds().y));
                textField5.setText(Integer.toString(b.getWidth()));
                textField6.setText(Integer.toString(b.getHeight()));
                cB3.setVisible(false);
                
                if (!b.isLocked)
                    cB1.setSelected(true);
                               
     
                
            } else if (selComps.get(0) instanceof CustomImageBox) {
                CustomImageBox b = (CustomImageBox) selComps.get(0);
                icon = b.getImageIcon();                
                textField1.setText(b.getImageIcon().toString()); 
                textField2.setVisible(false);
                textField3.setText(Integer.toString(b.getBounds().x));
                textField4.setText(Integer.toString(b.getBounds().y));
                textField5.setText(Integer.toString(b.getWidth()));
                textField6.setText(Integer.toString(b.getHeight()));
                scrollPane.setVisible(false);
                //if (!b.isLocked) {
                    //cB1.setSelected(true);
                //}

            }
        } else {
            setTitle("Multi-Component Setting");
            // check if all button type are the same.
            
            if (selComps.get(0) instanceof CustomButton){
                CustomButton b = (CustomButton) selComps.get(0);
                textField1.setText(b.getImageIcon().toString()); 
                icon = b.getImageIcon(); 
            }else if (selComps.get(0) instanceof CustomImageBox){
                CustomImageBox b = (CustomImageBox) selComps.get(0);
                textField1.setText(b.getImageIcon().toString()); 
                icon = b.getImageIcon(); 
            }
            cB1.setVisible(false);
            textField2.setVisible(false);
            textField3.setVisible(false);
            textField4.setVisible(false);
            textField5.setVisible(false);
            textField6.setVisible(false);
            scrollPane.setVisible(false);
            cB3.setVisible(false);
            
            labelStr2 = null;
            labelStr3 = null;
            labelStr4 = null;
            labelStr5 = null;
            
            System.out.println(sameComponentType());
            
        }

        btn1.setText("MoveToFront");
        btn2.setText("MoveToBack");
        btn3.setText("Change Image");
        btn4.setText("Copy");
        btn5.setText("Delete");



        Object[] array = {cB1, btn3, textField1,cB3,cB2, btn1, btn2, btn4, btn5, 
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
    protected void connectionSettingDialog() {
        btnText1 = "Connect"; // check modbus connection 
        funcType = "connect";

        String labelStr1 = "IP Address:";
        String labelStr2 = "e.g xxx.xxx.xxx.xxx";
        String labelStr3 = "Port Number:";
        String labelStr4 = "e.g 502";

        Object[] array = {labelStr1, labelStr2, textField1, labelStr3, labelStr4, textField2};

        Object[] options = {btnText1, btnText2};

        optionPane = new JOptionPane(array,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                options,
                options[0]);

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

        optionPane.addPropertyChangeListener(this);
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();
        switch (cmd) {

            case "MoveToFront": // multi use
                selComps.forEach((aComp) -> {
                    if (aComp instanceof CustomButton) {
                        CustomButton c = (CustomButton) aComp;
                        FunctionManager.moveToFront(c);
                    } else if (aComp instanceof CustomImageBox) {
                        CustomImageBox c = (CustomImageBox) aComp;
                        FunctionManager.moveToFront(c);
                    }
                });
                break;

            case "MoveToBack": //multi use
                selComps.forEach((aComp) -> {
                    if (aComp instanceof CustomButton) {
                        CustomButton c = (CustomButton) aComp;
                        FunctionManager.moveToBack(c);
                    } else if (aComp instanceof CustomImageBox) {
                        CustomImageBox c = (CustomImageBox) aComp;
                        FunctionManager.moveToBack(c);
                    }
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
                    selectedImageFile = file;
                    ImageIcon imgIcon = new ImageIcon (file.toString());
                    textField1.setText(fc.getCurrentDirectory().toString() + file.toString());
                    setDialogIcon(imgIcon);
                }
                break;

            case "Copy": //multi use
                break;
            case "Delete": // multi use
                clearAndHide();
                break;
                
            case "Stretch Image To Fit":       // only for image box 
                //CustomImageBox b = (CustomImageBox) selComp;
                if (cB3.isSelected()){
                    cB2.setSelected(false);
                    cB2.setEnabled(false);
                }else{
                    cB2.setEnabled(true);
                }
                break;
            case "Use New Image Size":       // only for image box 
                if (cB2.isSelected()){
                    cB3.setSelected(false);
                    cB3.setEnabled(false);
                }else{
                    cB3.setEnabled(true);
                }
                break;   
            case "Enable Movement": // multi use
                selComps.forEach((Component aComp) -> {
                    if (aComp instanceof CustomButton) {
                        CustomButton b = (CustomButton) aComp;
                      //  b.isLocked = !cB1.isSelected();
                    } else if (aComp instanceof CustomImageBox) {
                        CustomImageBox b = (CustomImageBox) aComp;
                       // b.isLocked = !cB1.isSelected();
                    }
                });

                break;
            case "Confirm":
                optionPane.setValue(btnText1);
                selComps.forEach((Component aComp)-> {
                    if (selectedImageFile != null){
                       FunctionManager.changeImage(aComp, cB2.isSelected(), selectedImageFile); 
                    }
                    
                    
                });
                
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
            if (funcType.equals("connect")) {
                connectParameterCheck(value);
            } else if (funcType.equals("button")) {
                buttonParameterCheck(value);

            }
        }

    }

    private void buttonParameterCheck(Object value) {
        if (btnText1.equals(value)) {
            result1 = textField1.getText(); // imageIcon
            result2 = textField2.getText(); // button text (not valid for imagebox)
            result3 = textField3.getText(); // x position
            result4 = textField4.getText(); // y position
            result5 = textField5.getText(); // width
            result6 = textField6.getText(); // height
            result7 = textArea1.getText(); // modbus command (not valid for imagebox)
            
           
            ImageIcon imgIcon = new ImageIcon(result1);
            /*
            CustomButton btn = (CustomButton) selComps.get(0);
            btn.setText(result2); 
            */
             
            if (!multiComp && selComps.get(0) instanceof CustomButton){
                
                List<String> compPara = new ArrayList<>();
                compPara.add(result7);
               // ComponentManager.componentPara.put(selComps.get(0), compPara); 
                
                CustomButton b = (CustomButton) selComps.get(0);
                b.setText(result2);
                b.setLocation(Integer.parseInt(result3),Integer.parseInt(result4));
                FunctionManager.resizeImageComp(b, b.getImageIcon(), Integer.parseInt(result5), Integer.parseInt(result6));
                b.repaint();
            }
           

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

    private void connectParameterCheck(Object value) {
        if (btnText1.equals(value)) {
            result1 = textField1.getText();
            result2 = textField2.getText();
            if ("Connect".equals(btnText1)) {
                if (result1.isEmpty() && result2.isEmpty()) {
                    textField1.selectAll();
                    JOptionPane.showMessageDialog(this, "IP Address and Port Number must be filled.", "Try again", JOptionPane.ERROR_MESSAGE);
                    textField1.setText(null);
                    textField2.setText(null);
                    textField1.requestFocusInWindow();
                } else if (result1.isEmpty()) {
                    textField1.selectAll();
                    JOptionPane.showMessageDialog(this, "IP Address must be filled.", "Try again", JOptionPane.ERROR_MESSAGE);
                    textField1.setText(null);
                    textField1.requestFocusInWindow();
                } else if (result2.isEmpty()) {
                    textField2.selectAll();
                    JOptionPane.showMessageDialog(this, "Port Number must be filled.", "Try again", JOptionPane.ERROR_MESSAGE);
                    textField2.setText(null);
                    textField2.requestFocusInWindow();
                } else {
                    try {
                        Integer.parseInt(result2);
                        clearAndHide();
                    } catch (NumberFormatException ne) {
                        JOptionPane.showMessageDialog(this, "Port Number must be in numeric format.", "Try again", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                System.out.println("Disconnect Modbus from parent method (callModbusDisconnect).");
                clearAndHide();
            }
        } else {
            result1 = null;
            result2 = null;
            clearAndHide();
        }

    }

    private void clearAndHide() {
        textField1.setText(null);
        textField2.setText(null);
        textField3.setText(null);
        textField4.setText(null);
        textField5.setText(null);
        funcType = null;
        setVisible(false);
    }

}
