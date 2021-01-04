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
        if (path[0] == Position.A1) {
            return currentPlayerRights.removeQueenSideRights();
        }
        if (path[0] == Position.H1) {
            return currentPlayerRights.removeKingSideRights();
        }
        return currentPlayerRights;
    }

    @Override
    public String toString() {
        return String.format("R: %s -> %s", getStartingPosition(), getFinalPosition());
    }

}
