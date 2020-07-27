/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terraui1;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;

/**
 *
 * @author Nate
 */
public class ActionManager implements ActionListener {
    //private static JDesktopPane desktopPane = ComponentManager.desktopPane;
    @Override
    public void actionPerformed(ActionEvent a) {
        Component comp = (Component) a.getSource();
        if (comp instanceof JMenuItem) {
            String item = a.getActionCommand();
            switch (item) {
                case ("Save"):
                    LoadSaveFileManager.saveState();
                    
                    break;
                case ("Import Save File"):
                    break;
                case ("Export Save File"):
                    break;
                case ("Connection"):
                    break;
                case ("Option"):
                    break;
                case ("Edit/User UI"):
                    FunctionManager.editMode();
                    break;
                case "Login":
                    break;
                case "Account Management":
                    break;
                case "UI Log":
                    break;
                case "Console/Machine Log":
                    break;
                default:
                    break;
            }
        } else if (comp instanceof JComboBox) {
            JComboBox comboBox = (JComboBox) comp;
            String item = comboBox.getSelectedItem().toString();
            if (comboBox.getName().equals("PanelTools")) {
                switch (item) {
                    case ("Add Panel"):
                        FunctionManager.addPanel("");
                        break;
                    case ("Delete Panel"):
                        break;
                    case ("Rename Panel"):
                        break;
                    case ("Move Panel"):
                        break;
                    default:
                        break;
                }
                comboBox.setSelectedIndex(0);
            } else if (comboBox.getName().equals("PanelChooser")){
                if (!item.isEmpty()){
                    System.out.println(item);
                    FunctionManager.selectPanel(ComponentManager.getJInternalFrame(item));
                }
            }

        } else if (comp instanceof JButton) {
            String item = a.getActionCommand();
            switch (item){
                case ("Add Button"):
                    FunctionManager.addButton();
                    break;
                case ("Add Image"):
                    FunctionManager.addImage();
                    break;
                case ("Add Message List"):
                    break;
                case ("Add Multicast player"):
                    break;
                default:
                    break;
            }
        }
    }
}
