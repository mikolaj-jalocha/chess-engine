package org.chess.engine.pieces;

import com.google.common.collect.ImmutableList;
import org.chess.engine.Alliance;
import org.chess.engine.board.Board;
import org.chess.engine.board.BoardUtils;
import org.chess.engine.board.Move;
import org.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class King extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.KING, piecePosition, pieceAlliance, true);
    }

    public King(final Alliance pieceAlliance,
                final int piecePosition,
                final boolean isFirstMove) {
        super(PieceType.KING, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public King movePiece(Move move) {
        return new King(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();


        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) || isEightColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }

            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -9) || (candidateOffset == -1) || (candidateOffset == 7));
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && ((candidateOffset == -17) || (candidateOffset == 1) || (candidateOffset == 9));
    }
}
