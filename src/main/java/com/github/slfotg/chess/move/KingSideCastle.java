package com.github.slfotg.chess.move;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.slfotg.chess.Board;
import com.github.slfotg.chess.enums.CastlingRights;
import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;
import com.github.slfotg.chess.enums.Side;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class KingSideCastle implements ChessMove {

    @Override
    public Piece getPiece() {
        return Piece.KING;
    }

    @Override
    public Position[] getPath() {
        return new Position[] { Position.E1, Position.F1, Position.G1 };
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
        currentPieces.remove(Position.H1);
        currentPieces.put(Position.F1, Piece.ROOK);
        return new Board(currentPieces, opponentPieces, Position.G1, currentBoard.getOpponentKingPosition());
    }

    @Override
    public CastlingRights updateCastlingRights(CastlingRights currentPlayerRights) {
        return CastlingRights.NONE;
    }

    @Override
    public boolean hasCastlingRights(CastlingRights currentPlayerRights) {
        return currentPlayerRights.hasRights(Side.KING);
    }

    @Override
    public boolean isAttackingMove() {
        return false;
    }

    @Override
    public Optional<List<Position>> getCastlingPositions() {
        return Optional.of(Arrays.asList(getPath()));
    }

    @Override
    public Optional<Position> getAttackedPosition() {
        return Optional.ofNullable(null);
    }

    @Override
    public String toString() {
        return "0-0";
    }

}
