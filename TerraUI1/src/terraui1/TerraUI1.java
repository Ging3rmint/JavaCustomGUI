/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terraui1;


import javafx.application.Application;
import javafx.stage.Stage;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Nate
 */
public class TerraUI1 extends Application {

    private static final JFrame frame = new JFrame();
    private static final JDesktopPane desktopPane = new JDesktopPane();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            
        }
        
        InitializeUI init = new InitializeUI();
        init.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init.setSize(800, 1000);
        init.setVisible(true);
        
        LoadSaveFileManager.loadState();
        
    }

}
