package org.chess.engine.player;

import org.chess.engine.Alliance;
import org.chess.engine.board.Board;
import org.chess.engine.board.Move;
import org.chess.engine.pieces.Piece;

import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player {

    public BlackPlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(
                board,blackStandardLegalMoves,whiteStandardLegalMoves
        );
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }
}
