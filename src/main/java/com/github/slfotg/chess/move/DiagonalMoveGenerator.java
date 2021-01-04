package com.github.slfotg.chess.move;

import static com.github.slfotg.chess.enums.Position.*;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.slfotg.chess.enums.Position;

class DiagonalMoveGenerator {

    private final Map<Position, List<Position[]>> pathsFrom;

    public DiagonalMoveGenerator() {
        // @formatter:off
        Position[][] diagonals = new Position[][] {
                new Position[] { A7, B8                         },
                new Position[] { A6, B7, C8                     },
                new Position[] { A5, B6, C7, D8                 },
                new Position[] { A4, B5, C6, D7, E8             },
                new Position[] { A3, B4, C5, D6, E7, F8         },
                new Position[] { A2, B3, C4, D5, E6, F7, G8     },
                new Position[] { A1, B2, C3, D4, E5, F6, G7, H8 },
                new Position[] {     B1, C2, D3, E4, F5, G6, H7 },
                new Position[] {         C1, D2, E3, F4, G5, H6 },
                new Position[] {             D1, E2, F3, G4, H5 },
                new Position[] {                 E1, F2, G3, H4 },
                new Position[] {                     F1, G2, H3 },
                new Position[] {                         G1, H2 },

                new Position[] { A2, B1                         },
                new Position[] { A3, B2, C1                     },
                new Position[] { A4, B3, C2, D1                 },
                new Position[] { A5, B4, C3, D2, E1             },
                new Position[] { A6, B5, C4, D3, E2, F1         },
                new Position[] { A7, B6, C5, D4, E3, F2, G1     },
                new Position[] { A8, B7, C6, D5, E4, F3, G2, H1 },
                new Position[] {     B8, C7, D6, E5, F4, G3, H2 },
                new Position[] {         C8, D7, E6, F5, G4, H3 },
                new Position[] {             D8, E7, F6, G5, H4 },
                new Position[] {                 E8, F7, G6, H5 },
                new Position[] {                     F8, G7, H6 },
                new Position[] {                         G8, H7 }
        };
        // @formatter:on
        pathsFrom = new EnumMap<>(Position.class);
        for (int i = 0; i < diagonals.length; i += 1) {
            for (int j = 0; j < diagonals[i].length; j += 1) {
                List<Position[]> rowsFrom = getDiagnoalsFrom(j, diagonals[i]);
                pathsFrom.merge(diagonals[i][j], rowsFrom, (a, b) -> {
                    a.addAll(b);
                    return a;
                });
            }
        }
    }

    private static List<Position[]> getDiagnoalsFrom(int index, Position[] diagonal) {
        List<Position[]> diagonalsFrom = new LinkedList<>();
        if (index > 0) {
            for (int i = index - 1; i >= 0; i -= 1) {
                Position[] diagonalFrom = new Position[index - i + 1];
                for (int j = index; j >= i; j -= 1) {
                    diagonalFrom[index - j] = diagonal[j];
                }
                diagonalsFrom.add(diagonalFrom);
            }
        }
        if (index < diagonal.length - 1) {
            for (int i = index + 1; i < diagonal.length; i += 1) {
                Position[] diagonalFrom = new Position[i - index + 1];
                for (int j = index; j <= i; j += 1) {
                    diagonalFrom[j - index] = diagonal[j];
                }
                diagonalsFrom.add(diagonalFrom);
            }
        }
        return diagonalsFrom;
    }

    public List<ChessMove> generateMoves(Position startingPosition, Function<Position[], ChessMove> moveGenerator) {
        // @formatter:off
        return pathsFrom
                .get(startingPosition)
                .stream()
                .map(moveGenerator)
                .collect(Collectors.toCollection(LinkedList::new));
        // @formatter:on
    }
}
