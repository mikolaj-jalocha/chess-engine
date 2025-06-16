package org.chess.engine.board;

import org.chess.engine.pieces.Pawn;
import org.chess.engine.pieces.Piece;

import java.util.Objects;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board _board, final Piece _movedPiece, final int _destinationCoordinate) {
        this.board = _board;
        this.movedPiece = _movedPiece;
        this.destinationCoordinate = _destinationCoordinate;
    }

    public int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Move otherMove))
            return false;
        return this.getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        return result;
    }

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

    public static final class MajorMove extends Move {
        public MajorMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate) {
            super(_board, _movedPiece, _destinationCoordinate);
        }
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public static class AttackMove extends Move {
        final Piece attackedPiece;

        public AttackMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate, final Piece _attackedPiece) {
            super(_board, _movedPiece, _destinationCoordinate);
            this.attackedPiece = _attackedPiece;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o)
                return true;
            if (!(o instanceof AttackMove otherAttackMove))
                return false;
            return super.equals(otherAttackMove)
                    && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public Board execute() {
            return null;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }
    }

    public static final class PawnMove extends Move {
        final Piece attackedPiece;

        public PawnMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate, final Piece _attackedPiece) {
            super(_board, _movedPiece, _destinationCoordinate);
            this.attackedPiece = _attackedPiece;
        }
    }

    public static class PawnAttackMove extends AttackMove {
        final Piece attackedPiece;

        public PawnAttackMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate, final Piece _attackedPiece) {
            super(_board, _movedPiece, _destinationCoordinate, _attackedPiece);
            this.attackedPiece = _attackedPiece;
        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove {
        final Piece attackedPiece;

        public PawnEnPassantAttackMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate, final Piece _attackedPiece) {
            super(_board, _movedPiece, _destinationCoordinate, _attackedPiece);
            this.attackedPiece = _attackedPiece;
        }
    }

    public static final class PawnJump extends Move {
        final Piece attackedPiece;

        public PawnJump(final Board _board, final Piece _movedPiece, final int _destinationCoordinate, final Piece _attackedPiece) {
            super(_board, _movedPiece, _destinationCoordinate);
            this.attackedPiece = _attackedPiece;
        }

        @Override
        public Board execute() {
            final Board.Builder builder = new Board.Builder();

            this.board.getCurrentPlayer().getActivePieces().stream()
                    .filter(
                    piece -> !this.movedPiece.equals(piece)
            ).forEach(builder::setPiece);
            this.board.getCurrentPlayer().getOpponent().getActivePieces().forEach(builder::setPiece);

            final Pawn movePawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movePawn);
            builder.setEnPassantPawn(movePawn);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static abstract class CastleMove extends Move {
        public CastleMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate) {
            super(_board, _movedPiece, _destinationCoordinate);
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate) {
            super(_board, _movedPiece, _destinationCoordinate);

        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate) {
            super(_board, _movedPiece, _destinationCoordinate);
        }
    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("cannot execute the null move!");
        }
    }

    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("Not instantiable");
        }

        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate) {

            for (final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }

    }

}
