package org.chess.engine.pieces;

import org.chess.engine.Alliance;
import org.chess.engine.board.Board;
import org.chess.engine.board.Move;

import java.util.List;

public abstract class Piece {


    protected final int piecePosition;
    protected final Alliance pieceAlliance;

    Piece(final int piecePosition, final Alliance pieceAlliance){
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
    }

    public abstract List<Move> calculateLegalMoves(final Board board);

}