package com.github.slfotg.chess.move;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.slfotg.chess.Board;
import com.github.slfotg.chess.enums.CastlingRights;
import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;

public interface ChessMove {

    Piece getPiece();

    Position[] getPath();

    default Position getStartingPosition() {
        return getPath()[0];
    }

    default Position getFinalPosition() {
        Position[] path = getPath();
        return path[path.length - 1];
    }

    /**
     * Get the board result of applying this move
     * 
     * @param currentBoard
     * @return the Board after this move is played
     */
    default Board applyMove(Board currentBoard) {
        Piece piece = getPiece();
        // @formatter:off
        Map<Position, Piece> currentPieces =
                currentBoard.getCurrentPieces().isEmpty()
                ? new EnumMap<>(Position.class)
                : new EnumMap<>(currentBoard.getCurrentPieces());
        Map<Position, Piece> opponentPieces =
                currentBoard.getOpponentPieces().isEmpty()
                ? new EnumMap<>(Position.class)
                : new EnumMap<>(currentBoard.getOpponentPieces());
        // @formatter:on
        currentPieces.remove(getStartingPosition());
        getAttackedPosition().ifPresent(opponentPieces::remove);
        currentPieces.put(getFinalPosition(), piece);
        return new Board(currentPieces, opponentPieces, currentBoard.getKingPosition(),
                currentBoard.getOpponentKingPosition());
    }

    /**
     * Get the updated castling rights after this move
     * 
     * @param currentPlayerRights
     * @return
     */
    default CastlingRights updateCastlingRights(CastlingRights currentPlayerRights) {
        return currentPlayerRights;
    }

    default boolean hasCastlingRights(CastlingRights currentPlayerRights) {
        return true;
    }

    /**
     * Returns true if this move can be used as a capture. Does not necessarily mean
     * it is a capture. (Pawn advance and castling will return false)
     * 
     * @return true if the move can be used to capture a piece or false if it's a
     *         pawn advance or castle
     */
    default boolean isAttackingMove() {
        return true;
    }

    /**
     * Returns true if a pawn is moved
     * 
     * @return
     */
    default boolean isPawnMove() {
        return false;
    }

    /**
     * Returns true if an opponent piece is captured
     * 
     * @param currentBoard
     * @return
     */
    default boolean isCapture(Board currentBoard) {
        var attackedPosition = getAttackedPosition();
        var opponentPieces = currentBoard.getOpponentPieces();
        return attackedPosition.map(opponentPieces::containsKey).orElse(false);
    }

    /**
     * Returns an optional list of positions that cannot be under attack in order to
     * apply this move (for castling)
     * 
     * @return
     */
    default Optional<List<Position>> getCastlingPositions() {
        return Optional.empty();
    }

    default Optional<Position> enPassantPosition() {
        return Optional.empty();
    }

    default Optional<Position> getAttackedPosition() {
        if (isAttackingMove()) {
            return Optional.of(getFinalPosition());
        }
        return Optional.empty();
    }

    default String stringValue() {
        return String.format("%s: %s -> %s", getPiece(), getStartingPosition(), getFinalPosition());
    }
}
