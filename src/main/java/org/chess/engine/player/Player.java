package org.chess.engine.player;

import org.chess.engine.board.Board;
import org.chess.engine.board.Move;
import org.chess.engine.pieces.King;
import org.chess.engine.pieces.Piece;

import java.util.Collection;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;

    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;
    }

    private King establishKing() {
        return (King) getActivePieces().stream()
                .filter(piece -> piece.getPieceType().isKing())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No king found among active pieces."));
    }

    public abstract Collection<Piece> getActivePieces();
}
