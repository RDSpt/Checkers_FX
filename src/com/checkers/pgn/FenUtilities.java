package com.checkers.pgn;

import com.checkers.engine.board.*;

public class FenUtilities {
	
	private FenUtilities(){
		throw new RuntimeException("not instantiable!");
	}
	
	public static Board createGameFromFEN(final String fenString){
		return  null;
	}
	
	public static String createFENFromGame(final Board board){
		return calculateBoardText(board)+" "+
				calculateCurrentPlayerText(board) +" "+
				"0 1";
	}
	
	private static String calculateCurrentPlayerText(final Board board) {
		return board.currentPlayer().toString().substring(0,1).toLowerCase();
	}
	
	private static String calculateBoardText(final Board board) {
		final StringBuilder builder = new StringBuilder();
		for(int i = 0; i < BoardUtils.NUM_TILES; i++){
			final String tileText = board.getTile(i).toString();
			builder.append(tileText);
		}
		builder.insert(8,"/");
		builder.insert(17,"/");
		builder.insert(26,"/");
		builder.insert(35,"/");
		builder.insert(44,"/");
		builder.insert(53,"/");
		builder.insert(62,"/");
		
		return builder.toString().replaceAll("--------","8")
								 .replaceAll("-------","7")
								 .replaceAll("------","6")
								 .replaceAll("-----","5")
								 .replaceAll("----","4")
								 .replaceAll("---","3")
								 .replaceAll("--","2")
								 .replaceAll("-","1");
	}
	
}
