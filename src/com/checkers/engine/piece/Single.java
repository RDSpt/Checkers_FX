package com.checkers.engine.piece;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.*;
import com.google.common.collect.ImmutableList;

import java.util.*;

public class Single extends Piece {
	
	private final static int[] CANDIDATE_MOVE_COORDINATES = {9, 7};
	
	public Single(final int piecePosition,
	              final Alliance pieceAlliance) {
		
		super(PieceType.SINGLE, piecePosition, pieceAlliance);
	}
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) { //Excludes
		// offset of first column
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == 9);
	}
	
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) { //Excludes
		// offset of eighth column
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 7);
	}
	
	@Override
	public Collection<Move> calculateLegalMoves(final Board board) { //Calculate possible moves
		final List<Move> legalMoves = new ArrayList<>();
		for (int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) { //Check all possible coordinates
			int candidateDestinationCoordinate;
			candidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() *
					currentCandidateOffset);
			if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
				if (currentCandidateOffset == 7 &&
						!((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
								(BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
					if (!candidateDestinationTile.isTileOccupied()) {//Check if tile is not occupied
						legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));//MOVE
					}
					else { //if it is occupied
						final Piece    pieceAtLocation = candidateDestinationTile.getPiece(); //get piece in location
						final Alliance pieceAlliance   = pieceAtLocation.pieceAlliance; //get piece alliance of the
						// location
						if (this.pieceAlliance != pieceAlliance) { //if enemy
							legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate,
									pieceAtLocation));
							//ATTACK MOVE
						}
					}
				}
				else if (currentCandidateOffset == 9 &&
						!((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
								(BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
					if (!candidateDestinationTile.isTileOccupied()) {//Check if tile is not occupied
						legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));//MOVE
					}
					else { //if it is occupied
						final Piece    pieceAtLocation = candidateDestinationTile.getPiece(); //get piece in location
						final Alliance pieceAlliance   = pieceAtLocation.pieceAlliance; //get piece alliance of the
						// location
						if (this.pieceAlliance != pieceAlliance) { //if enemy
							legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate,
									pieceAtLocation));
							//ATTACK MOVE
						}
					}
				}
				//TODO promotion
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}
	
	@Override
	public Single movePiece(Move move) {
		
		return new Single(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
	}
	
	@Override
	public String toString() {
		
		return PieceType.SINGLE.toString();
	}
}
