package com.checkers.engine.board;

import com.checkers.engine.piece.*;

import static com.checkers.engine.board.Board.Builder;

public abstract class Move {
	
	protected final Board board;
	protected final Piece movedPiece;
	protected final int destinationCoordinate;
	
	public static final Move NULL_MOVE = new NullMove();
	
	public Move(final Board board,
	            final Piece movedPiece,
	            final int destinationCoordinate) {
		
		this.board = board;
		this.movedPiece = movedPiece;
		this.destinationCoordinate = destinationCoordinate;
	}
	
	@Override
	public int hashCode() {
		
		final int prime  = 31;
		int       result = 1;
		result = prime * result + this.destinationCoordinate;
		result = prime * result + this.movedPiece.hashCode();
		return result;
	}
	
	@Override
	public boolean equals(final Object other) {
		
		if (this == other) {
			return true;
		}
		if (!(other instanceof Move)) {
			return false;
		}
		final Move otherMove = (Move) other;
		return getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
				getMovedPiece().equals(otherMove.getMovedPiece());
	}
	
	public int getDestinationCoordinate() {
		
		return destinationCoordinate;
	}
	
	public Piece getMovedPiece() {
		
		return this.movedPiece;
	}
	
	public boolean isAttack() {
		
		return false;
	}
	
	public Piece getAttackedPiece() {
		
		return null;
	}
	
	public int getCurrentCoordinate() {
		
		return this.movedPiece.getPiecePosition();
	}
	
	public Board getBoard() {
		
		return board;
	}
	
	public Board execute() {
		
		final Builder builder = new Builder();
		//set player pieces
		for (final Piece piece : this.board.currentPlayer().getActivePiece()) {
			if (!this.movedPiece.equals(piece)) {
				builder.setPiece(piece);
			}
		}
		//set opponent pieces
		for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePiece()) {
			builder.setPiece(piece);
		}
		//move the movedPiece
		builder.setPiece(this.movedPiece.movePiece(this));
		//Change player
		builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
		//Build board
		return builder.build();
	}
	
	public static class MajorMove extends Move {
		
		public MajorMove(final Board board,
		                 final Piece piece,
		                 final int destinationCoordinate) {
			
			super(board, piece, destinationCoordinate);
		}
		
		@Override
		public boolean equals(final Object other) {
			
			return this == other || other instanceof MajorMove && super.equals(other);
		}
		
		@Override
		public String toString() {
			
			return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this
					.destinationCoordinate);
		}
		
	}
	
	public static class AttackMove extends Move {
		
		final Piece attackedPiece;
		
		public AttackMove(final Board board,
		                  final Piece piece,
		                  final int destinationCoordinate,
		                  final Piece attackedPiece) {
			
			super(board, piece, destinationCoordinate);
			this.attackedPiece = attackedPiece;
		}
		
		@Override
		public int hashCode() {
			
			return this.attackedPiece.hashCode() + super.hashCode();
		}
		
		@Override
		public boolean equals(final Object other) {
			
			if (this == other) {
				return true;
			}
			if (!(other instanceof AttackMove)) {
				return false;
			}
			final AttackMove otherAttackMove = (AttackMove) other;
			return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
		}
		
		@Override
		public String toString() {
			
			return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1) + "x" +
					BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
		
		@Override
		public Board execute() {
			
			final Builder builder = new Builder();
			for (final Piece piece : this.board.currentPlayer().getActivePiece()) {
				if (!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePiece()) {
				if (!piece.equals(this.getAttackedPiece())) {
					builder.setPiece(piece);
				}
			}
			int abs = 0;
			if (Math.abs(movedPiece.getPiecePosition() - this.getAttackedPiece().getPiecePosition()) % 7 == 0) {
				int den = Math.abs(movedPiece.getPiecePosition() - this.getAttackedPiece().getPiecePosition()) / 7;
				abs = Math.abs(movedPiece.getPiecePosition() - this.getAttackedPiece().getPiecePosition()) / den;
			}
			else if (Math.abs(movedPiece.getPiecePosition() - this.getAttackedPiece().getPiecePosition()) % 9 == 0) {
				int den = Math.abs(movedPiece.getPiecePosition() - this.getAttackedPiece().getPiecePosition()) / 9;
				abs = Math.abs(movedPiece.getPiecePosition() - this.getAttackedPiece().getPiecePosition()) / den;
			}
			int destinationAlg = this.getAttackedPiece().getPiecePosition() + (movedPiece.getPieceAlliance()
					.getDirection() * abs);
			builder.setPiece(this.movedPiece.movePiece(new AttackMove(board, movedPiece, destinationAlg, attackedPiece)));
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
		
		@Override
		public boolean isAttack() {
			
			return true;
		}
		
		@Override
		public Piece getAttackedPiece() {
			
			return this.attackedPiece;
		}
	}
	
	public static final class NullMove extends Move {
		
		public NullMove() {
			
			super(null, null, -1);
		}
		
		@Override
		public Board execute() {
			
			throw new RuntimeException("error: cant execute null moves");
		}
	}
	
	public static class MoveFactory {
		
		private MoveFactory() {
			
			throw new RuntimeException("error!");
		}
		
		public static Move createMove(final Board board,
		                              final int currentCoordinate,
		                              final int destinationCoordinate) {
			
			for (final Move move : board.getAllLegalMoves()) {
				if (move.getCurrentCoordinate() == currentCoordinate &&
						move.getDestinationCoordinate() == destinationCoordinate) {
					return move;
				}
			}
			return NULL_MOVE;
		}
	}
	
	public static class Promotion extends Move {
		
		final Move decoratedMove;
		final Single promotedSingle;
		
		public Promotion(final Move decoratedMove) {
			
			super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
			this.decoratedMove = decoratedMove;
			this.promotedSingle = (Single) decoratedMove.getMovedPiece();
		}
		
		@Override
		public Board execute() {
			final Board singleMovedBoard = this.decoratedMove.execute();
			final Board.Builder builder = new Builder();
			for(final Piece piece: singleMovedBoard.currentPlayer().getActivePiece()){
				if(!this.promotedSingle.equals(piece)){
					builder.setPiece(piece);
				}
			}
			for (final Piece piece : singleMovedBoard.currentPlayer().getOpponent().getActivePiece()){
				builder.setPiece(piece);
			}
			builder.setPiece(this.promotedSingle.getPromotionPiece().movePiece(this));
			builder.setMoveMaker(singleMovedBoard.currentPlayer().getAlliance());
			return builder.build();
		}
		
		@Override
		public boolean isAttack() {
			
			return this.decoratedMove.isAttack();
		}
		
		@Override
		public Piece getAttackedPiece() {
			
			return this.decoratedMove.getAttackedPiece();
		}
		
		@Override
		public String toString() {
			
			return "";
		}
		
		@Override
		public int hashCode() {
			
			return decoratedMove.hashCode() + (31 * promotedSingle.hashCode());
		}
		
		@Override
		public boolean equals(final Object other) {
			
			return this == other || other instanceof Promotion && (this.decoratedMove.equals(other));
		}
	}
}
