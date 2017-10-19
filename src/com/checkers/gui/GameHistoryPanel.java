package com.checkers.gui;

import com.checkers.engine.board.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

import static com.checkers.gui.Table.MoveLog;

public class GameHistoryPanel extends JPanel {
	
	private final DataModel model;
	private final JScrollPane scrollPane;
	
	GameHistoryPanel() {
		
		this.model = new DataModel();
		final JTable table = new JTable(model);
		table.setRowHeight(15);
		this.scrollPane = new JScrollPane();
		scrollPane.setColumnHeaderView(table);
		//scrollPane.setPreferredSize();
		this.add(scrollPane, BorderLayout.CENTER);
		this.setVisible(true);
		this.setPreferredSize(new Dimension(50,100));
	}
	
	void redo(final Board board,
	          final MoveLog moveHistory) {
		
		
		int currentRow=0;
		this.model.clear();
		for(final Move move: moveHistory.getMoves()){
			final String moveText = move.toString();
			if(move.getMovedPiece().getPieceAlliance().isWhite()){
				this.model.setValueAt(moveText, currentRow,0);
			}else if(move.getMovedPiece().getPieceAlliance().isBlack()){
				this.model.setValueAt(moveText,currentRow,1);
				currentRow++;
			}
		}
		
		if(moveHistory.getMoves().size() > 0 ){
			final Move lastMove = moveHistory.getMoves().get(moveHistory.size()-1);
			final String moveText = lastMove.toString();
			if(lastMove.getMovedPiece().getPieceAlliance().isWhite()){
				this.model.setValueAt(moveText, currentRow -1 ,1 );
			}
		}
		final JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
		
	}
	
	private static class DataModel extends DefaultTableModel {
		
		private static final String[] NAMES = {"White", "Black"};
		private final List<GameHistoryPanel.Row> values;
		
		DataModel() {
			
			this.values = new ArrayList<>();
		}
		
		public void clear() {
			
			this.values.clear();
			setRowCount(0);
		}
		
		@Override
		public int getRowCount() {
			
			if (this.values == null) {
				return 0;
			}
			return this.values.size();
		}
		
		@Override
		public int getColumnCount() {
			
			return NAMES.length;
		}
		
		@Override
		public Object getValueAt(final int row, final int column) {
			
			final Row currentRow = this.values.get(row);
			if (column == 0) {
				return currentRow.getWhiteMove();
			}
			else if (column == 1) {
				return currentRow.getBlackMove();
			}
			return null;
		}
		
		@Override
		public void setValueAt(final Object aValue,
		                        final int row,
		                        final int column) {
			
			final Row currentRow;
			if (this.values.size() <= row){
				currentRow = new Row();
				this.values.add(currentRow);
				
			}else{
				currentRow = this.values.get(row);
			}
			if(column == 0){
				currentRow.setWhiteMove((String) aValue);
				fireTableRowsInserted(row,row);
			}else if (column == 1){
				currentRow.setBlackMove((String) aValue);
				fireTableCellUpdated(row,column);
			}
		}
		
		@Override
		public Class<?> getColumnClass(final int column){
			return Move.class;
		}
		
		@Override
		public String getColumnName(final int column){
			return NAMES[column];
		}
	}
	
	private static class Row {
	
		private String whiteMove;
		private String blackMove;
		
		Row(){
		
		}
		
		public String getWhiteMove(){
			return this.whiteMove;
		}
		
		public String getBlackMove(){
			return this.blackMove;
		}
		
		public void setWhiteMove(final String move){
			this.whiteMove = move;
		}
		
		public void setBlackMove(final String move){
			this.blackMove = move;
		}
		
	}
	
}
