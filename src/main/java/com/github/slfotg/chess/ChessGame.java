package com.github.slfotg.chess;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.slfotg.chess.enums.CastlingRights;
import com.github.slfotg.chess.enums.Color;
import com.github.slfotg.chess.enums.Result;

public class ChessGame {

    private GameState gameState;
    private List<GameState> previousStates;
    private Map<GameState, Integer> previousStateCounts;

    private ChessGame() {
        // @formatter:off
        gameState = GameState.builder()
                .activeColor(Color.WHITE)
                .board(Board.initialBoard())
                .halfMoveClock(0)
                .fullMoveNumber(1)
                .currentPlayerRights(CastlingRights.EITHER_SIDE)
                .opponentRights(CastlingRights.EITHER_SIDE)
                .build();
        // @formatter:on
        previousStates = new LinkedList<>();
        previousStates.add(gameState);
        initPreviousStateCounts();
    }

    public ChessGame(GameState gameState, List<GameState> previousStates) {
        this.gameState = gameState;
        this.previousStates = new LinkedList<>(previousStates);
        this.previousStates.add(gameState);
        initPreviousStateCounts();
    }

    private void initPreviousStateCounts() {
        previousStateCounts = new HashMap<>();
        for (GameState state : previousStates) {
            previousStateCounts.merge(state, 1, (a, b) -> a + b);
        }
    }

    public static ChessGame newGame() {
        return new ChessGame();
    }

    public Board getBoard() {
        return gameState.getBoard();
    }

    public Optional<Result> getResult() {
        return Optional.ofNullable(gameState.getResult().orElseGet(this::threefoldResult));
    }

    private Result threefoldResult() {
        if (gameState.isInCheck()) {
            return null;
        }
        for (int count : previousStateCounts.values()) {
            if (count >= 3) {
                return Result.THREE_FOLD_DRAW;
            }
        }
        return null;
    }

    public List<ChessGame> nextPossibleChessGames() {
        // @formatter:off
        return gameState.nextPossibleStates()
                .stream()
                .map(s -> new ChessGame(s, previousStates))
                .collect(Collectors.toList());
        // @fomatter:on
    }
}
