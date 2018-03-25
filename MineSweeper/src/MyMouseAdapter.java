
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class MyMouseAdapter extends MouseAdapter {
	private Random generator = new Random();
	public int maxBombs = 10;
	public boolean firstClick = true;
	public Color blanco = new Color (255,255,254);
	private static int number[][] = new int [10][11];

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


			JLabel endLabel = new JLabel("GAME OVER!",JLabel.CENTER);
			JLabel winLabel = new JLabel ("You win!! ",JLabel.CENTER);
			endLabel.setForeground(Color.RED);
			winLabel.setForeground(Color.BLUE);

			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			int gridX = myPanel.getGridX(x, y);
			int gridY = myPanel.getGridY(x, y);

			if(firstClick == true) {
				while (maxBombs > 0 ) { // implementa las bombas 
					int randX = generator.nextInt(9)+1;
					int randY = generator.nextInt(9)+1;

					if(randX != myPanel.mouseDownGridX && randY != myPanel.mouseDownGridY) {
						if (!(myPanel.colorArray[randX][randY].equals(blanco))) {
							myPanel.colorArray[randX][randY] = blanco;
							maxBombs= maxBombs - 1;
							//System.out.println(randX + ", " + randY);
						}
					}
				}
				for(int i=1;i<10;i++) {
					for(int j=1;j<10;j++) {
						myPanel.setBombCounter(0);
						myPanel.countBombs(i, j);
						number[i][j] = myPanel.getBombCounter();
					}
				}
				firstClick = false;
			}

			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
				//Had pressed outside
				//Do nothing
			} else {
				if ((gridX == -1) || (gridY == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					} else {
						//Released the mouse button on the same cell where it was pressed
						if ((gridX == 0) || (gridY == 0)) {
							//On the left column and on the top row... do nothing
						} else {
							//On the grid other than on the left column and on the top row:
							Color newColor = null;
							Color rojo = new Color (255,0,1);


							if(myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(Color.WHITE)) {
								myPanel.revealAdjacent(gridX, gridY);

							} else if(myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(Color.RED)) {

							} else if(myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(Color.GRAY)) {

							} else if(myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(rojo)){

							}
							else {
								myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = Color.BLACK;
								//ENDGAME CODE
								myFrame.add(endLabel);
								myPanel.setVisible(false);
							}

						}	if (myPanel.getGrayCounter() == 71) {
							myFrame.add(winLabel);
							myPanel.setVisible(false);
							// win Code here
						}
					}
				}
			}
			myPanel.repaint();
			break;
		case 3:		//Right mouse button  

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
			myPanel1.x = x111;
			myPanel1.y = y111;
			int gridX1 = myPanel1.getGridX(x111, y111);
			int gridY1 = myPanel1.getGridY(x111, y111);


			if ((myPanel1.mouseDownGridX == -1) || (myPanel1.mouseDownGridY == -1)) {
				//Had pressed outside
				//Do nothing
			} else {
				if ((gridX1 == -1) || (gridY1 == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel1.mouseDownGridX != gridX1) || (myPanel1.mouseDownGridY != gridY1)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					} else {
						if ((gridX1 == 0) || (gridY1 == 0)) {
							//On the left column and on the top row... do nothing
						} else {
							//On the grid other than on the left column and on the top row:
							Color currentColor = myPanel1.colorArray[myPanel1.mouseDownGridX][myPanel1.mouseDownGridY];
							Color rojo = new Color (255,0,1);

							if(currentColor.equals(Color.WHITE)) {
								//If the panel is white, change to red
								myPanel1.colorArray[myPanel1.mouseDownGridX][myPanel1.mouseDownGridY] = Color.RED;
								myPanel1.repaint();
							} else if(currentColor.equals(blanco)) {
								myPanel1.colorArray[myPanel1.mouseDownGridX][myPanel1.mouseDownGridY] = rojo;
								myPanel1.repaint();

							} else if (currentColor.equals(Color.RED)){
								//If the panel is red, then change to white
								myPanel1.colorArray[myPanel1.mouseDownGridX][myPanel1.mouseDownGridY] = Color.WHITE;
								myPanel1.repaint();
							} 
							else if (currentColor.equals(rojo)) {
								//If the panel is rojo, then change to blanco
								myPanel1.colorArray[myPanel1.mouseDownGridX][myPanel1.mouseDownGridY] = blanco;
								myPanel1.repaint();
							}
						}
					}
				}
			}

			myPanel1.repaint();

			//Do nothing
			break;


		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
	public static int[][] getNumber() {
		return number;
	}
}