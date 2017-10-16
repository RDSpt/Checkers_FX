package com.checkers.engine.piece;

import com.checkers.engine.Alliance;
import com.checkers.engine.board.*;
import com.google.common.collect.ImmutableList;

import java.util.*;

public class Double extends Piece {
	
	private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -7, 7, 9};
	
	public Double(int piecePosition, Alliance pieceAlliance) {
		
		super(PieceType.DOUBLE, piecePosition, pieceAlliance);
	}
	
	private static boolean isFirstColumnExclusion(final int currentPosition,
	                                              final int candidateOffset) {
		
		return BoardUtils.FIRST_COLUMN[currentPosition] &&
				((candidateOffset == -9) ||
						(candidateOffset == 7));
	}
	
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		
		return BoardUtils.EIGHTH_COLUMN[currentPosition] &&
				((candidateOffset == -7) ||
						(candidateOffset == 9));
	}
	
	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		
		final List<Move> legalMoves = new ArrayList<>();
		for (final int currentCandidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {
			int candidateDestinationCoordinate = this.piecePosition;
			while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				if (isFirstColumnExclusion(candidateDestinationCoordinate, currentCandidateCoordinateOffset) ||
						isEighthColumnExclusion(candidateDestinationCoordinate, currentCandidateCoordinateOffset)) {
					break;
				}//if first or last column go left of right
				candidateDestinationCoordinate += currentCandidateCoordinateOffset;
				if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
					final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
					if (!candidateDestinationTile.isTileOccupied()) {//Check if tile is not occupied
						legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));//MOVE
					}
					else { //if it is occupied
						final Piece    pieceAtLocation = candidateDestinationTile.getPiece(); //get piece in location
						final Alliance pieceAlliance   = pieceAtLocation.getPieceAlliance(); //get piece alliance of the
						// location
						if (this.pieceAlliance != pieceAlliance) { //if enemy
							final int nextTile = candidateDestinationCoordinate + (this.pieceAlliance.getDirection() *
									currentCandidateCoordinateOffset);
							if (BoardUtils.isValidTileCoordinate(nextTile)) {
								final Tile finalDestination = board.getTile(nextTile);
								if (!finalDestination.isTileOccupied()) {
									legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate,
											pieceAtLocation));
								}
							}
							//ATTACK MOVE
						}
						break;
					}
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}
	
	@Override
	public Double movePiece(Move move) {
		
		return new Double(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
	}
	
	@Override
	public String toString() {
		
		return PieceType.DOUBLE.toString();
	}
}
