package com.github.slfotg.chess.move.map;

import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.slfotg.chess.enums.Position;
import com.github.slfotg.chess.enums.Side;
import com.github.slfotg.chess.move.Castle;
import com.github.slfotg.chess.move.ChessMove;
import com.github.slfotg.chess.move.KingMove;
import com.github.slfotg.chess.move.util.Diagonals;
import com.github.slfotg.chess.move.util.Rows;

public class KingMoveMap extends DelegateImmutableMap<Position, Collection<ChessMove>> {

    public KingMoveMap(Rows rows, Diagonals diagonals) {
        super(initKingMoves(rows, diagonals));
    }

    private static Map<Position, Collection<ChessMove>> initKingMoves(Rows rows, Diagonals diagonals) {
        Map<Position, Collection<ChessMove>> kingMoves = new EnumMap<>(Position.class);

        for (Position position : Position.values()) {
            List<ChessMove> moves = new LinkedList<>();
            for (List<Position> row : rows.rowsFrom(position)) {
                if (row.size() == 2) {
                    moves.add(new KingMove(row.get(0), row.get(1)));
                }
            }
            for (List<Position> diagonal : diagonals.diagonalsFrom(position)) {
                if (diagonal.size() == 2) {
                    moves.add(new KingMove(diagonal.get(0), diagonal.get(1)));
                }
            }
            if (position == Position.E1) {
                moves.add(new Castle(Side.KING));
                moves.add(new Castle(Side.QUEEN));
            }
            kingMoves.put(position, moves);
        }
        return kingMoves;
    }
}
