package org.chess.engine.pieces;

import org.chess.engine.Alliance;
import org.chess.engine.board.Board;
import org.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {

    protected final int piecePosition;

    public Alliance getPieceAlliance() {
        return pieceAlliance;
    }

    protected final Alliance pieceAlliance;

    Piece(final int piecePosition, final Alliance pieceAlliance){
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

}