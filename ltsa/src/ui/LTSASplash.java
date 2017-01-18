package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

public class LTSASplash extends Window {
  final Window thisWindow;

  // SplashScreen's constructor
  public LTSASplash(Window owner){
  	  super(owner);
    thisWindow = this;
    ImageIcon splashPicture = new ImageIcon(this.getClass().getResource("icon/splash.gif"));
    // Create a JPanel so we can use a BevelBorder
    JPanel PanelForBorder=new JPanel(new BorderLayout());
    PanelForBorder.setLayout(new BorderLayout());
    PanelForBorder.add(new JLabel(splashPicture),BorderLayout.CENTER);
    PanelForBorder.setBorder(new BevelBorder(BevelBorder.RAISED));
    add(PanelForBorder);    
    pack();
    // Plonk it on center of screen
    Dimension WindowSize=getSize(),ScreenSize=Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((ScreenSize.width-WindowSize.width)/2,(ScreenSize.height-WindowSize.height)/2,WindowSize.width,WindowSize.height);
    this.addMouseListener(new Mouse());
    setVisible(true);  
  }
  
  class Mouse extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      thisWindow.setVisible(false);
      thisWindow.dispose();
    }
  }
      
}