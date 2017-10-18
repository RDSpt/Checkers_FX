package com.checkers.gui;

import com.checkers.engine.board.*;
import com.checkers.engine.piece.Piece;
import com.checkers.engine.player.MoveTransition;
import com.checkers.engine.player.ai.*;
import com.google.common.collect.Lists;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.checkers.engine.board.BoardUtils.*;
import static com.checkers.engine.board.Move.MoveFactory;
import static com.checkers.gui.Controller.*;

public class Table extends Observable {
	
	BoardPanel boardPanel;
	
	private Board checkersBoard = Board.createStandardBoard();
	private BoardDirection boardDirection;
	private Tile sourceTile;
	private Tile destinationTile;
	private Piece humanMovedPiece;
	private TakenPiecesPanel takenPiecesPanel = new TakenPiecesPanel();
	private SwingNode gameHistoryPanel = new SwingNode();
	private GameHistoryPanel gameHistoryPane = new GameHistoryPanel();
	private boolean highlightLegalMoves;
	private MoveLog moveLog;
	public static GameSetup gameSetup;
	
	private static final Table INSTANCE = new Table();
	private Move computerMove;
	
	private Table() {
		boardPanel = new BoardPanel();
		this.moveLog = new MoveLog();
		boardDirection = BoardDirection.NORMAL;
		this.gameSetup = new GameSetup(true);
		gameSetup.setBlackPlayerType(PlayerType.HUMAN);
		gameSetup.setWhitePlayerType(PlayerType.COMPUTER);
		this.addObserver(new TableGameAIWatcher());
		createTableMenuBar();
		MainPANE.getChildren().add(takenPiecesPanel);
		MainPANE.getChildren().add(boardPanel);
		boardPanel.setBorder(new Border(new BorderStroke(Color.gray(0.50), BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				new BorderWidths(10))));
		highlightLegalMoves = true;
	}
	
	public static Table get() {
		
		return INSTANCE;
	}
	
	public void show() {
		
		Table.get().getMoveLog().clear();
		Table.get().getGameHistoryPane().redo(checkersBoard, Table.get().getMoveLog());
		Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
		Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
	}
	
	private GameSetup getGameSetup() {
		
		return this.gameSetup;
	}
	
	private Board getGameBoard() {
		
		return this.checkersBoard;
	}
	
	//Adds Menus to Menu Bar
	public void createTableMenuBar() {
		
		MenuBAR.getMenus().add(createFileMenu());
		MenuBAR.getMenus().add(createPreferencesMenu(boardPanel));
		MenuBAR.getMenus().add(createOptionsMenu());
	}
	
	//Creates File Menu
	private Menu createFileMenu() {
		
		final Menu     fileMenu = new Menu("File");
		final MenuItem openPGN  = new MenuItem("Load PGN FIle");
		openPGN.setOnAction(event -> System.out.println("Open PGN file"));
		final MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.setOnAction(event -> System.exit(0));
		fileMenu.getItems().add(openPGN);
		fileMenu.getItems().add(exitMenuItem);
		return fileMenu;
	}
	
	//Create Preferences Menu
	private Menu createPreferencesMenu(final BoardPanel boardPanel) {
		
		final Menu     preferencesMenu   = new Menu("Preferences");
		final MenuItem flipBoardMenuItem = new MenuItem("Flip Board");
		flipBoardMenuItem.setOnAction(event -> {
			boardDirection = boardDirection.opposite();
			boardPanel.drawBoard(checkersBoard);
		});
		final CheckMenuItem legalMoveHighlightCheckbox = new CheckMenuItem("Highlight Legal Moves");
		legalMoveHighlightCheckbox.setSelected(true);
		legalMoveHighlightCheckbox.setOnAction(event -> highlightLegalMoves = legalMoveHighlightCheckbox.isSelected());
		//Add to Menu
		//preferencesMenu.getItems().add(flipBoardMenuItem);
		preferencesMenu.getItems().add(new SeparatorMenuItem());
		preferencesMenu.getItems().add(legalMoveHighlightCheckbox);
		return preferencesMenu;
	}
	
	private Menu createOptionsMenu() {
		
		final Menu     optionsMenu       = new Menu("Options");
		final MenuItem setupGameMenuItem = new MenuItem("Setup Game");
		setupGameMenuItem.setOnAction(event -> {
			Stage stage = new Stage();
			//Table.get().getGameSetup().show();
			Table.get().setupUpdate(Table.get().getGameSetup());
			stage.setTitle("Setup Game");
			stage.setScene(new GameSetup(true));
			stage.showAndWait();
			
		});
		optionsMenu.getItems().add(setupGameMenuItem);
		return optionsMenu;
	}
	
