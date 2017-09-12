package com.checkers.engine.board;

import com.checkers.engine.piece.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.*;

import static com.checkers.engine.board.BoardUtils.NUM_TILES;

public abstract class Tile {
	
	private static final Map<Integer, EmptyTile> EMPTY_TILE_CACHE = createAllPossibleEmptyTiles();
	protected final int tileCoordinate;
	
	Tile(final int tileCoordinate) {
		
		this.tileCoordinate = tileCoordinate;
	}
	
	private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
		
		final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
		for (int i = 0; i < NUM_TILES; i++) {
			emptyTileMap.put(i, new EmptyTile(i));
		}
		return ImmutableMap.copyOf(emptyTileMap);
	}
	
	public static Tile createTile(final int tileCoordinate, final Piece piece) {
		return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILE_CACHE.get(tileCoordinate);
	}
	
	public int getTileCoordinate() {
		return tileCoordinate;
	}
	
	public abstract boolean isTileOccupied();
	
	public abstract Piece getPiece();
	
	private static final class EmptyTile extends Tile {
		
		public EmptyTile(final int tileCoordinate) {
			
			super(tileCoordinate);
			
		}
		
		@Override
		public String toString(){
			return "-";
		}
		
		@Override
		public boolean isTileOccupied() {
			
			return false;
		}
		
		@Override
		public Piece getPiece() {
			
			return null;
		}
	}
	
	private static final class OccupiedTile extends Tile {
		
		final Piece pieceOnTile;
		
		public OccupiedTile(final int tileCoordinate, final Piece pieceOnTile) {
			
			super(tileCoordinate);
			this.pieceOnTile = pieceOnTile;
		}
		
		@Override
		public String toString(){
			return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() :  getPiece()
					.toString();
		}
		
		@Override
		public boolean isTileOccupied() {
			
			return true;
		}
		
		@Override
		public Piece getPiece() {
			
			return this.pieceOnTile;
		}
	}
}
