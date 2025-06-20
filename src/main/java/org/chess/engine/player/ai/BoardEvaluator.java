package org.chess.engine.player.ai;

import org.chess.engine.board.Board;

public interface BoardEvaluator {
    int evaluate(Board board, int depth);
}