	private void setupUpdate(final GameSetup gameSetup) {
		
		setChanged();
		notifyObservers(gameSetup);
	}
	
	private static class TableGameAIWatcher implements Observer {
		
		@Override
		public void update(final Observable o, Object arg) {
			
			if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().currentPlayer())) {
				//create an AI thread
				//execute AI work
				final AIThinkTank thinkTank = new AIThinkTank();
				thinkTank.execute();
			}
		}
		
	}
	
	private static class AIThinkTank {
		
		private AIThinkTank() {
		
		}
		
		private void execute(){
			Platform.runLater(() -> {
				final MoveStrategy miniMax  = new MiniMax(gameSetup.getSearchDepthValue());
				final Move         bestMove = miniMax.execute(Table.get().getGameBoard());
				Table.get().updateComputerMove(bestMove);
				Table.get().updateGameBoard(Table.get().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
				Table.get().getMoveLog().addMove(bestMove);
				Table.get().getGameHistoryPane().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
				Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
				Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
				Table.get().moveMadeUpdate(PlayerType.COMPUTER);
			});
		}
	}
	
	public void updateGameBoard(final Board board) {
		
		this.checkersBoard = board;
	}
	
	public void updateComputerMove(final Move move) {
		
		this.computerMove = move;
	}
	
	private MoveLog getMoveLog() {
		
		return this.moveLog;
	}
	
	private GameHistoryPanel getGameHistoryPane() {
		
		return this.gameHistoryPane;
	}
	
	private TakenPiecesPanel getTakenPiecesPanel() {
		
		return this.takenPiecesPanel;
	}
	
	private BoardPanel getBoardPanel() {
		
		return this.boardPanel;
	}
	
	private void moveMadeUpdate(final PlayerType playerType) {
		
		setChanged();
		notifyObservers(playerType);
	}
	
	private boolean isRightMouseButton(MouseEvent event) {
		
		if (event.getButton() == MouseButton.SECONDARY)
			return true;
		else
			return false;
	}
	
	private boolean isLeftMouseButton(MouseEvent event) {
		
		if (event.getButton() == MouseButton.PRIMARY)
			return true;
		else
			return false;
	}
	
	public enum BoardDirection {
		NORMAL {
			@Override
			List<TilePanel> traverse(final List<TilePanel> boardTiles) {
				
				return boardTiles;
			}
			
			@Override
			BoardDirection opposite() {
				
				return FLIPPED;
			}
		},
		FLIPPED {
			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				
				return Lists.reverse(boardTiles);
			}
			
			@Override
			BoardDirection opposite() {
				
				return NORMAL;
			}
		};
		
		abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
		
		abstract BoardDirection opposite();
	}
	
	private class BoardPanel extends GridPane {
		
		final List<TilePanel> boardTiles;
		TilePanel tilePanel;
		GridPane grid = new GridPane();
		
		private BoardPanel() {
			
			this.boardTiles = new ArrayList<>();
			int tileNum = 0;
			for (int r = 0; r < TILES_PER_ROW; r++) {
				for (int c = 0; c < TILES_PER_ROW; c++) {
					tilePanel = new TilePanel(this, tileNum);
					grid.setAlignment(Pos.CENTER);
					grid.add(tilePanel, c, r);
					this.boardTiles.add(tilePanel);
					tileNum++;
				}
				setPrefSize(400, 350);
				
			}
			add(grid, 1, 2);
			
		}
		
		public void drawBoard(final Board board) {
			
			getChildren().removeAll();
			getChildren().clear();
			for (TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
				tilePanel.getChildren().clear();
				tilePanel.drawTile(board);
				getChildren().add(tilePanel);
			}
		}
	}
	
	enum PlayerType {
		HUMAN,
		COMPUTER;
	}
	
	private class TilePanel extends TilePane {
		
		private final int tileId;
		
		TilePanel(final BoardPanel boardPanel,
		          final int tileId) {
			
			this.tileId = tileId;
			setMinSize(60, 60);
			assignTileColor();
			assignTilePieceIcon(checkersBoard);
			highlightLegals(checkersBoard);
			addMouseListener(boardPanel);
			
		}
		
		private void addMouseListener(final BoardPanel boardPanel) {
			
			setOnMouseClicked(event -> {
				if (isRightMouseButton(event)) {
					//reset click
					sourceTile = null;
					destinationTile = null;
					humanMovedPiece = null;
				}
				else if (isLeftMouseButton(event)) {
					//first click
					if (sourceTile == null) { //not clicked yet
						sourceTile = checkersBoard.getTile(tileId);//select Tile
						humanMovedPiece = sourceTile.getPiece();//get piece on tile
						if (humanMovedPiece == null) {//no piece selected
							sourceTile = null;//reset
						}
					}
					else { //piece selected
						destinationTile = checkersBoard.getTile(tileId); //select destination
						final Move move = MoveFactory.createMove(checkersBoard, sourceTile.getTileCoordinate(),
								destinationTile.getTileCoordinate());
						final MoveTransition transition = checkersBoard.currentPlayer().makeMove(move);
						if (transition.getMoveStatus().isDone()) {
							checkersBoard = transition.getTransitionBoard();
							moveLog.addMove(move);
						}
						sourceTile = null;
						destinationTile = null;
						humanMovedPiece = null;
					}
					
				}
				Platform.runLater(() -> {
					takenPiecesPanel.redo(moveLog);
					if (gameSetup.isAIPlayer(checkersBoard.currentPlayer())) {
						Table.get().moveMadeUpdate(PlayerType.HUMAN);
					}
					boardPanel.drawBoard(checkersBoard);
				});
				
			});
		}
		
		private void assignTilePieceIcon(final Board board) {
			
			this.getChildren().removeAll();
			if (board.getTile(this.tileId).isTileOccupied()) {
				final Image image = new Image("/Graphics/" +
						board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1) +
						board.getTile(this.tileId).getPiece().toString() + ".png", 50, 50, true, true);
				final ImageView imageView = new ImageView(image);
				setAlignment(Pos.CENTER);
				this.getChildren().add(imageView);
			}
			
		}
		
		private void assignTileColor() {
			
			/*Background light = new Background(new BackgroundFill(Color.color(1, 1, 1),
					CornerRadii.EMPTY, Insets.EMPTY));
			Background dark = new Background(new BackgroundFill(Color.color(0, 0, 0),
					CornerRadii.EMPTY, Insets.EMPTY));*/
			Background light = new Background(new BackgroundImage(new Image("/Graphics/White.jpg"),
					BackgroundRepeat
							.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
			Background dark = new Background(new BackgroundImage(new Image("/Graphics/Black.jpg"), BackgroundRepeat
					.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
			if (EIGHTH_RANK[this.tileId] ||
					SIXTH_RANK[this.tileId] ||
					FOURTH_RANK[this.tileId] ||
					SECOND_RANK[this.tileId]) {
				setBackground(this.tileId % 2 == 0 ? light : dark);
				
			}
			else if (SEVENTH_RANK[this.tileId] ||
					FIFTH_RANK[this.tileId] ||
					THIRD_RANK[this.tileId] ||
					FIRST_RANK[this.tileId]) {
				setBackground(this.tileId % 2 != 0 ? light : dark);
				
			}
		}
		
		public void drawTile(final Board board) {
			
			assignTileColor();
			assignTilePieceIcon(board);
			highlightLegals(board);
		}
		
		private void highlightLegals(final Board board) {
			
			if (highlightLegalMoves) {
				for (final Move move : pieceLegalMoves(board)) {
					if (move.getDestinationCoordinate() == this.tileId) {
						if (move.isAttack()) {
							try {
								getChildren().add(new StackPane(this.getChildren().get(0), new ImageView
										(new Image
												("/Graphics/red_dot.png",
														10, 10,
														true,
														true))));
								setAlignment(Pos.CENTER);
							} catch (Exception e) {
								e.printStackTrace();
								
							}
						}
						else {
							try {
								getChildren().add(new StackPane(new ImageView(new Image
										("/Graphics/green_dot.png",
												10, 10,
												true,
												true))));
								setAlignment(Pos.CENTER);
							} catch (Exception e) {
								e.printStackTrace();
								
							}
						}
					}
				}
			}
		}
		
		private Collection<Move> pieceLegalMoves(final Board board) {
			
			if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
				return humanMovedPiece.calculateLegalMoves(board);
			}
			return Collections.emptyList();
		}
	}
	
	public static class MoveLog {
		
		private final List<Move> moves;
		
		MoveLog() {
			
			this.moves = new ArrayList<>();
		}
		
		public List<Move> getMoves() {
			
			return moves;
		}
		
		public void addMove(final Move move) {
			
			this.moves.add(move);
		}
		
		public int size() {
			
			return this.moves.size();
		}
		
		public void clear() {
			
			this.moves.clear();
		}
		
		public Move removeMove(int index) {
			
			return this.moves.remove(index);
		}
		
		public boolean removeMove(final Move move) {
			
			return this.moves.remove(move);
		}
		
	}
}
	

