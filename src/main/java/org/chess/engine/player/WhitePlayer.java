package org.chess.engine.player;

import com.google.common.collect.ImmutableList;
import org.chess.engine.Alliance;
import org.chess.engine.board.Board;
import org.chess.engine.board.Move;
import org.chess.engine.board.Tile;
import org.chess.engine.pieces.Piece;
import org.chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player {

    public WhitePlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(
                board, whiteStandardLegalMoves, blackStandardLegalMoves
        );
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {

        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            if (!this.board.getTile(61).isTileOccupied() &&
                    !this.board.getTile(62).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(63);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()
                    ) {
                        kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing,
                                62, (Rook) rookTile.getPiece(),
                                rookTile.getTileCoordinate(), 61));
                    }
                }
            }
            if (!this.board.getTile(59).isTileOccupied() &&
                    !this.board.getTile(58).isTileOccupied() &&
                    !this.board.getTile(57).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(56);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing,
                            58, (Rook) rookTile.getPiece(),
                            rookTile.getTileCoordinate(), 59));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
