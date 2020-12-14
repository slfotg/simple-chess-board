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

public class Diagonals {

    private Map<Position, List<List<Position>>> diagonalsFrom;

    public Diagonals() {
        List<Position[]> diagonals = new ArrayList<>(30);
        // @formatter:off
        diagonals.add(new Position[] { Position.A8 });
        diagonals.add(new Position[] { Position.A7, Position.B8 });
        diagonals.add(new Position[] { Position.A6, Position.B7, Position.C8 });
        diagonals.add(new Position[] { Position.A5, Position.B6, Position.C7, Position.D8 });
        diagonals.add(new Position[] { Position.A4, Position.B5, Position.C6, Position.D7, Position.E8 });
        diagonals.add(new Position[] { Position.A3, Position.B4, Position.C5, Position.D6, Position.E7, Position.F8 });
        diagonals.add(new Position[] { Position.A2, Position.B3, Position.C4, Position.D5, Position.E6, Position.F7, Position.G8 });
        diagonals.add(new Position[] { Position.A1, Position.B2, Position.C3, Position.D4, Position.E5, Position.F6, Position.G7, Position.H8 });
        diagonals.add(new Position[] { Position.B1, Position.C2, Position.D3, Position.E4, Position.F5, Position.G6, Position.H7 });
        diagonals.add(new Position[] { Position.C1, Position.D2, Position.E3, Position.F4, Position.G5, Position.H6 });
        diagonals.add(new Position[] { Position.D1, Position.E2, Position.F3, Position.G4, Position.H5 });
        diagonals.add(new Position[] { Position.E1, Position.F2, Position.G3, Position.H4 });
        diagonals.add(new Position[] { Position.F1, Position.G2, Position.H3 });
        diagonals.add(new Position[] { Position.G1, Position.H2 });
        diagonals.add(new Position[] { Position.H1 });

        diagonals.add(new Position[] { Position.A1 });
        diagonals.add(new Position[] { Position.A2, Position.B1 });
        diagonals.add(new Position[] { Position.A3, Position.B2, Position.C1 });
        diagonals.add(new Position[] { Position.A4, Position.B3, Position.C2, Position.D1 });
        diagonals.add(new Position[] { Position.A5, Position.B4, Position.C3, Position.D2, Position.E1 });
        diagonals.add(new Position[] { Position.A6, Position.B5, Position.C4, Position.D3, Position.E2, Position.F1 });
        diagonals.add(new Position[] { Position.A7, Position.B6, Position.C5, Position.D4, Position.E3, Position.F2, Position.G1 });
        diagonals.add(new Position[] { Position.A8, Position.B7, Position.C6, Position.D5, Position.E4, Position.F3, Position.G2, Position.H1 });
        diagonals.add(new Position[] { Position.B8, Position.C7, Position.D6, Position.E5, Position.F4, Position.G3, Position.H2 });
        diagonals.add(new Position[] { Position.C8, Position.D7, Position.E6, Position.F5, Position.G4, Position.H3 });
        diagonals.add(new Position[] { Position.D8, Position.E7, Position.F6, Position.G5, Position.H4 });
        diagonals.add(new Position[] { Position.E8, Position.F7, Position.G6, Position.H5 });
        diagonals.add(new Position[] { Position.F8, Position.G7, Position.H6 });
        diagonals.add(new Position[] { Position.G8, Position.H7 });
        diagonals.add(new Position[] { Position.H8 });
        // @formatter:on

        diagonalsFrom = new EnumMap<>(Position.class);
        for (Position[] diagonal : diagonals) {
            for (int i = 0; i < diagonal.length; i += 1) {
                if (!diagonalsFrom.containsKey(diagonal[i])) {
                    diagonalsFrom.put(diagonal[i], new LinkedList<>());
                }
                List<List<Position>> diagonalsFromI = diagonalsFrom.get(diagonal[i]);
                List<Position> lower = Arrays.asList(Arrays.copyOf(diagonal, i + 1));
                List<Position> upper = Arrays.asList(Arrays.copyOfRange(diagonal, i, diagonal.length));
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

    public List<List<Position>> diagonalsFrom(Position startingPosition) {
        return diagonalsFrom.get(startingPosition);
    }

    public Collection<ChessMove> getDiagonalMoves(Position position, Piece piece) {
        List<ChessMove> allDiagonalMoves = new LinkedList<>();
        for (List<Position> positions : diagonalsFrom(position)) {
            allDiagonalMoves.add(new PieceMove(piece, positions.toArray(new Position[0])));
        }
        return allDiagonalMoves;
    }
}
