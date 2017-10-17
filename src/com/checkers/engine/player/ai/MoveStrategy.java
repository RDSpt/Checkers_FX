package com.checkers.engine.player.ai;

import com.checkers.engine.board.*;

public interface MoveStrategy {
	
	Move execute(Board board, int depth);
	
}
