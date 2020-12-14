package com.github.slfotg.chess.move.map;

import static com.github.slfotg.chess.enums.Piece.QUEEN;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import com.github.slfotg.chess.enums.Position;
import com.github.slfotg.chess.move.ChessMove;
import com.github.slfotg.chess.move.util.Diagonals;
import com.github.slfotg.chess.move.util.Rows;

public class QueenMoveMap extends DelegateImmutableMap<Position, Collection<ChessMove>> {

    public QueenMoveMap(Rows rows, Diagonals diagonals) {
        super(initQueenMoves(rows, diagonals));
    }

    private static Map<Position, Collection<ChessMove>> initQueenMoves(Rows rows, Diagonals diagonals) {
        Map<Position, Collection<ChessMove>> queenMoves = new EnumMap<>(Position.class);
        for (Position position : Position.values()) {
            Collection<ChessMove> moves = rows.getStraightMoves(position, QUEEN);
            moves.addAll(diagonals.getDiagonalMoves(position, QUEEN));
            queenMoves.put(position, moves);
        }
        return queenMoves;
    }

}
