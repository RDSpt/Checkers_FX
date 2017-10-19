package com.checkers.gui;

import com.checkers.engine.Alliance;
import com.checkers.engine.player.Player;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import static com.checkers.gui.Table.PlayerType;

public class GameSetup extends Scene {
	
	private static PlayerType whitePlayerType;
	private static PlayerType blackPlayerType;
	private Spinner searchDepthSpinner;
	
	private static final String HUMAN_TEXT = "Human";
	private static final String COMPUTER_TEXT = "Computer";
	private static int searchDepthValue;
	
	public GameSetup(final boolean modal) {
		
		super(new Pane());
		VBox frame = new VBox();
		createWindow(frame);
		searchDepthValue = (int) searchDepthSpinner.getValue();
		setRoot(frame);
	}
	
	private void createWindow(Pane frame) {
		
		//white buttons
		final ToggleGroup whiteGroup          = new ToggleGroup();
		final RadioButton whiteHumanButton    = new RadioButton(HUMAN_TEXT);
		final RadioButton whiteComputerButton = new RadioButton(COMPUTER_TEXT);
		whiteGroup.getToggles().add(whiteHumanButton);
		whiteGroup.getToggles().add(whiteComputerButton);
		if(whitePlayerType == PlayerType.HUMAN){
			whiteGroup.selectToggle(whiteHumanButton);
		}else{
			whiteGroup.selectToggle(whiteComputerButton);
		}
		
		//black buttons
		final ToggleGroup blackGroup          = new ToggleGroup();
		final RadioButton blackHumanButton    = new RadioButton(HUMAN_TEXT);
		final RadioButton blackComputerButton = new RadioButton(COMPUTER_TEXT);
		blackGroup.getToggles().add(blackHumanButton);
		blackGroup.getToggles().add(blackComputerButton);
		blackComputerButton.setSelected(true);
		if(blackPlayerType == PlayerType.HUMAN){
			blackGroup.selectToggle(blackHumanButton);
		}else{
			blackGroup.selectToggle(blackComputerButton);
		}
		
		frame.getChildren().add(new Label("Black"));
		frame.getChildren().add(blackHumanButton);
		frame.getChildren().add(blackComputerButton);
		frame.getChildren().add(new Label("White"));
		frame.getChildren().add(whiteHumanButton);
		frame.getChildren().add(whiteComputerButton);
		frame.getChildren().add(new Label("Search level"));
		this.searchDepthSpinner = new Spinner(1, 10, 5, 1);
		frame.getChildren().add(searchDepthSpinner);
		Button cancel = new Button("Cancel");
		Button ok     = new Button("OK");
		frame.getChildren().add(cancel);
		frame.getChildren().add(ok);
		ok.setOnAction(event -> {
			this.searchDepthValue = (int) searchDepthSpinner.getValue();
			Stage stage = (Stage) ok.getScene().getWindow();
			whitePlayerType = whiteComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
			blackPlayerType = blackComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
			stage.close();
		});
		cancel.setOnAction(event -> {
			Stage stage = (Stage) cancel.getScene().getWindow();
			System.out.println("cancel");
			stage.close();
		});
		/*Scene scene = new Scene(frame);
		stage.setScene(scene);*/
	}
	
	boolean isAIPlayer(final Player player) {
		
		if (player.getAlliance() == Alliance.WHITE) {
			return getWhitePlayerType() == PlayerType.COMPUTER;
		}
		return getBlackPlayerType() == PlayerType.COMPUTER;
	}
	
	public PlayerType getWhitePlayerType() {
		
		return whitePlayerType;
	}
	
	public PlayerType getBlackPlayerType() {
		
		return blackPlayerType;
	}
	
	public void setWhitePlayerType(PlayerType whitePlayerType) {
		
		this.whitePlayerType = whitePlayerType;
	}
	
	public void setBlackPlayerType(PlayerType blackPlayerType) {
		
		this.blackPlayerType = blackPlayerType;
	}
	
	public int getSearchDepthValue() {
		
		return this.searchDepthValue;
	}
}
