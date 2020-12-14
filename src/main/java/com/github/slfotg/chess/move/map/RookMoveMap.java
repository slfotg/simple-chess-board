package com.github.slfotg.chess.move.map;

import static com.github.slfotg.chess.enums.Piece.ROOK;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import com.github.slfotg.chess.enums.Position;
import com.github.slfotg.chess.move.ChessMove;
import com.github.slfotg.chess.move.util.Rows;

public class RookMoveMap extends DelegateImmutableMap<Position, Collection<ChessMove>> {

    public RookMoveMap(Rows rows) {
        super(initRookMoves(rows));
    }

    private static Map<Position, Collection<ChessMove>> initRookMoves(Rows rows) {
        Map<Position, Collection<ChessMove>> rookMoves = new EnumMap<>(Position.class);
        for (Position position : Position.values()) {
            rookMoves.put(position, rows.getStraightMoves(position, ROOK));
        }
        return rookMoves;
    }
}
