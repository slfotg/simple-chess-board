package com.github.slfotg.chess.move;

import static com.github.slfotg.chess.enums.Position.*;

import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.slfotg.chess.enums.Position;

class StraightMoveGenerator {

    private Map<Position, List<Position[]>> pathsFrom;

    public StraightMoveGenerator() {
        // @formatter:off
        Position[][] rows = new Position[][] {
                new Position[] { A8, B8, C8, D8, E8, F8, G8, H8 },
                new Position[] { A7, B7, C7, D7, E7, F7, G7, H7 },
                new Position[] { A6, B6, C6, D6, E6, F6, G6, H6 },
                new Position[] { A5, B5, C5, D5, E5, F5, G5, H5 },
                new Position[] { A4, B4, C4, D4, E4, F4, G4, H4 },
                new Position[] { A3, B3, C3, D3, E3, F3, G3, H3 },
                new Position[] { A2, B2, C2, D2, E2, F2, G2, H2 },
                new Position[] { A1, B1, C1, D1, E1, F1, G1, H1 },

                new Position[] { A8, A7, A6, A5, A4, A3, A2, A1 },
                new Position[] { B8, B7, B6, B5, B4, B3, B2, B1 },
                new Position[] { C8, C7, C6, C5, C4, C3, C2, C1 },
                new Position[] { D8, D7, D6, D5, D4, D3, D2, D1 },
                new Position[] { E8, E7, E6, E5, E4, E3, E2, E1 },
                new Position[] { F8, F7, F6, F5, F4, F3, F2, F1 },
                new Position[] { G8, G7, G6, G5, G4, G3, G2, G1 },
                new Position[] { H8, H7, H6, H5, H4, H3, H2, H1 }
        };
        // @formatter:on

        pathsFrom = new EnumMap<>(Position.class);
        for (int i = 0; i < rows.length; i += 1) {
            for (int j = 0; j < rows[i].length; j += 1) {
                List<Position[]> rowsFrom = getRowsFrom(j, rows[i]);
                pathsFrom.merge(rows[i][j], rowsFrom, (a, b) -> {
                    a.addAll(b);
                    return a;
                });
            }
        }
    }

    private static List<Position[]> getRowsFrom(int index, Position[] row) {
        List<Position[]> rowsFrom = new LinkedList<>();
        if (index > 0) {
            for (int i = index - 1; i >= 0; i -= 1) {
                Position[] rowFrom = new Position[index - i + 1];
                for (int j = index; j >= i; j -= 1) {
                    rowFrom[index - j] = row[j];
                }
                rowsFrom.add(rowFrom);
            }
        }
        if (index < row.length - 1) {
            for (int i = index + 1; i < row.length; i += 1) {
                Position[] rowFrom = new Position[i - index + 1];
                for (int j = index; j <= i; j += 1) {
                    rowFrom[j - index] = row[j];
                }
                rowsFrom.add(rowFrom);
            }
        }
        return rowsFrom;
    }

    public Collection<ChessMove> generateMoves(Position startingPosition,
            Function<Position[], ChessMove> moveGenerator) {
        // @formatter:off
        return pathsFrom
                .get(startingPosition)
                .stream()
                .map(moveGenerator)
                .collect(Collectors.toCollection(LinkedList::new));
        // @formatter:on
    }
}
