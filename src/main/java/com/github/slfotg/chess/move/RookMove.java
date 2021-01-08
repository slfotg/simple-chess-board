package com.github.slfotg.chess.move;

import com.github.slfotg.chess.enums.CastlingRights;
import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class RookMove implements ChessMove {

    private final Position[] path;

    @Override
    public Piece getPiece() {
        return Piece.ROOK;
    }

    @Override
    public Position[] getPath() {
        return path;
    }

    @Override
    public CastlingRights updateCastlingRights(CastlingRights currentPlayerRights) {
        switch (getStartingPosition()) {
        case A1:
            return currentPlayerRights.removeQueenSideRights();
        case H1:
            return currentPlayerRights.removeKingSideRights();
        default:
            return currentPlayerRights;
        }
    }

    @Override
    public String toString() {
        return stringValue();
    }

}
