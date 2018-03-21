import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;

public class MyMouseAdapter extends MouseAdapter {
	private Random generator = new Random();
	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
			case 1:		//Left mouse button
				Component c = e.getComponent();
				while (!(c instanceof JFrame)) {
					c = c.getParent();
					if (c == null) {
						return;
					}
				}
				JFrame myFrame = (JFrame) c;
				MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
				Insets myInsets = myFrame.getInsets();
				int x1 = myInsets.left;
				int y1 = myInsets.top;
				e.translatePoint(-x1, -y1);
				int x = e.getX();
				int y = e.getY();
				myPanel.x = x;
				myPanel.y = y;
				myPanel.mouseDownGridX = myPanel.getGridX(x, y);
				myPanel.mouseDownGridY = myPanel.getGridY(x, y);
				myPanel.repaint();
				break;
			case 3:		//Right mouse button
				Component c2 = e.getComponent();
				while (!(c2 instanceof JFrame)) {
					c2 = c2.getParent();
					if (c2 == null) {
						return;
					}
				}
				JFrame myFrame1 = (JFrame) c2;
				MyPanel myPanel1 = (MyPanel) myFrame1.getContentPane().getComponent(0);
				Insets myInsets1 = myFrame1.getInsets();
				int x11 = myInsets1.left;
				int y11 = myInsets1.top;
				e.translatePoint(-x11, -y11);
				int x111 = e.getX();
				int y111 = e.getY();
				myPanel1.x = x111;
				myPanel1.y = y111;
				myPanel1.mouseDownGridX = myPanel1.getGridX(x111, y111);
				myPanel1.mouseDownGridY = myPanel1.getGridY(x111, y111);
				myPanel1.repaint();
				break;
		default:    //Some other button (2 = Middle mouse button, etc.)
				//Do nothing
				break;
		}
	}
	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
			case 1:		//Left mouse button
				Component c = e.getComponent();
				while (!(c instanceof JFrame)) {
					c = c.getParent();
					if (c == null) {
						return;
					}
				}
				JFrame myFrame = (JFrame)c;
				MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
				Insets myInsets = myFrame.getInsets();
				int x1 = myInsets.left;
				int y1 = myInsets.top;
				e.translatePoint(-x1, -y1);
				int x = e.getX();
				int y = e.getY();
				myPanel.x = x;
				myPanel.y = y;
				int gridX = myPanel.getGridX(x, y);
				int gridY = myPanel.getGridY(x, y);
				if (!(myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) //Released the mouse button on the same cell where it was pressed
					{
					
					if ((gridX == 0) || (gridY == 0)) {
						Color newColor = null;
						switch (1) {
							
							case 1:
								newColor = Color.GRAY;
								break;
						}
						myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = newColor;
						myPanel.repaint();
						
						
						//On the left column and on the top row... do nothing
					} else {
						//On the grid other than on the left column and on the top row:
						Color newColor = null;
						switch (1) {
							
							case 1:
								newColor = Color.GRAY;
								break;
						}
						myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = newColor;
						myPanel.repaint();
					}
						}
					
				
				myPanel.repaint();
				break;
				
			case 3:		//Right mouse button  (cambiar el color a rojo) (no puede cambiar a rojo y el cuadro ya esta blanco)
				
				Component c1 = e.getComponent();
				while (!(c1 instanceof JFrame)) {
					c1 = c1.getParent();
					if (c1 == null) {
						return;
					}
				}
				JFrame myFrame1 = (JFrame)c1;
				MyPanel myPanel1 = (MyPanel) myFrame1.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
				Insets myInsets1 = myFrame1.getInsets();
				int x11 = myInsets1.left;
				int y11 = myInsets1.top;
				e.translatePoint(-x11, -y11);
				int x111 = e.getX();
				int y111 = e.getY();
				myPanel1.x = x11;
				myPanel1.y = y11;
				int gridX1 = myPanel1.getGridX(x11, y11);
				int gridY1 = myPanel1.getGridY(x11, y11);
			
				
				if (!(myPanel1.mouseDownGridX != gridX1) || (myPanel1.mouseDownGridY != gridY1)) //Released the mouse button on the same cell where it was pressed
				{
					Color currentColor = null;
					currentColor = myPanel1.colorArray[myPanel1.mouseDownGridX][myPanel1.mouseDownGridY];
					if (currentColor.equals(Color.WHITE)){
				
				if ((gridX1 == 0) || (gridY1 == 0)) {
					Color newColor = null;
					switch (1) {
						
						case 1:
							newColor = Color.RED;
							break;
					}
					myPanel1.colorArray[myPanel1.mouseDownGridX][myPanel1.mouseDownGridY] = newColor;
					myPanel1.repaint();
					
					
					//On the left column and on the top row... do nothing
				} else {
					//On the grid other than on the left column and on the top row:
					Color newColor = null;
					switch (1) {
						
						case 1:
							newColor = Color.RED;
							break;
					}
					myPanel1.colorArray[myPanel1.mouseDownGridX][myPanel1.mouseDownGridY] = newColor;
					myPanel1.repaint();
				}
					}
				}
					else {
						break;
					}
			myPanel1.repaint();
				
			
				//Do nothing
				break;
				
				
			default:    //Some other button (2 = Middle mouse button, etc.)
				//Do nothing
				break;
		}
	}
}