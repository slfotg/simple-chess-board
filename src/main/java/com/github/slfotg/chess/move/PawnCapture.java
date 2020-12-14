package com.github.slfotg.chess.move;

import com.github.slfotg.chess.Board;
import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;

public class PawnCapture extends PieceMove {

    public PawnCapture(Piece piece, Position[] path, Piece promotion) {
        super(piece, path);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean isPawnMove() {
        return true;
    }

    @Override
    public boolean isCapture(Board board) {
        return true;
    }

}
