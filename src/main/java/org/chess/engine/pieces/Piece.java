package org.chess.engine.pieces;

import org.chess.engine.Alliance;
import org.chess.engine.board.Board;
import org.chess.engine.board.Move;

import java.util.Collection;

@SuppressWarnings("unused")
public abstract class Piece {

    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;

    Piece(
            final PieceType pieceType,
            final int piecePosition,
            final Alliance pieceAlliance,
            final boolean isFirstMove) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = isFirstMove;
        int cachcedHashcode = computeHashCode();
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    public Alliance getPieceAlliance() {
        return pieceAlliance;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public int getPiecePosition() {
        return piecePosition;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Piece otherPiece))
            return false;
        return piecePosition == otherPiece.getPiecePosition() &&
                pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance() &&
                isFirstMove == otherPiece.isFirstMove();

    }

    public int getPieceValue() {
        return this.pieceType.pieceValue;
    }

    @Override
    public int hashCode() {
        return this.computeHashCode();
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    public enum PieceType {

        PAWN(100,"P") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT(300,"N") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP(300,"B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK(500,"R") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN(900,"Q") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING(10000,"K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private final String pieceName;
        private final int pieceValue;

        PieceType(final int pieceValue, final String pieceName) {
            this.pieceValue = pieceValue;
            this.pieceName = pieceName;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();

        @Override
        public String toString() {
            return this.pieceName;
        }
    }
}