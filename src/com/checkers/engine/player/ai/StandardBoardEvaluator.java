package com.checkers.engine.player.ai;

import com.checkers.engine.board.*;
import com.checkers.engine.piece.Piece;
import com.checkers.engine.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {
	
	@Override
	public int evaluate(final Board board) {
		
		return scorePlayer(board.whitePlayer()) -
				scorePlayer(board.blackPlayer());
	}
	
	private int scorePlayer(final Player player) {
		
		return pieceValue(player) + mobility(player) + attackPossible(player) + promotionNear(player);
	}
	
	private int promotionNear(final Player player) {
		for (Move move: player.getLegalMoves()){
			if(move.isPromotion()){
				return 2000;
			}
		}
		
		
		return 0;
	}
	
	private int attackPossible(final Player player) {
		for (Move move : player.getLegalMoves()){
			if(move.isAttack()){
				return 10000;
			}
		}
		return 0;
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
