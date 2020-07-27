/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terraui1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Nate
 */
public class KeyManager implements KeyListener{

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        //System.out.println(ke.getKeyCode());
        if (ke.getKeyCode() == 17){ //17 == ctrl button
            ComponentManager.ctrlPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        if (ComponentManager.ctrlPressed){
            ComponentManager.ctrlPressed = false;
        }
    }
    
}
