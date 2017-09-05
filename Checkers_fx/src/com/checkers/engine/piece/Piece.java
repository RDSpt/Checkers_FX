package com.checkers.engine.piece;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.*;

import java.util.*;

public abstract class Piece {
	
	protected final PieceType pieceType;
	protected final int piecePosition;
	protected Alliance pieceAlliance;
	
	public Piece(final PieceType pieceType,
	             final int piecePosition,
	             final Alliance pieceAlliance) {
		
		this.pieceType = pieceType;
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
	}
	
	public PieceType getPieceType() {
		
		return pieceType;
	}
	
	public abstract Collection<Move> calculateLegalMoves(final Board board);
	
	public Alliance getPieceAlliance() {
		
		return pieceAlliance;
	}
	
	public int getPiecePosition() {
		return piecePosition;
	}
	
	public enum PieceType{
		SINGLE("O"),
		DOUBLE("Q");
		
		private String pieceName;
		
		PieceType(final String pieceName){
			this.pieceName = pieceName;
		}
		
		@Override
		public String toString(){
			return this.pieceName;
		}
		
	}
}
