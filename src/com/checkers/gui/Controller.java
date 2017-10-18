package com.checkers.gui;

import com.checkers.engine.board.*;
import com.checkers.engine.piece.Piece;
import com.checkers.engine.player.MoveTransition;
import com.google.common.collect.Lists;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import javafx.application.Platform;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.embed.swing.SwingNode;
import javafx.stage.Stage;

import javax.sound.midi.ControllerEventListener;
import java.io.IOException;
import java.util.*;

import static com.checkers.engine.board.BoardUtils.*;
import static com.checkers.engine.board.Move.MoveFactory;

public class Controller {
	
	@FXML
	private MenuBar MenuBar;
	@FXML
	private Pane MainPane;
	
	public static Pane MainPANE;
	public static MenuBar MenuBAR;
	
	public void initialize() {
		MainPANE = MainPane;
		MenuBAR = MenuBar;
		Table.get().show();
	}
	
}
