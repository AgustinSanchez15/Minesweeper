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
	private static final int TOTAL_ROWS = 11;   //Last row has only one (gray) cell (originalmente tenia 11 pero le puse 10 para que asi sea un verdadero 10X10
	private static int bombCounter =0;
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public boolean firstClick = true;
	public Color blanco = new Color (255,255,254);
	public Color rojo = new Color (255, 0, 1);
	public Random generator = new Random();
	private int grayCounter = 0 ;
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public int number[][] = new int [TOTAL_COLUMNS][TOTAL_ROWS];
	public boolean number1[][] = new boolean[TOTAL_COLUMNS][TOTAL_ROWS];
	
	
	
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
		}
		for (int y = 0; y < TOTAL_ROWS; y++) {   //Left column
			colorArray[0][y] = Color.LIGHT_GRAY;
		}
		for (int x = 1; x < TOTAL_COLUMNS; x++) {   //The rest of the grid
			for (int y = 1; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.WHITE;
			}
		}
		for(int i=1;i<10;i++) {
			for(int j=1;j<10;j++) {
				setBombCounter(0);
				countBombs(i, j);
				number[i][j] = getBombCounter();
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
					if(colorArray[x][y].equals(Color.GRAY) && number[x][y] >0) {
						g.setColor(Color.BLACK);
						g.drawString(String.valueOf(number[x][y]), x1 + GRID_X + (x * (INNER_CELL_SIZE + 1))+27, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1))+33);
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
		firstClick = false;
		
		if((x<=0) || (y<=0) || (x>=10) || (y>=10)){
			return;
		}

		else {
			setBombCounter(0);
			countBombs(x, y);
			if(getBombCounter() != 0 && colorArray[x][y].equals(Color.WHITE)) {
				colorArray[x][y] = Color.GRAY;
				grayCounter++;
				
				repaint();
				//System.out.println(getBombCounter());

			} else {
				if(getBombCounter() == 0 && colorArray[x][y].equals(Color.WHITE)) {
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

	public void countBombs (int x, int y) {
		if (colorArray[x][y].equals(Color.WHITE)) { 
			if (x == 9) {
				if(colorArray[x][y+1].equals(blanco) || colorArray[x][y+1].equals(rojo)) {
					bombCounter= bombCounter +1;
				}if(colorArray[x-1][y+1].equals(blanco) || colorArray[x-1][y+1].equals(rojo)) {
					bombCounter= bombCounter +1;
				}	if(colorArray[x-1][y].equals(blanco) || colorArray[x-1][y].equals(rojo)) {
					bombCounter= bombCounter +1;
				}	if(colorArray[x-1][y-1].equals(blanco) || colorArray[x-1][y-1].equals(rojo)) {
					bombCounter= bombCounter +1;
				}
				if(colorArray[x][y-1].equals(blanco) || colorArray[x][y-1].equals(rojo)) {
					bombCounter= bombCounter +1;
				}
			}
			else {
				if(colorArray[x][y+1].equals(blanco) || colorArray[x][y+1].equals(rojo)) {
					bombCounter= bombCounter +1;
				}
				if(colorArray[x][y-1].equals(blanco) || colorArray[x][y-1].equals(rojo)) {
					bombCounter= bombCounter +1;
				}
				if(colorArray[x+1][y].equals(blanco) || colorArray[x+1][y].equals(rojo)) {
					bombCounter= bombCounter +1;
				}
				if(colorArray[x+1][y+1].equals(blanco) || colorArray[x+1][y+1].equals(rojo)) {
					bombCounter= bombCounter +1;
				}
				if(colorArray[x+1][y-1].equals(blanco) || colorArray[x+1][y-1].equals(rojo)) {
					bombCounter= bombCounter +1;
				}
				if(colorArray[x-1][y+1].equals(blanco) || colorArray[x-1][y+1].equals(rojo)) {
					bombCounter= bombCounter +1;
				}
				if(colorArray[x-1][y].equals(blanco) || colorArray[x-1][y].equals(rojo)) {
					bombCounter= bombCounter +1;
				}
				if(colorArray[x-1][y-1].equals(blanco) || colorArray[x-1][y-1].equals(rojo)) {
					bombCounter= bombCounter +1;
				}
			}	
		}
	}	
	public int getBombCounter() {
		return bombCounter;}
	public void setBombCounter(int i) {
		bombCounter = i;
	}
	public int getGrayCounter() {
		return grayCounter;
	}
	public boolean isFirstClick() {
		return firstClick;
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