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

            final Board.Builder builder = new Board.Builder();

            this.board.getCurrentPlayer().getActivePieces().stream()
                    .filter(piece -> !movedPiece.equals(piece))
                    .forEach(builder::setPiece);

            this.board.getCurrentPlayer().getOpponent().getActivePieces().forEach(builder::setPiece);

            // move the moved piece
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());

            return builder.build();
        }
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
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
