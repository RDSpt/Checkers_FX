package com.checkers.engine.piece;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.*;

import java.util.Collection;

public abstract class Piece {
	
	protected final PieceType pieceType;
	protected final int piecePosition;
	private final int cachedHashCode;
	protected Alliance pieceAlliance;
	
	public Piece(final PieceType pieceType,
	             final int piecePosition,
	             final Alliance pieceAlliance) {
		
		this.pieceType = pieceType;
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
		this.cachedHashCode = calculateHashCode();
	}
	
	@Override
	public int hashCode() {
		
		return this.cachedHashCode;
	}
	
	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Piece)) {
			return false;
		}
		final Piece otherPiece = (Piece) other;
		return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
				pieceAlliance == otherPiece.getPieceAlliance();
	}
	
	public int calculateHashCode() {
		
		int result = pieceType != null ? pieceType.hashCode() : 0;
		result = 31 * result + piecePosition;
		result = 31 * result + (pieceAlliance != null ? pieceAlliance.hashCode() : 0);
		return result;
	}
	
	public PieceType getPieceType() {
		
		return pieceType;
	}
	
	public abstract Collection<Move> calculateLegalMoves(final Board board);
	
	public abstract Piece movePiece(Move move);
	
	public Alliance getPieceAlliance() {
		
		return pieceAlliance;
	}
	
	public int getPiecePosition() {
		
		return piecePosition;
	}
	
	public int getPieceValue(){
		return this.pieceType.getPieceValue();
	};
	
	public enum PieceType {
		SINGLE("S",200),
		DOUBLE("D",400);
		
		private String pieceName;
		private int pieceValue;
		
		PieceType(final String pieceName, final int pieceValue) {
			
			this.pieceName = pieceName;
			this.pieceValue = pieceValue;
		}
		
		public int getPieceValue(){
			return this.pieceValue;
		}
		
		@Override
		public String toString() {
			
			return this.pieceName;
		}
	}
}
