package com.github.slfotg.chess.move;

import com.github.slfotg.chess.Board;
import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;

public class PawnAdvance extends PieceMove {

    Position finalPosition;

    public PawnAdvance(Piece piece, Position[] path) {
        super(piece, path);
    }

    @Override
    public boolean isPawnMove() {
        return true;
    }

    @Override
    public boolean isCapture(Board board) {
        return false;
    }

}
