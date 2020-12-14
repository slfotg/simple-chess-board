package com.github.slfotg.chess.move.map;

import static com.github.slfotg.chess.enums.Piece.BISHOP;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import com.github.slfotg.chess.enums.Position;
import com.github.slfotg.chess.move.ChessMove;
import com.github.slfotg.chess.move.util.Diagonals;

public class BishopMoveMap extends DelegateImmutableMap<Position, Collection<ChessMove>> {

    public BishopMoveMap(Diagonals diagonals) {
        super(initBishopMoves(diagonals));
    }

    private static Map<Position, Collection<ChessMove>> initBishopMoves(Diagonals diagonals) {
        Map<Position, Collection<ChessMove>> bishopMoves = new EnumMap<>(Position.class);
        for (Position position : Position.values()) {
            bishopMoves.put(position, diagonals.getDiagonalMoves(position, BISHOP));
        }
        return bishopMoves;
    }
}
