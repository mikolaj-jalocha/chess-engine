package org.chess.engine.player;

import org.chess.engine.board.Board;
import org.chess.engine.board.Move;
import org.chess.engine.pieces.Piece;

import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player {

    public BlackPlayer(Board board,
                       Collection<Move> whiteStandardLegalMoves,
                       Collection<Move> blackStandardLegalMoves) {
        super(
                board,blackStandardLegalMoves,whiteStandardLegalMoves
        );
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }
}
