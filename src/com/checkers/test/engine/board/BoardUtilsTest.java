package com.checkers.test.engine.board;

import com.checkers.engine.board.BoardUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardUtilsTest {
	
	@Test
	public void FirstColumn() {
		
		assertTrue(BoardUtils.FIRST_COLUMN[0]);
		assertTrue(BoardUtils.FIRST_COLUMN[8]);
		assertTrue(BoardUtils.FIRST_COLUMN[16]);
		assertTrue(BoardUtils.FIRST_COLUMN[24]);
		assertTrue(BoardUtils.FIRST_COLUMN[32]);
		assertTrue(BoardUtils.FIRST_COLUMN[40]);
		assertTrue(BoardUtils.FIRST_COLUMN[48]);
		assertTrue(BoardUtils.FIRST_COLUMN[56]);
		assertFalse(BoardUtils.FIRST_COLUMN[1]);
		assertFalse(BoardUtils.FIRST_COLUMN[2]);
		assertFalse(BoardUtils.FIRST_COLUMN[9]);
		
		assertFalse(BoardUtils.FIRST_COLUMN[10]);
		assertFalse(BoardUtils.FIRST_COLUMN[17]);
		assertFalse(BoardUtils.FIRST_COLUMN[18]);
	}
	
	@Test
	public void EightColumn() {
		
		assertTrue(BoardUtils.EIGHTH_COLUMN[7]);
		assertTrue(BoardUtils.EIGHTH_COLUMN[15]);
		assertTrue(BoardUtils.EIGHTH_COLUMN[23]);
		assertTrue(BoardUtils.EIGHTH_COLUMN[31]);
		assertTrue(BoardUtils.EIGHTH_COLUMN[39]);
		assertTrue(BoardUtils.EIGHTH_COLUMN[47]);
		assertTrue(BoardUtils.EIGHTH_COLUMN[55]);
		assertTrue(BoardUtils.EIGHTH_COLUMN[63]);
	}
	
}