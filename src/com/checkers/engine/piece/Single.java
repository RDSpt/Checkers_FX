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
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == 7) ||
				BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9);
	}
	
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) { //Excludes
		// offset of eighth column
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7) ||
				BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 9);
	}
	
	@Override
	public Collection<Move> calculateLegalMoves(final Board board) { //Calculate possible moves
		final List<Move> legalMoves = new ArrayList<>();
		for (int currentCandidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) { //Check all possible coordinates
			int destinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() *
					currentCandidateCoordinateOffset);
			if (BoardUtils.isValidTileCoordinate(destinationCoordinate)) {
				final Tile candidateDestinationTile = board.getTile(destinationCoordinate);
				if (isFirstColumnExclusion(this.piecePosition, (this.pieceAlliance.getDirection() *
						currentCandidateCoordinateOffset)) ||
						isEighthColumnExclusion(this.piecePosition, (this.pieceAlliance.getDirection() *
								currentCandidateCoordinateOffset))) {
					break;
				}
				if (!board.getTile(destinationCoordinate).isTileOccupied()) {//is tile empty?
					if (BoardUtils.isValidTileCoordinate(destinationCoordinate)) {
						if (this.pieceAlliance.isPromotionSquare(destinationCoordinate)) {
							legalMoves.add(new Move.Promotion(new Move.MajorMove(board, this, destinationCoordinate)));
						}
						else {
							legalMoves.add(new Move.MajorMove(board, this, destinationCoordinate));//MOVE}
						}
					}
				}
				else {//tile occupied
					final Piece    pieceAtLocation = candidateDestinationTile.getPiece(); //get piece in location
					final Alliance pieceAlliance   = pieceAtLocation.pieceAlliance; //get piece alliance of the
					if (this.pieceAlliance != pieceAlliance) {
						if (BoardUtils.FIRST_COLUMN[destinationCoordinate] || BoardUtils
								.EIGHTH_COLUMN[destinationCoordinate]) {
							break;
						}
						else {
							final int nextTile = destinationCoordinate + (this.pieceAlliance.getDirection() *
									currentCandidateCoordinateOffset);
							if (BoardUtils.isValidTileCoordinate(nextTile)) {
								final Tile finalDestination = board.getTile(nextTile);
								if (!finalDestination.isTileOccupied()) {
									if (this.pieceAlliance.isPromotionSquare(nextTile)) {
										legalMoves.add(new Move.Promotion(new Move.AttackMove(board, this,
												destinationCoordinate, pieceAtLocation)));
									}
									else {
										legalMoves.add(new Move.AttackMove(board, this, destinationCoordinate,
												pieceAtLocation));
									}
								}
							}
						}
					}
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}
	
	@Override
	public Single movePiece(Move move) {
		
		return new Single(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
	}
	
	@Override
	public String toString() {
		
		return PieceType.SINGLE.toString();
	}
	
	public Piece getPromotionPiece() {
		
		return new Double(this.piecePosition, this.pieceAlliance);
	}
}
