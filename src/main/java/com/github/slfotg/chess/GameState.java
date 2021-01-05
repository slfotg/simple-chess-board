package com.github.slfotg.chess;

import java.util.List;
import java.util.stream.Collectors;

import com.github.slfotg.chess.enums.CastlingRights;
import com.github.slfotg.chess.enums.Color;
import com.github.slfotg.chess.enums.Position;
import com.github.slfotg.chess.move.ChessMove;
import com.github.slfotg.chess.move.ChessMoveGenerator;
import com.github.slfotg.chess.util.PositionInverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public final class GameState {

    private static final PositionInverter POSITION_INVERTER = new PositionInverter();
    private static final ChessMoveGenerator MOVE_GENERATOR = new ChessMoveGenerator(POSITION_INVERTER);

    @NonNull
    private final Color activeColor;
    private final Position enPassant;
    @EqualsAndHashCode.Exclude
    private final int halfMoveClock;
    @EqualsAndHashCode.Exclude
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
        // @formatter:off
        return builder()
            .activeColor(activeColor == Color.BLACK ? Color.WHITE : Color.BLACK)
            .enPassant(chessMove.enPassantPosition().orElse(null))
            .halfMoveClock(chessMove.isPawnMove() || chessMove.isCapture(board) ? 0 : halfMoveClock + 1)
            .fullMoveNumber(activeColor == Color.BLACK ? fullMoveNumber + 1 : fullMoveNumber)
            .board(POSITION_INVERTER.invertBoard(chessMove.applyMove(board)))
            .currentPlayerRights(opponentRights)
            .opponentRights(chessMove.updateCastlingRights(currentPlayerRights))
            .build();
        // @formatter:on
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
