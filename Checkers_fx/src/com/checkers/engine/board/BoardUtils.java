package com.checkers.engine.board;

public class BoardUtils {
	
	//Columns
	public static final boolean[] FIRST_COLUMN = initColumn(0);
	public static final boolean[] SECOND_COLUMN = initColumn(1);
	public static final boolean[] THIRD_COLUMN = initColumn(2);
	public static final boolean[] FOURTH_COLUMN = initColumn(3);
	public static final boolean[] FIFTH_COLUMN = initColumn(4);
	public static final boolean[] SIXTH_COLUMN = initColumn(5);
	public static final boolean[] SEVENTH_COLUMN = initColumn(6);
	public static final boolean[] EIGHTH_COLUMN = initColumn(7);
	//Rows
	public static final boolean[] SECOND_ROW = initRow(8);
	public static final boolean[] SEVENTH_ROW = initRow(48);
	
	public static final int NUM_TILES = 64;
	public static final int TILES_PER_ROW = 8;
	//Constructor
	private BoardUtils() {
		
		throw new RuntimeException("Can't instantiate");
	}
	//Methods
	private static boolean[] initColumn(int columnNumber) {
		
		final boolean[] column = new boolean[NUM_TILES];
		do {
			column[columnNumber] = true;
			columnNumber += TILES_PER_ROW;
			
		} while (columnNumber < NUM_TILES);
		return column;
	}
	
	
	private static boolean[] initRow(int rowNumber) {
		final boolean[] row = new boolean[NUM_TILES];
		do{
			row[rowNumber] = true;
			rowNumber++;
		}while(rowNumber % TILES_PER_ROW !=0);
		return row;
	}
	
	public static boolean isValidTileCoordinate(int coordinate) {//if inside the board
		return coordinate >= 0 && coordinate < 64;
	}
}
