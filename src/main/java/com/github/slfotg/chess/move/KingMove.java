package com.github.slfotg.chess.move;

import java.util.EnumMap;
import java.util.Map;

import com.github.slfotg.chess.Board;
import com.github.slfotg.chess.enums.CastlingRights;
import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;

public class KingMove extends PieceMove {

    private Position end;

    public KingMove(Position start, Position end) {
        super(Piece.KING, start, end);
        this.end = end;
    }

    @Override
    public Board updateBoard(Board currentBoard) {
        Map<Position, Piece> currentPieces = new EnumMap<>(currentBoard.getCurrentPieces());
        Map<Position, Piece> opponentPieces = new EnumMap<>(currentBoard.getOpponentPieces());
        return new Board(currentPieces, opponentPieces, end, currentBoard.getOpponentKingPosition());
    }

    @Override
    public CastlingRights updateCastlingRights(CastlingRights currentPlayerRights) {
        return CastlingRights.NONE;
    }

}
