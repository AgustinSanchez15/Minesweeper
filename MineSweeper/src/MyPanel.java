import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Random;

import javax.swing.JPanel;

public class MyPanel extends JPanel {
	
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 25;
	private static final int INNER_CELL_SIZE = 60;
	private static final int TOTAL_COLUMNS = 10;
	private static final int TOTAL_ROWS = 11;   
	private static int bombCounter =0;
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public Random generator = new Random();
	private int grayCounter = 0 ;
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public boolean[][] isBomb = new boolean[TOTAL_COLUMNS][TOTAL_ROWS];
	
	
	
	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //Top row
			colorArray[x][0] = Color.LIGHT_GRAY;
			isBomb[x][0] = false;
		}
		for (int y = 0; y < TOTAL_ROWS; y++) {   //Left column
			colorArray[0][y] = Color.LIGHT_GRAY;
			isBomb[0][y] = false;
		}
		for (int x = 1; x < TOTAL_COLUMNS; x++) {   //The rest of the grid
			for (int y = 1; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.WHITE;
				isBomb[x][y] = false;
			}
		}
	}



	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x1, y1, width + 1, height + 1);

		//Draw the grid minus the bottom row (which has only one cell)
		//By default, the grid will be 10x10 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS - 1; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)));
		}

		//Draw an additional cell at the bottom left
		g.drawRect(x1 + GRID_X, y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)), INNER_CELL_SIZE + 1, INNER_CELL_SIZE + 1);

		//Paint cell colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				if ((x == 0) || (y != TOTAL_ROWS - 1)) {
					Color c = colorArray[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);

					//Lets draw a string
					if(colorArray[x][y].equals(Color.GRAY) && MyMouseAdapter.getNumber()[x][y] > 0) {
						g.setColor(Color.BLACK);
						isBomb[x][y] = false;
						g.drawString(String.valueOf(MyMouseAdapter.getNumber()[x][y]), x1 + GRID_X + (x * (INNER_CELL_SIZE + 1))+27, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1))+33);
					}
				}
			}
		}
	}


	// This method helps to find the adjacent boxes that don't have a mine.
	// It is partially implemented since the verify hasn't been discussed in class
	// Verify that the coordinates in the parameters are valid.
	// Also verifies if there are any mines around the x,y coordinate
	public void revealAdjacent(int x, int y){
		
		if((x<=0) || (y<=0) || (x>=10) || (y>=10)){
			return;
		}

		else {
			setBombCounter(0);
			countBombs(x, y);
			if(bombCounter!=0) {
			}
			if(getBombCounter() != 0 && !isBomb[x][y] && colorArray[x][y] != Color.GRAY && colorArray[x][y] != Color.RED) {
				colorArray[x][y] = Color.GRAY;
				grayCounter++;
				repaint();

			} else {
				if(getBombCounter() == 0 && !isBomb[x][y] && colorArray[x][y] != Color.GRAY && colorArray[x][y] != Color.RED) {
					colorArray[x][y] = Color.GRAY;
					grayCounter++;
					revealAdjacent(x, y-1);
					revealAdjacent(x+1, y-1);
					revealAdjacent(x+1, y);
					revealAdjacent(x+1, y+1);
					revealAdjacent(x, y+1);
					revealAdjacent(x-1, y+1);
					revealAdjacent(x-1, y);
					revealAdjacent(x-1, y-1);
				}
			}
		}
	}

	//Count the bombs on the current panel
	public void countBombs (int x, int y) {
			if (x == 9) {
				if( isBomb[x][y+1]) {
					bombCounter= bombCounter +1;
				}if(isBomb[x-1][y+1]) {
					bombCounter= bombCounter +1;
				}	if(isBomb[x-1][y]) {
					bombCounter= bombCounter +1;
				}	if( isBomb[x-1][y-1]) {
					bombCounter= bombCounter +1;
				}
				if(isBomb[x][y-1]) {
					bombCounter= bombCounter +1;
				}
			}
			else {
				if(isBomb[x][y+1]) {
					bombCounter= bombCounter +1;
				}
				if(isBomb[x][y-1]) {
					bombCounter= bombCounter +1;
				}
				if(isBomb[x+1][y]) {
					bombCounter= bombCounter +1;
				}
				if(isBomb[x+1][y+1]) {
					bombCounter= bombCounter +1;
				}
				if(isBomb[x+1][y-1]) {
					bombCounter= bombCounter +1;
				}
				if(isBomb[x-1][y+1]) {
					bombCounter= bombCounter +1;
				}
				if(isBomb[x-1][y]) {
					bombCounter= bombCounter +1;
				}
				if(isBomb[x-1][y-1]) {
					bombCounter= bombCounter +1;
				}
			}	
		}
	
	//Getter for bombCounter
	public int getBombCounter() {
		return bombCounter;}
	
	//Setter for bombCounter
	public void setBombCounter(int i) {
		bombCounter = i;
	}
	
	//Setter for grayCounter
	public void setGrayCounter(int i){
		grayCounter = i;
	}
	
	//Getter for grayCounter
	public int getGrayCounter() {
		return grayCounter;
	}




	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return x;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	} 
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return y;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
}