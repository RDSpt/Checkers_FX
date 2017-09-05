package com.checkers.gui;

import com.checkers.engine.board.Board;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static void main(String[] args) {
		
		Board board = Board.createStandardBoard();
		System.out.println(board);
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
		primaryStage.setTitle("Hello World");
		primaryStage.setScene(new Scene(root, 300, 275));
		primaryStage.show();
	}
}
