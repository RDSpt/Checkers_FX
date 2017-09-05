package com.checkers.engine.board;

import com.checkers.engine.Alliance;
import com.checkers.engine.piece.*;
import com.checkers.engine.player.*;
import com.google.common.collect.ImmutableList;

import java.util.*;

public class Board {
	
	private final List<Tile> gameBoard;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	
	private Board(Builder builder) {
		
		this.gameBoard = createGameBoard(builder);
		this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
		this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
		final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
		final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
		
		this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
	}
	
	public Collection<Piece> getWhitePieces() {
		
		return this.whitePieces;
	}
	
	public Collection<Piece> getBlackPieces() {
		
		return this.blackPieces;
	}
	
	public Player whitePlayer(){
		return this.whitePlayer;
	}
	
	public Player blackPlayer(){
		return this.blackPlayer;
	}
	
	public static Board createStandardBoard() {
		
		final Builder builder = new Builder();
		//Black
		//First Row
		builder.setPiece(new Single(1, Alliance.BLACK));
		builder.setPiece(new Single(3, Alliance.BLACK));
		builder.setPiece(new Single(5, Alliance.BLACK));
		builder.setPiece(new Single(7, Alliance.BLACK));
		//Second Row
		builder.setPiece(new Single(8, Alliance.BLACK));
		builder.setPiece(new Single(10, Alliance.BLACK));
		builder.setPiece(new Single(12, Alliance.BLACK));
		builder.setPiece(new Single(14, Alliance.BLACK));
		//Third Row
		builder.setPiece(new Single(17, Alliance.BLACK));
		builder.setPiece(new Single(19, Alliance.BLACK));
		builder.setPiece(new Single(21, Alliance.BLACK));
		builder.setPiece(new Single(23, Alliance.BLACK));
		//White
		//Third Row
		builder.setPiece(new Single(40, Alliance.WHITE));
		builder.setPiece(new Single(42, Alliance.WHITE));
		builder.setPiece(new Single(44, Alliance.WHITE));
		builder.setPiece(new Single(46, Alliance.WHITE));
		//Second Row
		builder.setPiece(new Single(49, Alliance.WHITE));
		builder.setPiece(new Single(51, Alliance.WHITE));
		builder.setPiece(new Single(53, Alliance.WHITE));
		builder.setPiece(new Single(55, Alliance.WHITE));
		//First Row
		builder.setPiece(new Single(56, Alliance.WHITE));
		builder.setPiece(new Single(58, Alliance.WHITE));
		builder.setPiece(new Single(60, Alliance.WHITE));
		builder.setPiece(new Single(62, Alliance.WHITE));
		//First Mover
		builder.setMoveMaker(Alliance.BLACK);
		return builder.build();
	}
	
	@Override
	public String toString(){
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			final String tileText = this.gameBoard.get(i).toString();
			builder.append(String.format("%3s",tileText));
			if((i + 1 )% BoardUtils.TILES_PER_ROW == 0){
				builder.append("\n");
			}
			
		}
		return builder.toString();
	}
	
	private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
		
		final List<Move> legalMoves = new ArrayList<>();
		for (final Piece piece : pieces) {
			legalMoves.addAll(piece.calculateLegalMoves(this));
		}
		return ImmutableList.copyOf(legalMoves);
	}
	
	private Collection<Piece> calculateActivePieces(final List<Tile> gameBoard,
	                                                final Alliance alliance) {
		
		final List<Piece> activePieces = new ArrayList<>();
		for (final Tile tile : gameBoard) {
			if (tile.isTileOccupied()) {
				final Piece piece = tile.getPiece();
				if (piece.getPieceAlliance() == alliance) {
					activePieces.add(piece);
				}
			}
		}
		return activePieces;
	}
	
	public Tile getTile(final int tileCoordinate) {
		
		return gameBoard.get(tileCoordinate);
	}
	
	private List<Tile> createGameBoard(final Builder builder) {
		
		final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
		}
		return ImmutableList.copyOf(tiles);
	}
	
	public static class Builder {
		
		Map<Integer, Piece> boardConfig;
		Alliance nextMoveMaker;
		
		public Builder() {
			this.boardConfig = new HashMap<>();
		}
		
		public Builder setPiece(final Piece piece) {
			
			this.boardConfig.put(piece.getPiecePosition(), piece);
			return this;
		}
		
		public Builder setMoveMaker(final Alliance nextMoveMaker) {
			
			this.nextMoveMaker = nextMoveMaker;
			return this;
		}
		
		public Board build() {
			
			return new Board(this);
		}
		
	}
}
