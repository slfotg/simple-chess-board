package com.github.slfotg.chess.move;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class QueenMove implements ChessMove {

    private final Position[] path;

    @Override
    public Piece getPiece() {
        return Piece.QUEEN;
    }

    @Override
    public Position[] getPath() {
        return path;
    }

    public static Map<Position, Collection<ChessMove>> allQueenMoves(StraightMoveGenerator rowMoveGenerator,
            DiagonalMoveGenerator diagonalMoveGenerator) {
        Map<Position, Collection<ChessMove>> queenMoves = new EnumMap<>(Position.class);
        for (Position position : Position.values()) {
            Collection<ChessMove> moves = rowMoveGenerator.generateMoves(position, QueenMove::new);
            moves.addAll(diagonalMoveGenerator.generateMoves(position, QueenMove::new));
            queenMoves.put(position, moves);
        }
        return queenMoves;
    }

    @Override
    public String toString() {
        return stringValue();
    }

}
