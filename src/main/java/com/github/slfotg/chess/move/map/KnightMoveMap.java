package com.github.slfotg.chess.move.map;

import static com.github.slfotg.chess.enums.Piece.KNIGHT;
import static com.github.slfotg.chess.enums.Position.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.slfotg.chess.enums.Position;
import com.github.slfotg.chess.move.ChessMove;
import com.github.slfotg.chess.move.PieceMove;

public class KnightMoveMap extends DelegateImmutableMap<Position, Collection<ChessMove>> {

    public KnightMoveMap() {
        super(initKnightMoves());
    }

    private static ChessMove knightMove(Position startPosition, Position endPosition) {
        return new PieceMove(KNIGHT, startPosition, endPosition);
    }

    private static void initKnightMovesFrom(Map<Position, Collection<ChessMove>> knightMoves, Position startPosition,
            Position... endPositions) {
        knightMoves.put(startPosition, Arrays.asList(endPositions).stream()
                .map(endPosition -> knightMove(startPosition, endPosition)).collect(Collectors.toList()));
    }

    private static Map<Position, Collection<ChessMove>> initKnightMoves() {
        Map<Position, Collection<ChessMove>> knightMoves = new EnumMap<>(Position.class);
        // @formatter:off
        initKnightMovesFrom(knightMoves, A1,                     B3,     C2);
        initKnightMovesFrom(knightMoves, A2,                     B4, C1, C3);
        initKnightMovesFrom(knightMoves, A3,                 B1, B5, C2, C4);
        initKnightMovesFrom(knightMoves, A4,                 B2, B6, C3, C5);
        initKnightMovesFrom(knightMoves, A5,                 B3, B7, C4, C6);
        initKnightMovesFrom(knightMoves, A6,                 B4, B8, C5, C7);
        initKnightMovesFrom(knightMoves, A7,                 B5,     C6, C8);
        initKnightMovesFrom(knightMoves, A8,                 B6,     C7    );

        initKnightMovesFrom(knightMoves, B1,             A3,     C3,     D2);
        initKnightMovesFrom(knightMoves, B2,             A4,     C4, D1, D3);
        initKnightMovesFrom(knightMoves, B3,         A1, A5, C1, C5, D2, D4);
        initKnightMovesFrom(knightMoves, B4,         A2, A6, C2, C6, D3, D5);
        initKnightMovesFrom(knightMoves, B5,         A3, A7, C3, C7, D4, D6);
        initKnightMovesFrom(knightMoves, B6,         A4, A8, C4, C8, D5, D7);
        initKnightMovesFrom(knightMoves, B7,         A5,     C5,     D6, D8);
        initKnightMovesFrom(knightMoves, B8,         A6,     C6,     D7    );

        initKnightMovesFrom(knightMoves, C1,     A2,     B3,     D3,     E2);
        initKnightMovesFrom(knightMoves, C2, A1, A3,     B4,     D4, E1, E3);
        initKnightMovesFrom(knightMoves, C3, A2, A4, B1, B5, D1, D5, E2, E4);
        initKnightMovesFrom(knightMoves, C4, A3, A5, B2, B6, D2, D6, E3, E5);
        initKnightMovesFrom(knightMoves, C5, A4, A6, B3, B7, D3, D7, E4, E6);
        initKnightMovesFrom(knightMoves, C6, A5, A7, B4, B8, D4, D8, E5, E7);
        initKnightMovesFrom(knightMoves, C7, A6, A8, B5,     D5,     E6, E8);
        initKnightMovesFrom(knightMoves, C8, A7,     B6,     D6,     E7    );

        initKnightMovesFrom(knightMoves, D1,     B2,     C3,     E3,     F2);
        initKnightMovesFrom(knightMoves, D2, B1, B3,     C4,     E4, F1, F3);
        initKnightMovesFrom(knightMoves, D3, B2, B4, C1, C5, E1, E5, F2, F4);
        initKnightMovesFrom(knightMoves, D4, B3, B5, C2, C6, E2, E6, F3, F5);
        initKnightMovesFrom(knightMoves, D5, B4, B6, C3, C7, E3, E7, F4, F6);
        initKnightMovesFrom(knightMoves, D6, B5, B7, C4, C8, E4, E8, F5, F7);
        initKnightMovesFrom(knightMoves, D7, B6, B8, C5,     E5,     F6, F8);
        initKnightMovesFrom(knightMoves, D8, B7,     C6,     E6,     F7    );

        initKnightMovesFrom(knightMoves, E1,     C2,     D3,     F3,     G2);
        initKnightMovesFrom(knightMoves, E2, C1, C3,     D4,     F4, G1, G3);
        initKnightMovesFrom(knightMoves, E3, C2, C4, D1, D5, F1, F5, G2, G4);
        initKnightMovesFrom(knightMoves, E4, C3, C5, D2, D6, F2, F6, G3, G5);
        initKnightMovesFrom(knightMoves, E5, C4, C6, D3, D7, F3, F7, G4, G6);
        initKnightMovesFrom(knightMoves, E6, C5, C7, D4, D8, F4, F8, G5, G7);
        initKnightMovesFrom(knightMoves, E7, C6, C8, D5,     F5,     G6, G8);
        initKnightMovesFrom(knightMoves, E8, C7,     D6,     F6,     G7    );

        initKnightMovesFrom(knightMoves, F1,     D2,     E3,     G3,     H2);
        initKnightMovesFrom(knightMoves, F2, D1, D3,     E4,     G4, H1, H3);
        initKnightMovesFrom(knightMoves, F3, D2, D4, E1, E5, G1, G5, H2, H4);
        initKnightMovesFrom(knightMoves, F4, D3, D5, E2, E6, G2, G6, H3, H5);
        initKnightMovesFrom(knightMoves, F5, D4, D6, E3, E7, G3, G7, H4, H6);
        initKnightMovesFrom(knightMoves, F6, D5, D7, E4, E8, G4, G8, H5, H7);
        initKnightMovesFrom(knightMoves, F7, D6, D8, E5,     G5,     H6, H8);
        initKnightMovesFrom(knightMoves, F8, D7,     E6,     G6,     H7    );

        initKnightMovesFrom(knightMoves, G1,     E2,     F3,     H3        );
        initKnightMovesFrom(knightMoves, G2, E1, E3,     F4,     H4        );
        initKnightMovesFrom(knightMoves, G3, E2, E4, F1, F5, H1, H5        );
        initKnightMovesFrom(knightMoves, G4, E3, E5, F2, F6, H2, H6        );
        initKnightMovesFrom(knightMoves, G5, E4, E6, F3, F7, H3, H7        );
        initKnightMovesFrom(knightMoves, G6, E5, E7, F4, F8, H4, H8        );
        initKnightMovesFrom(knightMoves, G7, E6, E8, F5,     H5            );
        initKnightMovesFrom(knightMoves, G8, E7,     F6,     H6            );

        initKnightMovesFrom(knightMoves, H1,     F2,     G3                );
        initKnightMovesFrom(knightMoves, H2, F1, F3,     G4                );
        initKnightMovesFrom(knightMoves, H3, F2, F4, G1, G5                );
        initKnightMovesFrom(knightMoves, H4, F3, F5, G2, G6                );
        initKnightMovesFrom(knightMoves, H5, F4, F6, G3, G7                );
        initKnightMovesFrom(knightMoves, H6, F5, F7, G4, G8                );
        initKnightMovesFrom(knightMoves, H7, F6, F8, G5                    );
        initKnightMovesFrom(knightMoves, H8, F7,     G6                    );
        // @formatter:on
        return knightMoves;
    }
}
