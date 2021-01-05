package com.github.slfotg.chess;

import java.util.LinkedList;
import java.util.List;

import com.github.slfotg.chess.enums.CastlingRights;
import com.github.slfotg.chess.enums.Color;

public class ChessGame {

    private GameState gameState;
    private List<GameState> previousStates;

    private ChessGame() {
        previousStates = new LinkedList<>();
        // @formatter:off
        gameState = GameState.builder()
                .activeColor(Color.WHITE)
                .board(Board.initialBoard())
                .halfMoveClock(0)
                .fullMoveNumber(1)
                .currentPlayerRights(CastlingRights.EITHER_SIDE)
                .opponentRights(CastlingRights.EITHER_SIDE)
                .build();
        // @fomatter:on
    }

    public ChessGame(GameState gameState, List<GameState> previousStates) {
        this.gameState = gameState;
        this.previousStates = new LinkedList<>(previousStates);
    }
    
    public static ChessGame newGame() {
        return new ChessGame();
    }
}
