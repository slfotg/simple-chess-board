package com.github.slfotg.chess.move;

import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class BishopMove implements ChessMove {

    private final Position[] path;

    @Override
    public Piece getPiece() {
        return Piece.BISHOP;
    }

    @Override
    public Position[] getPath() {
        return path;
    }

    @Override
    public String toString() {
        return String.format("B: %s -> %s", getStartingPosition(), getFinalPosition());
    }

}
