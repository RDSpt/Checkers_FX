package com.checkers.engine.player;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.*;
import com.checkers.engine.piece.Piece;

import java.util.Collection;

public abstract class Player {
	
	protected final Board board;
	protected final Collection<Move> legalMoves;
	
	public Player(final Board board,
	              final Collection<Move> legalMoves,
	              final Collection<Move> opponentMoves) {
		
		this.board = board;
		this.legalMoves = legalMoves;
	}
	
	public boolean isMoveLegal(final Move move){
		return this.legalMoves.contains(move);
	}
	
	public MoveTransition makeMove(final Move move){
		return  null;
	}
	
	public abstract Collection<Piece> getActivePiece();
	public abstract Alliance getAlliance();
	public abstract Player getOpponent();
}
