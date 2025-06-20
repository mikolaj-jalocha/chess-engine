package org.chess.engine.board;

import static org.junit.jupiter.api.Assertions.*;

import org.chess.engine.player.MoveTransition;
import org.chess.engine.player.ai.MiniMax;
import org.chess.engine.player.ai.MoveStrategy;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void initialBoard() {
        final Board board = Board.createStandardBoard();
        assertEquals(20, board.getCurrentPlayer().getLegalMoves().size());
        assertEquals(20, board.getCurrentPlayer().getOpponent().getLegalMoves().size());
        Assert.assertFalse(board.getCurrentPlayer().isInCheck());
        Assert.assertFalse(board.getCurrentPlayer().isInCheckMate());
        Assert.assertFalse(board.getCurrentPlayer().isCastled());
        //assertTrue(board.getCurrentPlayer().isKingSideCastleCapable());
        //assertTrue(board.currentPlayer().isQueenSideCastleCapable());
        Assert.assertEquals(board.getCurrentPlayer(), board.whitePlayer());
        Assert.assertEquals(board.getCurrentPlayer().getOpponent(), board.blackPlayer());
        Assert. assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        Assert.assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        Assert. assertFalse(board.getCurrentPlayer().getOpponent().isCastled());
        // assertTrue(board.getCurrentPlayer().getOpponent().isKingSideCastleCapable());
        // assertTrue(board.getCurrentPlayer().getOpponent().isQueenSideCastleCapable());
    }

    @Test
    public void testFoolsMate() {

        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f2"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        //Assert.assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getTransitionBoard().getCurrentPlayer().makeMove(Move.MoveFactory.createMove(t1.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                BoardUtils.getCoordinateAtPosition("e5")));

        Assert. assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getTransitionBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t2.getTransitionBoard(), (BoardUtils.getCoordinateAtPosition("g2")),
                        BoardUtils.getCoordinateAtPosition("g3")));
        Assert.assertTrue(t3.getMoveStatus().isDone());

        final MoveStrategy strategy = new MiniMax(4);
        final Move aiMove = strategy.execute(t3.getTransitionBoard());
        System.out.println(aiMove.toString());
        final Move bestMove = Move.MoveFactory.createMove(t3.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d8"),
             BoardUtils.getCoordinateAtPosition("h4"));

        Assert.assertEquals(aiMove, bestMove);
    }

}