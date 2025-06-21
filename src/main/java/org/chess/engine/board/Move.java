package org.chess.engine.board;

import org.chess.engine.pieces.Pawn;
import org.chess.engine.pieces.Piece;
import org.chess.engine.pieces.Rook;

@SuppressWarnings({"StaticInitializerReferencesSubClass", "unused"})
public abstract class Move {

    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board _board, final Piece _movedPiece, final int _destinationCoordinate) {
        this.board = _board;
        this.movedPiece = _movedPiece;
        this.destinationCoordinate = _destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(final Board board,
                 final int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    public int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public Board getBoard() {
        return this.board;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Move otherMove))
            return false;

        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
                this.getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
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

    public static class MajorAttackMove extends AttackMove {
        public MajorAttackMove(final Board board,
                               final Piece pieceMoved,
                               final int destinationCoordinate,
                               final Piece pieceAttacked
        ) {
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof MajorAttackMove && super.equals(o);
        }
    }

    public static final class MajorMove extends Move {
        public MajorMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate) {
            super(_board, _movedPiece, _destinationCoordinate);
        }

        @Override
        public boolean equals(final Object o) {
            return this == o || o instanceof MajorMove && super.equals(o);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
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
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }
    }

    public static final class PawnMove extends Move {


        public PawnMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate) {
            super(_board, _movedPiece, _destinationCoordinate);
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof PawnMove && super.equals(o);
        }
    }


    public static class PawnAttackMove extends AttackMove {
        final Piece attackedPiece;

        public PawnAttackMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate, final Piece _attackedPiece) {
            super(_board, _movedPiece, _destinationCoordinate, _attackedPiece);
            this.attackedPiece = _attackedPiece;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof PawnAttackMove && super.equals(o);
        }

    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove {
        public PawnEnPassantAttackMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate, final Piece _attackedPiece) {
            super(_board, _movedPiece, _destinationCoordinate, _attackedPiece);
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof PawnEnPassantAttackMove && super.equals(o);
        }

        @Override
        public Board execute() {
            final Board.Builder builder = new Board.Builder();
            this.board.getCurrentPlayer().getActivePieces().stream()
                    .filter(piece -> !this.movedPiece.equals(piece))
                    .forEach(builder::setPiece);
            this.board.getCurrentPlayer().getOpponent().getActivePieces().stream()
                    .filter(piece -> !piece.equals(this.getAttackedPiece()))
                    .forEach(builder::setPiece);
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static class PawnPromotion extends Move {

        final Move decoratedMove;
        final Pawn promotedPawn;

        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
        }

        @Override
        public Board execute() {

            final Board pawnMoveBoard = this.decoratedMove.execute();
            final Board.Builder builder = new Board.Builder();

            pawnMoveBoard.getCurrentPlayer().getActivePieces().stream()
                    .filter(piece -> !this.promotedPawn.equals(piece))
                    .forEach(builder::setPiece);

            pawnMoveBoard.getCurrentPlayer().getOpponent().getActivePieces()
                    .forEach(builder::setPiece);

            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setMoveMaker(pawnMoveBoard.getCurrentPlayer().getAlliance());
            return builder.build();
        }

        @Override
        public boolean isAttack() {
            return this.decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackedPiece() {
            return this.decoratedMove.getAttackedPiece();
        }

        @Override
        public int hashCode() {
            return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof PawnPromotion && super.equals(o);
        }
    }

    public static final class PawnJump extends Move {

        public PawnJump(final Board _board, final Piece _movedPiece, final int _destinationCoordinate) {
            super(_board, _movedPiece, _destinationCoordinate);
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

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            final Board.Builder builder = new Board.Builder();
            this.board.getCurrentPlayer().getActivePieces().stream()
                    .filter(
                            piece -> !this.movedPiece.equals(piece) && !this.castleRook.equals(piece)
                    ).forEach(builder::setPiece);
            this.board.getCurrentPlayer().getOpponent().getActivePieces().forEach(builder::setPiece);

            //move king
            builder.setPiece(this.movedPiece.movePiece(this));
            //move castle
            builder.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDestination));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof CastleMove otherCastleMove))
                return false;
            return super.equals(otherCastleMove) &&
                    this.castleRook.equals(otherCastleMove.getCastleRook());
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate,
                          final Rook castleRook,
                          final int castleRookStart,
                          final int castleRookDestination
        ) {
            super(_board, _movedPiece, _destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return castleRook;
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate, final Rook castleRook,
                                  final int castleRookStart,
                                  final int castleRookDestination) {
            super(_board, _movedPiece, _destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof KingSideCastleMove && super.equals(o);
        }

        @Override
        public String toString() {
            return "O-O";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board _board, final Piece _movedPiece, final int _destinationCoordinate, final Rook castleRook,
                                   final int castleRookStart,
                                   final int castleRookDestination) {
            super(_board, _movedPiece, _destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof QueenSideCastleMove && super.equals(o);
        }

        @Override
        public String toString() {
            return "O-O-O";
        }
    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("cannot execute the null move!");
        }

        @Override
        public int getCurrentCoordinate() {
            return -1;
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
