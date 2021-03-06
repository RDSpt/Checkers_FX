package com.checkers.engine.player;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.*;
import com.checkers.engine.piece.Piece;

import java.util.Collection;

public class BlackPlayer extends Player {
	
	public BlackPlayer(final Board board,
	                   final  Collection<Move> whiteStandardLegalMoves,
	                   final  Collection<Move> blackStandardLegalMoves) {
		
		super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
	}
	
	@Override
	public Collection<Piece> getActivePiece() {
		
		return this.board.getBlackPieces();
	}
	
	@Override
	public Alliance getAlliance() {
		
		return Alliance.BLACK;
	}
	
	@Override
	public Player getOpponent() {
		
		return this.board.whitePlayer();
	}
}
