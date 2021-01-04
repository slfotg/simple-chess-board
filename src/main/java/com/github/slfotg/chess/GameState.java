package com.github.slfotg.chess;

import java.util.List;
import java.util.stream.Collectors;

import com.github.slfotg.chess.enums.CastlingRights;
import com.github.slfotg.chess.enums.Color;
import com.github.slfotg.chess.enums.Position;
import com.github.slfotg.chess.move.ChessMove;
import com.github.slfotg.chess.move.ChessMoveGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Builder
@Getter
public class GameState {

    private static final ChessMoveGenerator MOVE_GENERATOR = new ChessMoveGenerator();

    @NonNull
    private final Color activeColor;
    private final Position enPassant;
    private final int halfMoveClock;
    private final int fullMoveNumber;
    @NonNull
    private final Board board;
    @NonNull
    private final CastlingRights currentPlayerRights;
    @NonNull
    private final CastlingRights opponentRights;
    
    public List<ChessMove> getPossibleMoves() {
        return MOVE_GENERATOR.getPossibleMoves(board, currentPlayerRights, enPassant);
    }

    public GameState applyMove(ChessMove chessMove) {
        // TODO 
        return null;
    }
    
    public GameState[] nextPossibleStates() {
        // @formatter:off
        return getPossibleMoves().stream()
                .map(this::applyMove)
                .collect(Collectors.toList())
                .toArray(new GameState[0]);
        // @formatter:on
    }
}
