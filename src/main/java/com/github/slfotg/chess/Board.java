package com.github.slfotg.chess;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;

import lombok.Getter;

@Getter
public class Board {

    private static final Map<Position, Position> MIRROR_MAP;

    private final Map<Position, Piece> currentPieces;
    private final Map<Position, Piece> opponentPieces;
    private final Position kingPosition;
    private final Position opponentKingPosition;

    public Board(Map<Position, Piece> currentPieces, Map<Position, Piece> opponentPieces, Position kingPosition,
            Position oponentKingPosition) {
        this.currentPieces = Collections.unmodifiableMap(currentPieces);
        this.opponentPieces = Collections.unmodifiableMap(opponentPieces);
        this.kingPosition = kingPosition;
        this.opponentKingPosition = oponentKingPosition;
    }

    public Board mirror() {
        Map<Position, Piece> mirrorCurrentPieces = new EnumMap<>(Position.class);
        this.opponentPieces.forEach((position, piece) -> mirrorCurrentPieces.put(MIRROR_MAP.get(position), piece));
        Map<Position, Piece> mirrorOpponentPieces = new EnumMap<>(Position.class);
        this.currentPieces.forEach((position, piece) -> mirrorOpponentPieces.put(MIRROR_MAP.get(position), piece));
        return new Board(mirrorCurrentPieces, mirrorOpponentPieces, MIRROR_MAP.get(opponentKingPosition),
                MIRROR_MAP.get(kingPosition));
    }

    static {
        MIRROR_MAP = new EnumMap<>(Position.class);
        MIRROR_MAP.put(Position.A1, Position.A8);
        MIRROR_MAP.put(Position.A2, Position.A7);
        MIRROR_MAP.put(Position.A3, Position.A6);
        MIRROR_MAP.put(Position.A4, Position.A5);
        MIRROR_MAP.put(Position.A5, Position.A4);
        MIRROR_MAP.put(Position.A6, Position.A3);
        MIRROR_MAP.put(Position.A7, Position.A2);
        MIRROR_MAP.put(Position.A8, Position.A1);

        MIRROR_MAP.put(Position.B1, Position.B8);
        MIRROR_MAP.put(Position.B2, Position.B7);
        MIRROR_MAP.put(Position.B3, Position.B6);
        MIRROR_MAP.put(Position.B4, Position.B5);
        MIRROR_MAP.put(Position.B5, Position.B4);
        MIRROR_MAP.put(Position.B6, Position.B3);
        MIRROR_MAP.put(Position.B7, Position.B2);
        MIRROR_MAP.put(Position.B8, Position.B1);

        MIRROR_MAP.put(Position.C1, Position.C8);
        MIRROR_MAP.put(Position.C2, Position.C7);
        MIRROR_MAP.put(Position.C3, Position.C6);
        MIRROR_MAP.put(Position.C4, Position.C5);
        MIRROR_MAP.put(Position.C5, Position.C4);
        MIRROR_MAP.put(Position.C6, Position.C3);
        MIRROR_MAP.put(Position.C7, Position.C2);
        MIRROR_MAP.put(Position.C8, Position.C1);

        MIRROR_MAP.put(Position.D1, Position.D8);
        MIRROR_MAP.put(Position.D2, Position.D7);
        MIRROR_MAP.put(Position.D3, Position.D6);
        MIRROR_MAP.put(Position.D4, Position.D5);
        MIRROR_MAP.put(Position.D5, Position.D4);
        MIRROR_MAP.put(Position.D6, Position.D3);
        MIRROR_MAP.put(Position.D7, Position.D2);
        MIRROR_MAP.put(Position.D8, Position.D1);

        MIRROR_MAP.put(Position.E1, Position.E8);
        MIRROR_MAP.put(Position.E2, Position.E7);
        MIRROR_MAP.put(Position.E3, Position.E6);
        MIRROR_MAP.put(Position.E4, Position.E5);
        MIRROR_MAP.put(Position.E5, Position.E4);
        MIRROR_MAP.put(Position.E6, Position.E3);
        MIRROR_MAP.put(Position.E7, Position.E2);
        MIRROR_MAP.put(Position.E8, Position.E1);

        MIRROR_MAP.put(Position.F1, Position.F8);
        MIRROR_MAP.put(Position.F2, Position.F7);
        MIRROR_MAP.put(Position.F3, Position.F6);
        MIRROR_MAP.put(Position.F4, Position.F5);
        MIRROR_MAP.put(Position.F5, Position.F4);
        MIRROR_MAP.put(Position.F6, Position.F3);
        MIRROR_MAP.put(Position.F7, Position.F2);
        MIRROR_MAP.put(Position.F8, Position.F1);

        MIRROR_MAP.put(Position.G1, Position.G8);
        MIRROR_MAP.put(Position.G2, Position.G7);
        MIRROR_MAP.put(Position.G3, Position.G6);
        MIRROR_MAP.put(Position.G4, Position.G5);
        MIRROR_MAP.put(Position.G5, Position.G4);
        MIRROR_MAP.put(Position.G6, Position.G3);
        MIRROR_MAP.put(Position.G7, Position.G2);
        MIRROR_MAP.put(Position.G8, Position.G1);

        MIRROR_MAP.put(Position.H1, Position.H8);
        MIRROR_MAP.put(Position.H2, Position.H7);
        MIRROR_MAP.put(Position.H3, Position.H6);
        MIRROR_MAP.put(Position.H4, Position.H5);
        MIRROR_MAP.put(Position.H5, Position.H4);
        MIRROR_MAP.put(Position.H6, Position.H3);
        MIRROR_MAP.put(Position.H7, Position.H2);
        MIRROR_MAP.put(Position.H8, Position.H1);
    }
}
