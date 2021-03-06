package com.checkers.engine.player.ai;

import com.checkers.engine.board.*;
import com.checkers.engine.player.MoveTransition;

public class MiniMax implements MoveStrategy {
	
	private final BoardEvaluator boardEvaluator;
	private final int searchDepth;
	
	public MiniMax(final int searchDepth) {
		
		this.boardEvaluator = new StandardBoardEvaluator();
		this.searchDepth = searchDepth;
	}
	
	@Override
	public String toString() {
		
		return "MiniMax";
	}
	
	@Override
	public Move execute(Board board) {
		
		final long startTime        = System.currentTimeMillis();
		Move       bestMove         = null;
		int        highestSeenValue = Integer.MIN_VALUE;
		int        lowestSeenValue  = Integer.MAX_VALUE;
		int        currentValue;
		int numMoves = board.currentPlayer().getLegalMoves().size();
		if(numMoves >0) {
			for (final Move move : board.currentPlayer().getLegalMoves()) {
				final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
				if (moveTransition.getMoveStatus().isDone()) {
					currentValue = board.currentPlayer().getAlliance().isWhite() ?
							min(moveTransition.getTransitionBoard(), searchDepth - 1) :
							max(moveTransition.getTransitionBoard(), searchDepth - 1);
					if (board.currentPlayer().getAlliance().isWhite() && currentValue >= highestSeenValue) {
						highestSeenValue = currentValue;
						bestMove = move;
					}
					else if (board.currentPlayer().getAlliance().isBlack() && currentValue <= lowestSeenValue) {
						lowestSeenValue = currentValue;
						bestMove = move;
					}
				}
			}
			final long executionTime = System.currentTimeMillis() - startTime;
			System.out.println("AI move: " + bestMove.toString());
			System.out.println(board.currentPlayer() + " took " + (executionTime / 1000) + " seconds thinking");
		}else
		{
			System.out.println("No more moves!");
		}
		return bestMove;
	}
	
	public int min(final Board board, final int depth) {
		
		if (depth == 0 || isEndGameScenario(board)) {
			return this.boardEvaluator.evaluate(board);
		}
		int lowestSeenValue = Integer.MAX_VALUE;
		for (final Move move : board.currentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
			if (moveTransition.getMoveStatus().isDone()) {
				final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);
				if (currentValue <= lowestSeenValue) {
					lowestSeenValue = currentValue;
				}
			}
		}
		return lowestSeenValue;
		
	}
	
	public int max(final Board board, final int depth) {
		
		if (depth == 0 || isEndGameScenario(board)) {
			return this.boardEvaluator.evaluate(board);
		}
		int highestSeenValue = Integer.MIN_VALUE;
		for (final Move move : board.currentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
			if (moveTransition.getMoveStatus().isDone()) {
				final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1);
				if (currentValue >= highestSeenValue) {
					highestSeenValue = currentValue;
				}
			}
		}
		return highestSeenValue;
	}
	
	private boolean isEndGameScenario(Board board) {
		return board.currentPlayer().getActivePiece().size() == 0;
	}
	// === END === //
}
