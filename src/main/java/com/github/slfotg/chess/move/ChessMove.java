package com.github.slfotg.chess.move;

import java.util.Collection;
import java.util.Map;

import com.github.slfotg.chess.Board;
import com.github.slfotg.chess.GameState;
import com.github.slfotg.chess.enums.CastlingRights;
import com.github.slfotg.chess.enums.Color;
import com.github.slfotg.chess.enums.Position;

public interface ChessMove {

    /**
     * Apply this move to the currentState and returns the resulting GameState
     * 
     * @param currentState
     * @return The resulting GameState after this move is applied to currentState
     * @throws InvalidMove
     */
    default GameState applyMove(GameState currentState) throws InvalidMove {
        if (!isValid(currentState, null)) {
            throw new InvalidMove();
        }
        GameState.GameStateBuilder builder = GameState.builder();
        if (currentState.getActiveColor() == Color.WHITE) {
            builder.activeColor(Color.BLACK);
            builder.fullMoveNumber(currentState.getFullMoveNumber());
            builder.whiteRights(updateCastlingRights(currentState.getWhiteRights()));
            builder.blackRights(currentState.getBlackRights());
        } else {
            builder.activeColor(Color.WHITE);
            builder.fullMoveNumber(currentState.getFullMoveNumber() + 1);
            builder.whiteRights(currentState.getWhiteRights());
            builder.blackRights(updateCastlingRights(currentState.getBlackRights()));
        }
        Board currentBoard = currentState.getBoard();
        builder.halfMoveClock(isCapture(currentBoard) || isPawnMove() ? 0 : currentState.getHalfMoveClock() + 1);
        builder.board(updateBoard(currentBoard).mirror());
        return builder.build();
    }

    /**
     * Get the board result of applying this move
     * 
     * @param currentBoard
     * @return the Board after this move is played
     */
    Board updateBoard(Board currentBoard);

    /**
     * Get the updated castling rights after this move
     * 
     * @param currentPlayerRights
     * @return
     */
    CastlingRights updateCastlingRights(CastlingRights currentPlayerRights);

    /**
     * Returns true if this move can be used as a capture. Does not necessarily mean
     * it is a capture. (Pawn advance and castling will return false)
     * 
     * @return true if the move can be used to capture a piece or false if it's a
     *         pawn advance or castle
     */
    boolean isAttackingMove();

    /**
     * Returns true if this move is technically valid (no moving through pieces, no
     * castling through checks). It does not check if the resulting board would end
     * in a check against the current player which is allowed in certain types of
     * games, but those games would also result in an immediate loss by capturing
     * the king.
     * 
     * @param currentState
     * @param opponentAttacks
     * @return
     */
    boolean isValid(GameState currentState, Map<Position, Collection<ChessMove>> opponentAttacks);

    /**
     * Same as isValid except this also checks if the move would result in a check
     * against the current player.
     * 
     * @param currentState
     * @param opponentAttacks
     * @return
     */
    boolean isLegal(GameState currentState, Map<Position, Collection<ChessMove>> opponentAttacks);

    /**
     * Returns true if a pawn is moved
     * @return
     */
    default boolean isPawnMove() {
        return false;
    }

    /**
     * Returns true if an opponent piece is captured
     * @param currentBoard
     * @return
     */
    boolean isCapture(Board currentBoard);
}
