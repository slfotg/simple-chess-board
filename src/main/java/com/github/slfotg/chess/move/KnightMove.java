package com.github.slfotg.chess.move;

import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;

class KnightMove implements ChessMove {

    private final Position[] path;

    public KnightMove(Position start, Position end) {
        path = new Position[] { start, end };
    }

    @Override
    public Piece getPiece() {
        return Piece.KNIGHT;
    }

    @Override
    public Position[] getPath() {
        return path;
    }

    @Override
    public String toString() {
        return stringValue();
    }

}
