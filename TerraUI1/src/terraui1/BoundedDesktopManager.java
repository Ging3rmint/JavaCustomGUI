/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terraui1;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;

/**
 *
 * @author Nate
 */
public class BoundedDesktopManager extends DefaultDesktopManager {

  @Override
  public void beginDraggingFrame(JComponent f) {
    // Don't do anything. Needed to prevent the DefaultDesktopManager setting the dragMode
  }

  @Override
  public void beginResizingFrame(JComponent f, int direction) {
    // Don't do anything. Needed to prevent the DefaultDesktopManager setting the dragMode
  }

  @Override
  public void setBoundsForFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {
    boolean didResize = (f.getWidth() != newWidth || f.getHeight() != newHeight);
    if (!inBounds((JInternalFrame) f, newX, newY, newWidth, newHeight)) {
      Container parent = f.getParent();
      Dimension parentSize = parent.getSize();
      int boundedX = (int) Math.min(Math.max(0, newX), parentSize.getWidth() - newWidth);
      int boundedY = (int) Math.min(Math.max(0, newY), parentSize.getHeight() - newHeight);
      f.setBounds(boundedX, boundedY, newWidth, newHeight);
    } else {
      f.setBounds(newX, newY, newWidth, newHeight);
    }
    if(didResize) {
      f.validate();
    }
  }

  protected boolean inBounds(JInternalFrame f, int newX, int newY, int newWidth, int newHeight) {
    if (newX < 0 || newY < 0) return false;
    if (newX + newWidth > f.getDesktopPane().getWidth()) return false;
    return newY + newHeight <= f.getDesktopPane().getHeight();
  }
}
