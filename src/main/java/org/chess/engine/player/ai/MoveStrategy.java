package org.chess.engine.player.ai;

import org.chess.engine.board.Board;
import org.chess.engine.board.Move;

public interface MoveStrategy {
    Move execute(Board board);
}
