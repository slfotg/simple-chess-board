package com.github.slfotg.chess;

import static com.github.slfotg.chess.enums.Piece.*;
import static com.github.slfotg.chess.enums.Position.*;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;

import lombok.Getter;

@Getter
public class Board {

    private final Map<Position, Piece> currentPieces;
    private final Map<Position, Piece> opponentPieces;
    private final Position kingPosition;
    private final Position opponentKingPosition;

    public Board(Map<Position, Piece> currentPieces, Map<Position, Piece> opponentPieces, Position kingPosition,
            Position oponentKingPosition) {
        this.currentPieces = new EnumMap<>(currentPieces);
        this.opponentPieces = new EnumMap<>(opponentPieces);
        this.kingPosition = kingPosition;
        this.opponentKingPosition = oponentKingPosition;
    }

    public Map<Position, Piece> getCurrentPieces() {
        return Collections.unmodifiableMap(currentPieces);
    }

    public Map<Position, Piece> getOpponentPieces() {
        return Collections.unmodifiableMap(opponentPieces);
    }

    public Optional<Piece> pieceAt(Position position) {
        if (currentPieces.containsKey(position)) {
            return Optional.of(currentPieces.get(position));
        }
        if (opponentPieces.containsKey(position)) {
            return Optional.of(opponentPieces.get(position));
        }
        if (kingPosition == position || opponentKingPosition == position) {
            return Optional.of(KING);
        }
        return Optional.ofNullable(null);
    }

    public static Board initialBoard() {
        Map<Position, Piece> currentPieces = new EnumMap<>(Position.class);
        Map<Position, Piece> opponentPieces = new EnumMap<>(Position.class);
        Position kingPosition = Position.E1;
        Position opponentKingPosition = Position.E8;

        currentPieces.put(A2, PAWN);
        currentPieces.put(B2, PAWN);
        currentPieces.put(C2, PAWN);
        currentPieces.put(D2, PAWN);
        currentPieces.put(E2, PAWN);
        currentPieces.put(F2, PAWN);
        currentPieces.put(G2, PAWN);
        currentPieces.put(H2, PAWN);

        currentPieces.put(A1, ROOK);
        currentPieces.put(B1, KNIGHT);
        currentPieces.put(C1, BISHOP);
        currentPieces.put(D1, QUEEN);
        currentPieces.put(F1, BISHOP);
        currentPieces.put(G1, KNIGHT);
        currentPieces.put(H1, ROOK);

        opponentPieces.put(A7, PAWN);
        opponentPieces.put(B7, PAWN);
        opponentPieces.put(C7, PAWN);
        opponentPieces.put(D7, PAWN);
        opponentPieces.put(E7, PAWN);
        opponentPieces.put(F7, PAWN);
        opponentPieces.put(G7, PAWN);
        opponentPieces.put(H7, PAWN);

        opponentPieces.put(A8, ROOK);
        opponentPieces.put(B8, KNIGHT);
        opponentPieces.put(C8, BISHOP);
        opponentPieces.put(D8, QUEEN);
        opponentPieces.put(F8, BISHOP);
        opponentPieces.put(G8, KNIGHT);
        opponentPieces.put(H8, ROOK);

        return new Board(currentPieces, opponentPieces, kingPosition, opponentKingPosition);
    }
}
