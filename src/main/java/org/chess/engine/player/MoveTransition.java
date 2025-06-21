package org.chess.engine.player;

import org.chess.engine.board.Board;
import org.chess.engine.board.Move;

@SuppressWarnings("unused")
public class MoveTransition {

    private final Board transitionBoard;
    private final MoveStatus moveStatus;

    public MoveTransition(
            final Board transitionBoard,
            final Move move,
            final MoveStatus moveStatus
    ) {
        this.transitionBoard = transitionBoard;
        this.moveStatus = moveStatus;
    }

    public Board getTransitionBoard(){
        return transitionBoard;
    }

    public MoveStatus getMoveStatus() {
        return moveStatus;
    }
}
