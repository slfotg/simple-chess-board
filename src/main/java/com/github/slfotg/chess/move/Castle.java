package com.github.slfotg.chess.move;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import com.github.slfotg.chess.Board;
import com.github.slfotg.chess.GameState;
import com.github.slfotg.chess.enums.CastlingRights;
import com.github.slfotg.chess.enums.Color;
import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;
import com.github.slfotg.chess.enums.Side;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Castle implements ChessMove {

    private final Side side;

    @Override
    public Board updateBoard(Board currentBoard) {
        Map<Position, Piece> currentPieces = new EnumMap<>(currentBoard.getCurrentPieces());
        Map<Position, Piece> opponentPieces = new EnumMap<>(currentBoard.getOpponentPieces());

        currentPieces.remove(Position.E1);
        if (side == Side.KING) {
            currentPieces.remove(Position.H1);
            currentPieces.put(Position.F1, Piece.ROOK);
        } else {
            currentPieces.remove(Position.A1);
            currentPieces.put(Position.D1, Piece.ROOK);
        }
        Position kingPosition = (side == Side.KING ? Position.G1 : Position.C1);

        return new Board(currentPieces, opponentPieces, kingPosition, currentBoard.getOpponentKingPosition());
    }

    @Override
    public CastlingRights updateCastlingRights(CastlingRights currentPlayerRights) {
        return CastlingRights.NONE;
    }

    @Override
    public boolean isAttackingMove() {
        return false;
    }

    @Override
    public boolean isValid(GameState currentState, Map<Position, Collection<ChessMove>> opponentAttacks) {
        if (!currentCastlingRights(currentState).hasRights(side)) {
            return false;
        }
        Map<Position, Piece> currentPieces = currentState.getBoard().getCurrentPieces();
        Map<Position, Piece> opponentPieces = currentState.getBoard().getOpponentPieces();
        if (side == Side.KING) {
            if (currentPieces.containsKey(Position.F1) || currentPieces.containsKey(Position.G1)
                    || opponentPieces.containsKey(Position.F1) || opponentPieces.containsKey(Position.G1)) {
                return false;
            }
        } else {
            if (currentPieces.containsKey(Position.B1) || currentPieces.containsKey(Position.C1)
                    || currentPieces.containsKey(Position.D1) || opponentPieces.containsKey(Position.B1)
                    || opponentPieces.containsKey(Position.C1) || opponentPieces.containsKey(Position.D1)) {
                return false;
            }
        }
        // TODO - check for moving through check
        return true;
    }

    private CastlingRights currentCastlingRights(GameState currentState) {
        if (currentState.getActiveColor() == Color.WHITE) {
            return currentState.getWhiteRights();
        }
        return currentState.getBlackRights();
    }

    @Override
    public boolean isPawnMove() {
        return false;
    }

    @Override
    public boolean isCapture(Board currentBoard) {
        return false;
    }

    @Override
    public boolean isLegal(GameState currentState, Map<Position, Collection<ChessMove>> opponentAttacks) {
        // TODO Auto-generated method stub
        return false;
    }
}
