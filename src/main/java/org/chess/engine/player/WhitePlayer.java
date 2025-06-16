package org.chess.engine.player;

import org.chess.engine.Alliance;
import org.chess.engine.board.Board;
import org.chess.engine.board.Move;
import org.chess.engine.pieces.Piece;

import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player {

    public WhitePlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(
                board, whiteStandardLegalMoves,blackStandardLegalMoves
        );
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }
}
