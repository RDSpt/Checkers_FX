package com.checkers.engine.player;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.*;
import com.checkers.engine.piece.Piece;

import java.util.Collection;

public class WhitePlayer extends Player {
	
	public WhitePlayer(final Board board,
	                   final Collection<Move> whiteStandardLegalMoves,
	                   final Collection<Move> blackStandardLegalMoves) {
		
		super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
	}
	
	@Override
	public Collection<Piece> getActivePiece() {
		
		return this.board.getWhitePieces();
	}
	
	@Override
	public Alliance getAlliance() {
		
		return Alliance.WHITE;
	}
	
	@Override
	public Player getOpponent() {
		
		return this.board.blackPlayer();
	}
}
