package com.github.slfotg.chess.move;

import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.github.slfotg.chess.Board;
import com.github.slfotg.chess.GameState;
import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;
import com.github.slfotg.chess.move.map.BishopMoveMap;
import com.github.slfotg.chess.move.map.KingMoveMap;
import com.github.slfotg.chess.move.map.KnightMoveMap;
import com.github.slfotg.chess.move.map.PawnMoveMap;
import com.github.slfotg.chess.move.map.QueenMoveMap;
import com.github.slfotg.chess.move.map.RookMoveMap;
import com.github.slfotg.chess.move.util.Diagonals;
import com.github.slfotg.chess.move.util.Rows;

public class MoveCalculator {

    private final Map<Piece, Map<Position, Collection<ChessMove>>> allMoveMap;

    public MoveCalculator() {
        Diagonals diagonals = new Diagonals();
        Rows rows = new Rows();

        allMoveMap = new EnumMap<>(Piece.class);
        allMoveMap.put(Piece.KING, new KingMoveMap(rows, diagonals));
        allMoveMap.put(Piece.QUEEN, new QueenMoveMap(rows, diagonals));
        allMoveMap.put(Piece.ROOK, new RookMoveMap(rows));
        allMoveMap.put(Piece.BISHOP, new BishopMoveMap(diagonals));
        allMoveMap.put(Piece.KNIGHT, new KnightMoveMap());
        allMoveMap.put(Piece.PAWN, new PawnMoveMap());
    }

    public Collection<ChessMove> getValidMoves(GameState currentState) {
        Board board = currentState.getBoard();

        return getAllMoves( board).stream().filter(move -> move.isValid(currentState, opponentAttacks(board)))
                .collect(Collectors.toList());
    }

    private Collection<ChessMove> getAllMoves(Board board) {
        Map<Position, Piece> pieces = board.getCurrentPieces();
        List<ChessMove> allMoves = new LinkedList<>();
        allMoves.addAll(allMoveMap.get(Piece.KING).get(board.getKingPosition()));
        for (Entry<Position, Piece> e : pieces.entrySet()) {
            Position position = e.getKey();
            Piece piece = e.getValue();
            allMoves.addAll(allMoveMap.get(piece).get(position));
        }
        return allMoves;
    }
    
    private Map<Position, Collection<ChessMove>> opponentAttacks(Board board) {
        // TODO
        return null;
    }
}
