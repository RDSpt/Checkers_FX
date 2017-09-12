package com.checkers.engine.player;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.*;
import com.checkers.engine.piece.Piece;
import com.google.common.collect.ImmutableList;

import javax.management.ImmutableDescriptor;
import java.util.*;

public abstract class Player {
	
	protected final Board board;
	protected final Collection<Move> legalMoves;
	
	public Player(final Board board,
	              final Collection<Move> legalMoves,
	              final Collection<Move> opponentMoves) {
		
		this.board = board;
		this.legalMoves = legalMoves;
	}
	
	public static Collection<Move> calculateAttacksOnTile(final int piecePosition, Collection<Move> moves){
		final List<Move> attackMoves = new ArrayList<>();
		for(final Move move : moves){
			if(piecePosition == move.getDestinationCoordinate()){
				attackMoves.add(move);
			}
		}
		return ImmutableList.copyOf(attackMoves);
	}
	
	public boolean isMoveLegal(final Move move){
		return this.legalMoves.contains(move);
	}
	
	public Collection<Move> getLegalMoves() {
		
		return legalMoves;
	}
	
	public MoveTransition makeMove(final Move move){
		if(!isMoveLegal(move)){
			return new MoveTransition(this.board,move,MoveStatus.ILLEGAL_MOVE);
		}
		final Board transitionBoard = move.execute();
		return new MoveTransition(transitionBoard,move, MoveStatus.DONE);
	}
	
	public abstract Collection<Piece> getActivePiece();
	public abstract Alliance getAlliance();
	public abstract Player getOpponent();
}
