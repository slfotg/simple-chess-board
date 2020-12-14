package com.github.slfotg.chess.move;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import com.github.slfotg.chess.Board;
import com.github.slfotg.chess.GameState;
import com.github.slfotg.chess.enums.CastlingRights;
import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;

/**
 * Represents a chess move for minor and major pieces (bishop, knight, rook, and
 * queen)
 * 
 */
public class PieceMove implements ChessMove {

    private final Piece piece;
    // path represents all positions a piece travels from and to to get the the
    // final position.
    // Ex: bishop from c4 to f7 would be represented by [C4, D5, E6, F7]
    // Ex: knight from f6 to g4 would be represented by only [F6, G4] since it
    // "hops"
    private final Position[] path;

    public PieceMove(Piece piece, Position... path) {
        this.piece = piece;
        this.path = path;
    }

    @Override
    public Board updateBoard(Board currentBoard) {
        Map<Position, Piece> currentPieces = new EnumMap<>(currentBoard.getCurrentPieces());
        Map<Position, Piece> opponentPieces = new EnumMap<>(currentBoard.getOpponentPieces());
        currentPieces.remove(path[0]);
        opponentPieces.remove(path[path.length - 1]);
        currentPieces.put(path[path.length - 1], piece);
        return new Board(currentPieces, opponentPieces, currentBoard.getKingPosition(),
                currentBoard.getOpponentKingPosition());
    }

    @Override
    public CastlingRights updateCastlingRights(CastlingRights currentPlayerRights) {
        if (piece == Piece.ROOK) {
            if (path[0] == Position.A1) {
                return currentPlayerRights.removeQueenSideRights();
            }
            if (path[0] == Position.H1) {
                return currentPlayerRights.removeKingSideRights();
            }
        }
        return currentPlayerRights;
    }

    @Override
    public boolean isAttackingMove() {
        return true;
    }

    @Override
    public boolean isValid(GameState currentState, Map<Position, Collection<ChessMove>> opponentAttacks) {
        Board board = currentState.getBoard();
        Map<Position, Piece> pieces = board.getCurrentPieces();
        Map<Position, Piece> opponentPieces = board.getOpponentPieces();
        Position kingPosition = board.getKingPosition();
        // don't really need to check against opponent king since the game would be over
        // at that point but doing it anyway to allow for different game rules
        Position opponentKingPosition = board.getOpponentKingPosition();

        // check the piece is actually at the location
        if (pieces.get(path[0]) != piece) {
            return false;
        }
        // check the path a piece takes to get to the final location
        // making sure it doesn't "run" into anything
        for (int i = 1; i < path.length - 1; i += 1) {
            if (pieces.containsKey(path[i]) || opponentPieces.containsKey(path[i]) || path[i] == kingPosition
                    || path[i] == opponentKingPosition) {
                return false;
            }
        }
        // check that you don't land at the same position as one of your own pieces
        return path[path.length - 1] != kingPosition && !pieces.containsKey(path[path.length - 1]);
    }

    @Override
    public boolean isCapture(Board currentBoard) {
        Map<Position, Piece> opponentPieces = currentBoard.getOpponentPieces();
        Position opponentKingPosition = currentBoard.getOpponentKingPosition();
        return path[path.length - 1] == opponentKingPosition || opponentPieces.containsKey(path[path.length - 1]);
    }

    @Override
    public boolean isLegal(GameState currentState, Map<Position, Collection<ChessMove>> opponentAttacks) {
        // TODO Auto-generated method stub
        return false;
    }

}
