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

class QueenSideCastle implements ChessMove {

    @Override
    public Piece getPiece() {
        return Piece.KING;
    }

    @Override
    public Position[] getPath() {
        return new Position[] { Position.E1, Position.D1, Position.C1, Position.B1 };
    }

    @Override
    public Board applyMove(Board currentBoard) {
        Map<Position, Piece> currentPieces = new EnumMap<>(currentBoard.getCurrentPieces());
        Map<Position, Piece> opponentPieces = new EnumMap<>(currentBoard.getOpponentPieces());
        currentPieces.remove(Position.A1);
        currentPieces.put(Position.D1, Piece.ROOK);
        return new Board(currentPieces, opponentPieces, Position.C1,
                currentBoard.getOpponentKingPosition());
    }

    @Override
    public CastlingRights updateCastlingRights(CastlingRights currentPlayerRights) {
        return CastlingRights.NONE;
    }

    @Override
    public boolean hasCastlingRights(CastlingRights currentPlayerRights) {
        return currentPlayerRights.hasRights(Side.QUEEN);
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
        return "O-O-O";
    }

}
