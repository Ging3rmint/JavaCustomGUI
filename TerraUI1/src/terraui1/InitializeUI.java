/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terraui1;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;


/**
 *
 * @author Nate
 */
public class InitializeUI extends JFrame {
    private static final JDesktopPane desktopPane = ComponentManager.desktopPane;
    private static final JToolBar editBar = ComponentManager.editBar;
    private static final JMenuBar menuBar = ComponentManager.menuBar;
    private static final JComboBox panelChooser = ComponentManager.panelChooser;
    private static final ActionManager actionManager = new ActionManager();
    private static final KeyManager keyManager = new KeyManager();
    
    private final String[] menus = {"File", "Setting", "User", "System"}; 
    private final String[] fileMenu = {"Save", "Import Save File", "Export Save File"};
    private final String[] settingMenu = {"Connection", "Option", "Edit/User UI"};
    private final String[] userMenu = {"Login", "Account Management"};
    private final String[] systemMenu = {"UI Log", "Console/Machine Log"};   
    private final String[] panelTools = {" --Edit Panel-- ", "Add Panel", "Delete Panel", "Rename Panel", "Move Panel"};

    public InitializeUI(){

        super("TerraUI v0.02a");
        setLayout(new BorderLayout());
      
        initializeMenuBar1(menuBar);
        initializeEditBar1(editBar);
        
        setJMenuBar(menuBar);      
        
        editBar.setVisible(false);
        add(editBar, BorderLayout.PAGE_START);
        
        addKeyListener(keyManager);
        desktopPane.setDesktopManager(new BoundedDesktopManager());
        add(desktopPane, BorderLayout.CENTER);       
        ComponentManager.myTitle = this.getTitle();
        ComponentManager.frame = this;

    }

    private void initializeEditBar1(JToolBar editBar1){
        JButton btn = null;

        JComboBox comboBox = null;
        
        panelChooser.setName("PanelChooser");
        panelChooser.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        panelChooser.addActionListener(actionManager);
        panelChooser.setFont(ComponentManager.font);
        
        addToToolBar(panelChooser, 100, 200, editBar1);
        editBar1.addSeparator();
        
        comboBox = new JComboBox();
        comboBox.setName("PanelTools");
        comboBox.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxx");
        comboBox.setModel(new DefaultComboBoxModel(panelTools));
        comboBox.addActionListener(actionManager);
        comboBox.setFont(ComponentManager.font);
        
        addToToolBar(comboBox, 100, 200, editBar1);
        editBar1.addSeparator();
        
        btn = new JButton("Add Button");
        btn.addActionListener(actionManager);
        btn.setFont(ComponentManager.font);
        
        addToToolBar(btn, 100, 200, editBar1);
        editBar1.addSeparator();
        
        btn = new JButton("Add Image");
        btn.addActionListener(actionManager);
        btn.setFont(ComponentManager.font);
        addToToolBar(btn, 100, 200, editBar1);
        editBar1.addSeparator();
        
        btn = new JButton("Add Message List");
        btn.addActionListener(actionManager);
        btn.setFont(ComponentManager.font);
        addToToolBar(btn, 100, 200, editBar1);
        editBar1.addSeparator();


        
    }
    private void addToToolBar (Component component, int row, int column, JToolBar toolBar1){
        Dimension d = component.getPreferredSize();
        component.setMaximumSize(d);
        component.setMinimumSize(d);
        component.setPreferredSize(d);
        toolBar1.add( component );
    }
    private void initializeMenuBar1(JMenuBar menuBar1){
        JMenu menu = null;
        
        for (String item: menus){
            menu = new JMenu(item);
            menu.setFont(ComponentManager.font);
            initializeMenu(menu);
            menuBar1.add(menu);
        }
        
    }
    private void initializeMenu(JMenu menu){
        String menuName = menu.getText();
        JMenuItem menuItem = null;
        String[] itemArray = null;
        
        switch (menuName){
            case "File":
                itemArray = fileMenu;
                break;
            
            case "Setting":
                itemArray = settingMenu;
                break;
             
            case "User":
                itemArray = userMenu;
                break;
                
            case "System":
                itemArray = systemMenu;
                break;    
                
            default:
                break;
        }
        
        for (String item:itemArray){
            menuItem = new JMenuItem(item);
            menuItem.setFont(ComponentManager.font);
            menuItem.addActionListener(actionManager);
            menu.add(menuItem);
        }

    }
}
