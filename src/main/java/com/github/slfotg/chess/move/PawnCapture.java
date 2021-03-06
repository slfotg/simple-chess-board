package com.github.slfotg.chess.move;

import java.util.EnumMap;
import java.util.Map;

import com.github.slfotg.chess.Board;
import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;

class PawnCapture implements ChessMove {

    private final Position[] path;
    private final Piece finalPiece;

    public PawnCapture(Position... path) {
        this(Piece.PAWN, path);
    }

    public PawnCapture(Piece promotedTo, Position... path) {
        this.path = path;
        this.finalPiece = promotedTo;
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
    public Board applyMove(Board currentBoard) {
        // @formatter:off
        Map<Position, Piece> currentPieces =
                currentBoard.getCurrentPieces().isEmpty()
                ? new EnumMap<>(Position.class)
                : new EnumMap<>(currentBoard.getCurrentPieces());
        Map<Position, Piece> opponentPieces =
                currentBoard.getOpponentPieces().isEmpty()
                ? new EnumMap<>(Position.class)
                : new EnumMap<>(currentBoard.getOpponentPieces());
        // @formatter:off
        currentPieces.remove(getStartingPosition());
        getAttackedPosition().ifPresent(opponentPieces::remove);
        currentPieces.put(getFinalPosition(), finalPiece);
        return new Board(currentPieces, opponentPieces, currentBoard.getKingPosition(),
                currentBoard.getOpponentKingPosition());
    }

    @Override
    public boolean isCapture(Board board) {
        return true;
    }

    @Override
    public boolean isAttackingMove() {
        return true;
    }

    @Override
    public boolean isPawnMove() {
        return true;
    }

    @Override
    public String toString() {
        return stringValue();
    }

}
