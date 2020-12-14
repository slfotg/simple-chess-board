package com.github.slfotg.chess.evaluate;

import java.util.Map;

import com.github.slfotg.chess.Board;
import com.github.slfotg.chess.GameState;
import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;

public class NaiveEvaluationFunction implements BoardEvaluationFunction {

    @Override
    public double evaluate(GameState gameState) {
        Board board = gameState.getBoard();
        return evaluate(board.getCurrentPieces()) - evaluate(board.getOpponentPieces());
    }

    private double evaluate(Map<Position, Piece> pieces) {
        double value = 0;
        for (Piece piece : pieces.values()) {
            switch (piece) {
            case PAWN:
                value += 1;
                break;
            case KNIGHT:
            case BISHOP:
                value += 3;
                break;
            case ROOK:
                value += 5;
                break;
            case QUEEN:
                value += 9;
                break;
            default:
            }
        }
        return value;
    }

}
