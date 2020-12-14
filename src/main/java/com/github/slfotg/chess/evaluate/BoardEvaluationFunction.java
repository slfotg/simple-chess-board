package com.github.slfotg.chess.evaluate;

import com.github.slfotg.chess.GameState;

public interface BoardEvaluationFunction {

    double evaluate(GameState gameState);
}
