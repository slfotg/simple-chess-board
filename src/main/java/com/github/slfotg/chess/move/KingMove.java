package com.github.slfotg.chess.move;

import java.util.EnumMap;
import java.util.Map;

import com.github.slfotg.chess.Board;
import com.github.slfotg.chess.enums.CastlingRights;
import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class KingMove implements ChessMove {

    private final Position[] path;

    @Override
    public Piece getPiece() {
        return Piece.KING;
    }

    @Override
    public Position[] getPath() {
        return path;
    }

    @Override
    public Board applyMove(Board currentBoard) {
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
        opponentPieces.remove(getFinalPosition());
        return new Board(currentPieces, opponentPieces, getFinalPosition(),
                currentBoard.getOpponentKingPosition());
    }

    @Override
    public CastlingRights updateCastlingRights(CastlingRights currentPlayerRights) {
        return CastlingRights.NONE;
    }

    @Override
    public String toString() {
        return stringValue();
    }

}
