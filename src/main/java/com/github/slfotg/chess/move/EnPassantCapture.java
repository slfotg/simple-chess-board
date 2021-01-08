package com.github.slfotg.chess.move;

import java.util.Optional;

import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;

class EnPassantCapture implements ChessMove {

    private final Position enPassantPawn;
    private final Position[] path;

    public EnPassantCapture(Position enPassantPawn, Position... path) {
        this.enPassantPawn = enPassantPawn;
        this.path = path;
    }

    @Override
    public Piece getPiece() {
        return Piece.PAWN;
    }

    @Override
    public Position[] getPath() {
        return path;
    }

    @Override
    public boolean isPawnMove() {
        return true;
    }

    @Override
    public Optional<Position> getAttackedPosition() {
        return Optional.of(enPassantPawn);
    }

    @Override
    public String toString() {
        return stringValue();
    }

}
