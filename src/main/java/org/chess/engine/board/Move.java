package org.chess.engine.board;

import org.chess.engine.pieces.Piece;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    private Move(final Board _board, final Piece _movedPiece, final int _destinationCoordinate) {
        this.board = _board;
        this.movedPiece = _movedPiece;
        this.destinationCoordinate = _destinationCoordinate;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public abstract Board execute();

    public static final class MajorMove extends Move {
        public MajorMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate) {
            super(_board, _movedPiece, _destinationCoordinate);
        }

        @Override
        public Board execute() {
            return null;
        }
    }


    public static final class AttackMove extends Move {
        final Piece attackedPiece;

        public AttackMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate, final Piece _attackedPiece) {
            super(_board, _movedPiece, _destinationCoordinate);
            this.attackedPiece = _attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
        }
    }
}
