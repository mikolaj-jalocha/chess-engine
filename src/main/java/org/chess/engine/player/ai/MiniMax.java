package org.chess.engine.player.ai;

import org.chess.engine.board.Board;
import org.chess.engine.board.Move;
import org.chess.engine.player.MoveTransition;

public class MiniMax implements MoveStrategy {

    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;

    public MiniMax(final int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }

    // return the best move
    @Override
    public Move execute(Board board) {

        final long startTime = System.currentTimeMillis();

        Move bestMove = null;

        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.getCurrentPlayer() + "THINKING with depth =" + searchDepth);

        int numMoves = board.getCurrentPlayer().getLegalMoves().size();

        for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {

                currentValue = board.getCurrentPlayer().getAlliance().isWhite() ?
                        min(moveTransition.getTransitionBoard(), searchDepth - 1) :
                        max(moveTransition.getTransitionBoard(), searchDepth - 1);

                if (board.getCurrentPlayer().getAlliance().isWhite() &&
                        currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.getCurrentPlayer().getAlliance().isBlack() &&
                        currentValue <= lowestSeenValue
                ) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        final long executionTime = System.currentTimeMillis() - startTime;

        return bestMove;
    }

    private static boolean isEndGameScenario(final Board board) {
        return board.getCurrentPlayer().isInCheckMate() ||
                board.getCurrentPlayer().isInStaleMate();
    }

    public int min(final Board board, final int depth) {
        if (depth == 0 || isEndGameScenario(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }

        // return the lowest value
        return board.getCurrentPlayer().getLegalMoves().stream()
                .map(move -> board.getCurrentPlayer().makeMove(move))
                .filter(moveTransition -> moveTransition.getMoveStatus().isDone())
                .mapToInt(moveTransition -> max(moveTransition.getTransitionBoard(), depth - 1))
                .min()
                .orElse(Integer.MAX_VALUE);
    }

    public int max(final Board board, final int depth) {
        if (depth == 0 || isEndGameScenario(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }

        // return the highest value
        return board.getCurrentPlayer().getLegalMoves().stream()
                .map(move -> board.getCurrentPlayer().makeMove(move))
                .filter(moveTransition -> moveTransition.getMoveStatus().isDone())
                .mapToInt(moveTransition -> min(moveTransition.getTransitionBoard(), depth - 1))
                .max()
                .orElse(Integer.MIN_VALUE);
    }
}
