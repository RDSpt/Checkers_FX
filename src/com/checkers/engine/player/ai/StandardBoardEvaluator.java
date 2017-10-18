package com.checkers.engine.player.ai;

import com.checkers.engine.board.Board;
import com.checkers.engine.piece.Piece;
import com.checkers.engine.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {
	
	@Override
	public int evaluate(final Board board) {
		
		return scorePlayer(board.whitePlayer()) -
				scorePlayer(board.blackPlayer());
	}
	
	private int scorePlayer(final Player player) {
		
		return pieceValue(player) + mobility(player);
	}
	
	private static int mobility(final Player player) {
		
		return player.getLegalMoves().size() * 10;
	}
	
	private static int pieceValue(final Player player) {
		
		int pieceValueScore = 0;
		for (final Piece piece : player.getActivePiece()) {
			pieceValueScore += piece.getPieceValue();
		}
		return pieceValueScore;
	}
	
}
