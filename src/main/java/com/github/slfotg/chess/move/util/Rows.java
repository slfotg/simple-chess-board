package com.github.slfotg.chess.move.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;
import com.github.slfotg.chess.move.ChessMove;
import com.github.slfotg.chess.move.PieceMove;

public class Rows {

    private Map<Position, List<List<Position>>> rowsFrom;

    public Rows() {
        List<Position[]> rows = new ArrayList<>(16);
        rows.add(new Position[] { Position.A8, Position.B8, Position.C8, Position.D8, Position.E8, Position.F8, Position.G8, Position.H8 });
        rows.add(new Position[] { Position.A7, Position.B7, Position.C7, Position.D7, Position.E7, Position.F7, Position.G7, Position.H7 });
        rows.add(new Position[] { Position.A6, Position.B6, Position.C6, Position.D6, Position.E6, Position.F6, Position.G6, Position.H6 });
        rows.add(new Position[] { Position.A5, Position.B5, Position.C5, Position.D5, Position.E5, Position.F5, Position.G5, Position.H5 });
        rows.add(new Position[] { Position.A4, Position.B4, Position.C4, Position.D4, Position.E4, Position.F4, Position.G4, Position.H4 });
        rows.add(new Position[] { Position.A3, Position.B3, Position.C3, Position.D3, Position.E3, Position.F3, Position.G3, Position.H3 });
        rows.add(new Position[] { Position.A2, Position.B2, Position.C2, Position.D2, Position.E2, Position.F2, Position.G2, Position.H2 });
        rows.add(new Position[] { Position.A1, Position.B1, Position.C1, Position.D1, Position.E1, Position.F1, Position.G1, Position.H1 });

        rows.add(new Position[] { Position.A8, Position.A7, Position.A6, Position.A5, Position.A4, Position.A3, Position.A2, Position.A1 });
        rows.add(new Position[] { Position.B8, Position.B7, Position.B6, Position.B5, Position.B4, Position.B3, Position.B2, Position.B1 });
        rows.add(new Position[] { Position.C8, Position.C7, Position.C6, Position.C5, Position.C4, Position.C3, Position.C2, Position.C1 });
        rows.add(new Position[] { Position.D8, Position.D7, Position.D6, Position.D5, Position.D4, Position.D3, Position.D2, Position.D1 });
        rows.add(new Position[] { Position.E8, Position.E7, Position.E6, Position.E5, Position.E4, Position.E3, Position.E2, Position.E1 });
        rows.add(new Position[] { Position.F8, Position.F7, Position.F6, Position.F5, Position.F4, Position.F3, Position.F2, Position.F1 });
        rows.add(new Position[] { Position.G8, Position.G7, Position.G6, Position.G5, Position.G4, Position.G3, Position.G2, Position.G1 });
        rows.add(new Position[] { Position.H8, Position.H7, Position.H6, Position.H5, Position.H4, Position.H3, Position.H2, Position.H1 });

        rowsFrom = new EnumMap<>(Position.class);
        for (Position[] row : rows) {
            for (int i = 0; i < row.length; i += 1) {
                if (!rowsFrom.containsKey(row[i])) {
                    rowsFrom.put(row[i], new LinkedList<>());
                }
                List<List<Position>> diagonalsFromI = rowsFrom.get(row[i]);
                List<Position> lower = Arrays.asList(Arrays.copyOf(row, i + 1));
                List<Position> upper = Arrays.asList(Arrays.copyOfRange(row, i, row.length));
                if (!lower.isEmpty()) {
                    Collections.reverse(lower);
                    diagonalsFromI.add(lower);
                }
                if (!upper.isEmpty()) {
                    diagonalsFromI.add(upper);
                }
            }
        }
    }

    public List<List<Position>> rowsFrom(Position startingPosition) {
        return rowsFrom.get(startingPosition);
    }

    public Collection<ChessMove> getStraightMoves(Position position, Piece piece) {
        List<ChessMove> allStraightMoves = new LinkedList<>();
        for (List<Position> positions : rowsFrom(position)) {
            allStraightMoves.add(new PieceMove(piece, positions.toArray(new Position[0])));
        }
        return allStraightMoves;
    }
}
