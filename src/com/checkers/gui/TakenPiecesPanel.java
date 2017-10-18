package com.checkers.gui;

import com.checkers.engine.board.Move;
import com.checkers.engine.piece.Piece;
import com.google.common.primitives.Ints;
import javafx.geometry.Insets;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.*;

import static com.checkers.gui.Table.MoveLog;

public class TakenPiecesPanel extends Pane {
	
	private static final Color PANEL_COLOR = Color.gray(0.50);
	private final Pane northPanel = new Pane();
	private final Pane southPanel = new Pane();
	
	public TakenPiecesPanel() {
		
		defineGrid();
		setBackground(new Background(new BackgroundFill(PANEL_COLOR,
				CornerRadii.EMPTY, Insets.EMPTY)));
		getChildren().add(new VBox(northPanel, southPanel));
		setPrefSize(40, 80);
	}
	
	public void defineGrid() {
		
		GridPane northGrid = new GridPane();
		GridPane southGrid = new GridPane();
		for (int c = 0; c < 2; c++) {
			northGrid.addColumn(c, new Pane());
			southGrid.addColumn(c, new Pane());
			for (int r = 0; r < 8; r++) {
				northGrid.addRow(r, new Pane());
				southGrid.addRow(r, new Pane());
			}
			northPanel.getChildren().clear();
			southPanel.getChildren().clear();
			northPanel.getChildren().add(northGrid);
			southPanel.getChildren().add(southGrid);
		}
	}
	
	public void redo(final MoveLog moveLog) {
		
		southPanel.getChildren().removeAll();
		northPanel.getChildren().removeAll();
		final List<Piece> whiteTakenPieces = new ArrayList<>();
		final List<Piece> blackTakenPieces = new ArrayList<>();
		for (final Move move : moveLog.getMoves()) {
			if (move.isAttack()) {
				final Piece takenPiece = move.getAttackedPiece();
				if (takenPiece.getPieceAlliance().isWhite()) {
					whiteTakenPieces.add(takenPiece);
				}
				else if (takenPiece.getPieceAlliance().isBlack()) {
					blackTakenPieces.add(takenPiece);
				}
				else {
					throw new RuntimeException("error! no other aliance");
				}
			}
		}
		Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
			
			@Override
			public int compare(Piece o1, Piece o2) {
				
				return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
			}
		});
		Collections.sort(blackTakenPieces, new Comparator<Piece>() {
			
			@Override
			public int compare(Piece o1, Piece o2) {
				
				return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
			}
		});
		for (final Piece takenPiece : whiteTakenPieces) {
			final Image image = new Image("/Graphics/" +
					takenPiece.getPieceAlliance().toString().substring(0, 1) +
					takenPiece.toString() +
					".png", 50, 50, true, true);
			final ImageView imageView = new ImageView(image);
			this.southPanel.getChildren().add(imageView);
		}
		for (final Piece takenPiece : blackTakenPieces) {
			final Image image = new Image("/Graphics/" +
					takenPiece.getPieceAlliance().toString().substring(0, 1) +
					takenPiece.toString() +
					".png", 50, 50, true, true);
			final ImageView imageView = new ImageView(image);
			this.southPanel.getChildren().add(imageView);
		}
	}
}
