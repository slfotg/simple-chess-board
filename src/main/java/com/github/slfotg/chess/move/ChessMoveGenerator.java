package com.github.slfotg.chess.move;

import static com.github.slfotg.chess.enums.Position.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.slfotg.chess.Board;
import com.github.slfotg.chess.enums.CastlingRights;
import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;
import com.github.slfotg.chess.util.PositionInverter;

public class ChessMoveGenerator {

    private final PositionInverter positionInverter = new PositionInverter();
    private final Map<Position, Collection<ChessMove>> allPawnMoves;
    private final Map<Position, Collection<ChessMove>> allKnightMoves;
    private final Map<Position, Collection<ChessMove>> allBishopMoves;
    private final Map<Position, Collection<ChessMove>> allRookMoves;
    private final Map<Position, Collection<ChessMove>> allQueenMoves;
    private final Map<Position, Collection<ChessMove>> allKingMoves;

    public ChessMoveGenerator() {
        DiagonalMoveGenerator diagonalMoveGenerator = new DiagonalMoveGenerator();
        StraightMoveGenerator straightMoveGenerator = new StraightMoveGenerator();
        allPawnMoves = generateAllPawnMoves();
        allKnightMoves = generateAllKnightMoves();
        allBishopMoves = generateAllBishopMoves(diagonalMoveGenerator);
        allRookMoves = generateAllRookMoves(straightMoveGenerator);
        allQueenMoves = generateAllQueenMoves(straightMoveGenerator, diagonalMoveGenerator);
        allKingMoves = generateAllKingMoves(straightMoveGenerator, diagonalMoveGenerator);
    }

    private boolean isPathBlocked(ChessMove move, Board board, List<Position> attackedPositions) {
        // check to see if there's any piece (white or black) in the way of getting to
        // final position
        Position[] path = move.getPath();
        for (int i = 1; i < path.length - 1; i += 1) {
            if (board.pieceAt(path[i]).isPresent()) {
                return true;
            }
        }

        // check (for castling) if any opponent piece is attacking any position the king
        // travels along
        Optional<List<Position>> castlingPositions = move.getCastlingPositions();
        if (castlingPositions.isPresent()) {
            for (Position position : castlingPositions.get()) {
                for (Position attackedPosition : attackedPositions) {
                    if (position == attackedPosition) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isFinalPositionBlocked(ChessMove move, Board board, Position enPassant) {
        var finalPosition = move.getFinalPosition();
        var attackedPosition = move.getAttackedPosition();
        if (!move.isAttackingMove()) {
            // for pawn advances or castling, no piece can be on the final position
            if (board.pieceAt(finalPosition).isPresent()) {
                return true;
            }
        } else if (attackedPosition.isPresent()) {
            if (move.isPawnMove()) {
                // make sure there is an opponent piece to attack
                if (attackedPosition.get() != finalPosition) {
                    return finalPosition == enPassant;
                }
                if (!board.getOpponentPieces().containsKey(attackedPosition.get())) {
                    return true;
                }
            } else {
                // checks to make sure not attacking own piece
                if (board.getCurrentPieces().containsKey(attackedPosition.get())) {
                    return true;
                }
                if (board.getKingPosition() == attackedPosition.get()) {
                    return true;
                }
            }
        } else {
            throw new RuntimeException("an attacking move should always have an attackedPosition");
        }
        return false;
    }

    private boolean leavesInCheck(ChessMove move, Board board) {
        Board resultingBoard = move.applyMove(board);
        for (Position attack : getOpponentAttacks(resultingBoard)) {
            if (resultingBoard.getKingPosition() == attack) {
                return true;
            }
        }
        return false;
    }

    protected List<ChessMove> getAllPossibleMoves(Board board) {
        List<ChessMove> possibleMoves = new LinkedList<>();
        board.getCurrentPieces().entrySet().forEach(piecePosition -> {
            var piece = piecePosition.getValue();
            var position = piecePosition.getKey();
            switch (piece) {
            case PAWN:
                possibleMoves.addAll(allPawnMoves.get(position));
                break;
            case BISHOP:
                possibleMoves.addAll(allBishopMoves.get(position));
                break;
            case KNIGHT:
                possibleMoves.addAll(allKnightMoves.get(position));
                break;
            case ROOK:
                possibleMoves.addAll(allRookMoves.get(position));
                break;
            case QUEEN:
                possibleMoves.addAll(allQueenMoves.get(position));
                break;
            default:
                throw new RuntimeException("unexpected piece");
            }
        });
        possibleMoves.addAll(allKingMoves.get(board.getKingPosition()));
        return possibleMoves;
    }

    private List<Position> getOpponentAttacks(Board board) {
        Board invertedBoard = positionInverter.invertBoard(board);
        // @formatter:off
        return getAllPossibleMoves(invertedBoard).stream()
                .filter(ChessMove::isAttackingMove)
                .filter(move -> !isPathBlocked(move, invertedBoard, new LinkedList<>()))
                .map(positionInverter::invertChessMove)
                .map(ChessMove::getFinalPosition)
                .collect(Collectors.toList());
        // @formatter:on
    }

    public List<ChessMove> getPossibleMoves(Board board, CastlingRights currentPlayerRights, Position enPassant) {
        List<ChessMove> possibleMoves = getAllPossibleMoves(board);
        List<Position> opponentAttacks = getOpponentAttacks(board);
        // @formatter:off
        return possibleMoves.stream()
                .filter(move -> move.hasCastlingRights(currentPlayerRights))
                .filter(move -> !isPathBlocked(move, board, opponentAttacks))
                .filter(move -> !isFinalPositionBlocked(move, board, enPassant))
                .filter(move -> !leavesInCheck(move, board))
                .collect(Collectors.toList());
        // @formatter:on
    }

    private static Map<Position, Collection<ChessMove>> generateAllPawnAdvances() {
        var pawnAdvances = new EnumMap<Position, Collection<ChessMove>>(Position.class);

        // @formatter:off
        // 2nd Rank:
        pawnAdvances.put(A2,
                Arrays.asList(
                        new PawnAdvance(A2, A3),
                        new PawnAdvance(A2, A3, A4)));
        pawnAdvances.put(B2,
                Arrays.asList(
                        new PawnAdvance(B2, B3),
                        new PawnAdvance(B2, B3, B4)));
        pawnAdvances.put(C2,
                Arrays.asList(
                        new PawnAdvance(C2, C3),
                        new PawnAdvance(C2, C3, C4)));
        pawnAdvances.put(D2,
                Arrays.asList(
                        new PawnAdvance(D2, D3),
                        new PawnAdvance(D2, D3, D4)));
        pawnAdvances.put(E2,
                Arrays.asList(
                        new PawnAdvance(E2, E3),
                        new PawnAdvance(E2, E3, E4)));
        pawnAdvances.put(F2,
                Arrays.asList(
                        new PawnAdvance(F2, F3),
                        new PawnAdvance(F2, F3, F4)));
        pawnAdvances.put(G2,
                Arrays.asList(
                        new PawnAdvance(G2, G3),
                        new PawnAdvance(G2, G3, G4)));
        pawnAdvances.put(H2,
                Arrays.asList(
                        new PawnAdvance(H2, H3),
                        new PawnAdvance(H2, H3, H4)));

        // 3rd Rank:
        pawnAdvances.put(A3,
                Arrays.asList(
                        new PawnAdvance(A3, A4)));
        pawnAdvances.put(B3,
                Arrays.asList(
                        new PawnAdvance(B3, B4)));
        pawnAdvances.put(C3,
                Arrays.asList(
                        new PawnAdvance(C3, C4)));
        pawnAdvances.put(D3,
                Arrays.asList(
                        new PawnAdvance(D3, D4)));
        pawnAdvances.put(E3,
                Arrays.asList(
                        new PawnAdvance(E3, E4)));
        pawnAdvances.put(F3,
                Arrays.asList(
                        new PawnAdvance(F3, F4)));
        pawnAdvances.put(G3,
                Arrays.asList(
                        new PawnAdvance(G3, G4)));
        pawnAdvances.put(H3,
                Arrays.asList(
                        new PawnAdvance(H3, H4)));

        // 4th Rank:
        pawnAdvances.put(A4,
                Arrays.asList(
                        new PawnAdvance(A4, A5)));
        pawnAdvances.put(B4,
                Arrays.asList(
                        new PawnAdvance(B4, B5)));
        pawnAdvances.put(C4,
                Arrays.asList(
                        new PawnAdvance(C4, C5)));
        pawnAdvances.put(D4,
                Arrays.asList(
                        new PawnAdvance(D4, D5)));
        pawnAdvances.put(E4,
                Arrays.asList(
                        new PawnAdvance(E4, E5)));
        pawnAdvances.put(F4,
                Arrays.asList(
                        new PawnAdvance(F4, F5)));
        pawnAdvances.put(G4,
                Arrays.asList(
                        new PawnAdvance(G4, G5)));
        pawnAdvances.put(H4,
                Arrays.asList(
                        new PawnAdvance(H4, H5)));

        // 5th Rank:
        pawnAdvances.put(A5,
                Arrays.asList(
                        new PawnAdvance(A5, A6)));
        pawnAdvances.put(B5,
                Arrays.asList(
                        new PawnAdvance(B5, B6)));
        pawnAdvances.put(C5,
                Arrays.asList(
                        new PawnAdvance(C5, C6)));
        pawnAdvances.put(D5,
                Arrays.asList(
                        new PawnAdvance(D5, D6)));
        pawnAdvances.put(E5,
                Arrays.asList(
                        new PawnAdvance(E5, E6)));
        pawnAdvances.put(F5,
                Arrays.asList(
                        new PawnAdvance(F5, F6)));
        pawnAdvances.put(G5,
                Arrays.asList(
                        new PawnAdvance(G5, G6)));
        pawnAdvances.put(H5,
                Arrays.asList(
                        new PawnAdvance(H5, H6)));

        // 6th Rank:
        pawnAdvances.put(A6,
                Arrays.asList(
                        new PawnAdvance(A6, A7)));
        pawnAdvances.put(B6,
                Arrays.asList(
                        new PawnAdvance(B6, B7)));
        pawnAdvances.put(C6,
                Arrays.asList(
                        new PawnAdvance(C6, C7)));
        pawnAdvances.put(D6,
                Arrays.asList(
                        new PawnAdvance(D6, D7)));
        pawnAdvances.put(E6,
                Arrays.asList(
                        new PawnAdvance(E6, E7)));
        pawnAdvances.put(F6,
                Arrays.asList(
                        new PawnAdvance(F6, F7)));
        pawnAdvances.put(G6,
                Arrays.asList(
                        new PawnAdvance(G6, G7)));
        pawnAdvances.put(H6,
                Arrays.asList(
                        new PawnAdvance(H6, H7)));

        // 7th Rank (promotions:
        pawnAdvances.put(A7,
                Arrays.asList(
                        new PawnAdvance(Piece.BISHOP, A7, A8),
                        new PawnAdvance(Piece.KNIGHT, A7, A8),
                        new PawnAdvance(Piece.ROOK,   A7, A8),
                        new PawnAdvance(Piece.QUEEN,  A7, A8)));
        pawnAdvances.put(B7,
                Arrays.asList(
                        new PawnAdvance(Piece.BISHOP, B7, B8),
                        new PawnAdvance(Piece.KNIGHT, B7, B8),
                        new PawnAdvance(Piece.ROOK,   B7, B8),
                        new PawnAdvance(Piece.QUEEN,  B7, B8)));
        pawnAdvances.put(C7,
                Arrays.asList(
                        new PawnAdvance(Piece.BISHOP, C7, C8),
                        new PawnAdvance(Piece.KNIGHT, C7, C8),
                        new PawnAdvance(Piece.ROOK,   C7, C8),
                        new PawnAdvance(Piece.QUEEN,  C7, C8)));
        pawnAdvances.put(D7,
                Arrays.asList(
                        new PawnAdvance(Piece.BISHOP, D7, D8),
                        new PawnAdvance(Piece.KNIGHT, D7, D8),
                        new PawnAdvance(Piece.ROOK,   D7, D8),
                        new PawnAdvance(Piece.QUEEN,  D7, D8)));
        pawnAdvances.put(E7,
                Arrays.asList(
                        new PawnAdvance(Piece.BISHOP, E7, E8),
                        new PawnAdvance(Piece.KNIGHT, E7, E8),
                        new PawnAdvance(Piece.ROOK,   E7, E8),
                        new PawnAdvance(Piece.QUEEN,  E7, E8)));
        pawnAdvances.put(F7,
                Arrays.asList(
                        new PawnAdvance(Piece.BISHOP, F7, F8),
                        new PawnAdvance(Piece.KNIGHT, F7, F8),
                        new PawnAdvance(Piece.ROOK,   F7, F8),
                        new PawnAdvance(Piece.QUEEN,  F7, F8)));
        pawnAdvances.put(G7,
                Arrays.asList(
                        new PawnAdvance(Piece.BISHOP, G7, G8),
                        new PawnAdvance(Piece.KNIGHT, G7, G8),
                        new PawnAdvance(Piece.ROOK,   G7, G8),
                        new PawnAdvance(Piece.QUEEN,  G7, G8)));
        pawnAdvances.put(H7,
                Arrays.asList(
                        new PawnAdvance(Piece.BISHOP, H7, H8),
                        new PawnAdvance(Piece.KNIGHT, H7, H8),
                        new PawnAdvance(Piece.ROOK,   H7, H8),
                        new PawnAdvance(Piece.QUEEN,  H7, H8)));
        // @formatter:on
        return pawnAdvances;
    }

    private static Map<Position, Collection<ChessMove>> generateAllPawnCaptures() {
        var pawnCaptures = new EnumMap<Position, Collection<ChessMove>>(Position.class);

        // @formatter:off
        // 2nd Rank:
        pawnCaptures.put(A2,
                Arrays.asList(
                        new PawnCapture(A2, B3)));
        pawnCaptures.put(B2,
                Arrays.asList(
                        new PawnCapture(B2, A3),
                        new PawnCapture(B2, C3)));
        pawnCaptures.put(C2,
                Arrays.asList(
                        new PawnCapture(C2, B3),
                        new PawnCapture(C2, D3)));
        pawnCaptures.put(D2,
                Arrays.asList(
                        new PawnCapture(D2, C3),
                        new PawnCapture(D2, E3)));
        pawnCaptures.put(E2,
                Arrays.asList(
                        new PawnCapture(E2, D3),
                        new PawnCapture(E2, F3)));
        pawnCaptures.put(F2,
                Arrays.asList(
                        new PawnCapture(F2, E3),
                        new PawnCapture(F2, G3)));
        pawnCaptures.put(G2,
                Arrays.asList(
                        new PawnCapture(G2, F3),
                        new PawnCapture(G2, H3)));
        pawnCaptures.put(H2,
                Arrays.asList(
                        new PawnCapture(H2, G3)));

        // 3rd Rank:
        pawnCaptures.put(A3,
                Arrays.asList(
                        new PawnCapture(A3, B4)));
        pawnCaptures.put(B3,
                Arrays.asList(
                        new PawnCapture(B3, A4),
                        new PawnCapture(B3, C4)));
        pawnCaptures.put(C3,
                Arrays.asList(
                        new PawnCapture(C3, B4),
                        new PawnCapture(C3, D4)));
        pawnCaptures.put(D3,
                Arrays.asList(
                        new PawnCapture(D3, C4),
                        new PawnCapture(D3, E4)));
        pawnCaptures.put(E3,
                Arrays.asList(
                        new PawnCapture(E3, D4),
                        new PawnCapture(E3, F4)));
        pawnCaptures.put(F3,
                Arrays.asList(
                        new PawnCapture(F3, E4),
                        new PawnCapture(F3, G4)));
        pawnCaptures.put(G3,
                Arrays.asList(
                        new PawnCapture(G3, F4),
                        new PawnCapture(G3, H4)));
        pawnCaptures.put(H3,
                Arrays.asList(
                        new PawnCapture(H3, G4)));

        // 4th Rank:
        pawnCaptures.put(A4,
                Arrays.asList(
                        new PawnCapture(A4, B5)));
        pawnCaptures.put(B4,
                Arrays.asList(
                        new PawnCapture(B4, A5),
                        new PawnCapture(B4, C5)));
        pawnCaptures.put(C4,
                Arrays.asList(
                        new PawnCapture(C4, B5),
                        new PawnCapture(C4, D5)));
        pawnCaptures.put(D4,
                Arrays.asList(
                        new PawnCapture(D4, C5),
                        new PawnCapture(D4, E5)));
        pawnCaptures.put(E4,
                Arrays.asList(
                        new PawnCapture(E4, D5),
                        new PawnCapture(E4, F5)));
        pawnCaptures.put(F4,
                Arrays.asList(
                        new PawnCapture(F4, E5),
                        new PawnCapture(F4, G5)));
        pawnCaptures.put(G4,
                Arrays.asList(
                        new PawnCapture(G4, F5),
                        new PawnCapture(G4, H5)));
        pawnCaptures.put(H4,
                Arrays.asList(
                        new PawnCapture(H4, G5)));

        // 5th Rank:
        pawnCaptures.put(A5,
                Arrays.asList(
                        new PawnCapture(A5, B6),
                        new EnPassantCapture(B5, A5, B6)));
        pawnCaptures.put(B5,
                Arrays.asList(
                        new PawnCapture(B5, A6),
                        new PawnCapture(B5, C6),
                        new EnPassantCapture(A5, B5, A6),
                        new EnPassantCapture(C5, B5, C6)));
        pawnCaptures.put(C5,
                Arrays.asList(
                        new PawnCapture(C5, B6),
                        new PawnCapture(C5, D6),
                        new EnPassantCapture(B5, C5, B6),
                        new EnPassantCapture(D5, C5, D6)));
        pawnCaptures.put(D5,
                Arrays.asList(
                        new PawnCapture(D5, C6),
                        new PawnCapture(D5, E6),
                        new EnPassantCapture(C5, D5, C6),
                        new EnPassantCapture(E5, D5, E6)));
        pawnCaptures.put(E5,
                Arrays.asList(
                        new PawnCapture(E5, D6),
                        new PawnCapture(E5, F6),
                        new EnPassantCapture(D5, E5, D6),
                        new EnPassantCapture(F5, E5, F6)));
        pawnCaptures.put(F5,
                Arrays.asList(
                        new PawnCapture(F5, E6),
                        new PawnCapture(F5, G6),
                        new EnPassantCapture(E5, F5, E6),
                        new EnPassantCapture(G5, F5, G6)));
        pawnCaptures.put(G5,
                Arrays.asList(
                        new PawnCapture(G5, F6),
                        new PawnCapture(G5, H6),
                        new EnPassantCapture(F5, G5, F6),
                        new EnPassantCapture(H5, G5, H6)));
        pawnCaptures.put(H5,
                Arrays.asList(
                        new PawnCapture(H5, G6),
                        new EnPassantCapture(G5, H5, G6)));

        // 6th Rank:
        pawnCaptures.put(A6,
                Arrays.asList(
                        new PawnCapture(A6, B7)));
        pawnCaptures.put(B6,
                Arrays.asList(
                        new PawnCapture(B6, A7),
                        new PawnCapture(B6, C7)));
        pawnCaptures.put(C6,
                Arrays.asList(
                        new PawnCapture(C6, B7),
                        new PawnCapture(C6, D7)));
        pawnCaptures.put(D6,
                Arrays.asList(
                        new PawnCapture(D6, C7),
                        new PawnCapture(D6, E7)));
        pawnCaptures.put(E6,
                Arrays.asList(
                        new PawnCapture(E6, D7),
                        new PawnCapture(E6, F7)));
        pawnCaptures.put(F6,
                Arrays.asList(
                        new PawnCapture(F6, E7),
                        new PawnCapture(F6, G7)));
        pawnCaptures.put(G6,
                Arrays.asList(
                        new PawnCapture(G6, F7),
                        new PawnCapture(G6, H7)));
        pawnCaptures.put(H6,
                Arrays.asList(
                        new PawnCapture(H6, G7)));

        // 7th Rank:
        pawnCaptures.put(A7,
                Arrays.asList(
                        new PawnCapture(Piece.BISHOP, A7, B8),
                        new PawnCapture(Piece.KNIGHT, A7, B8),
                        new PawnCapture(Piece.ROOK,   A7, B8),
                        new PawnCapture(Piece.QUEEN,  A7, B8)));
        pawnCaptures.put(B7,
                Arrays.asList(
                        new PawnCapture(Piece.BISHOP, B7, A8),
                        new PawnCapture(Piece.KNIGHT, B7, A8),
                        new PawnCapture(Piece.ROOK,   B7, A8),
                        new PawnCapture(Piece.QUEEN,  B7, A8),
                        new PawnCapture(Piece.BISHOP, B7, C8),
                        new PawnCapture(Piece.KNIGHT, B7, C8),
                        new PawnCapture(Piece.ROOK,   B7, C8),
                        new PawnCapture(Piece.QUEEN,  B7, C8)));
        pawnCaptures.put(C7,
                Arrays.asList(
                        new PawnCapture(Piece.BISHOP, C7, B8),
                        new PawnCapture(Piece.KNIGHT, C7, B8),
                        new PawnCapture(Piece.ROOK,   C7, B8),
                        new PawnCapture(Piece.QUEEN,  C7, B8),
                        new PawnCapture(Piece.BISHOP, C7, D8),
                        new PawnCapture(Piece.KNIGHT, C7, D8),
                        new PawnCapture(Piece.ROOK,   C7, D8),
                        new PawnCapture(Piece.QUEEN,  C7, D8)));
        pawnCaptures.put(D7,
                Arrays.asList(
                        new PawnCapture(Piece.BISHOP, D7, C8),
                        new PawnCapture(Piece.KNIGHT, D7, C8),
                        new PawnCapture(Piece.ROOK,   D7, C8),
                        new PawnCapture(Piece.QUEEN,  D7, C8),
                        new PawnCapture(Piece.BISHOP, D7, E8),
                        new PawnCapture(Piece.KNIGHT, D7, E8),
                        new PawnCapture(Piece.ROOK,   D7, E8),
                        new PawnCapture(Piece.QUEEN,  D7, E8)));
        pawnCaptures.put(E7,
                Arrays.asList(
                        new PawnCapture(Piece.BISHOP, E7, D8),
                        new PawnCapture(Piece.KNIGHT, E7, D8),
                        new PawnCapture(Piece.ROOK,   E7, D8),
                        new PawnCapture(Piece.QUEEN,  E7, D8),
                        new PawnCapture(Piece.BISHOP, E7, F8),
                        new PawnCapture(Piece.KNIGHT, E7, F8),
                        new PawnCapture(Piece.ROOK,   E7, F8),
                        new PawnCapture(Piece.QUEEN,  E7, F8)));
        pawnCaptures.put(F7,
                Arrays.asList(
                        new PawnCapture(Piece.BISHOP, F7, E8),
                        new PawnCapture(Piece.KNIGHT, F7, E8),
                        new PawnCapture(Piece.ROOK,   F7, E8),
                        new PawnCapture(Piece.QUEEN,  F7, E8),
                        new PawnCapture(Piece.BISHOP, F7, G8),
                        new PawnCapture(Piece.KNIGHT, F7, G8),
                        new PawnCapture(Piece.ROOK,   F7, G8),
                        new PawnCapture(Piece.QUEEN,  F7, G8)));
        pawnCaptures.put(G7,
                Arrays.asList(
                        new PawnCapture(Piece.BISHOP, G7, F8),
                        new PawnCapture(Piece.KNIGHT, G7, F8),
                        new PawnCapture(Piece.ROOK,   G7, F8),
                        new PawnCapture(Piece.QUEEN,  G7, F8),
                        new PawnCapture(Piece.BISHOP, G7, H8),
                        new PawnCapture(Piece.KNIGHT, G7, H8),
                        new PawnCapture(Piece.ROOK,   G7, H8),
                        new PawnCapture(Piece.QUEEN,  G7, H8)));
        pawnCaptures.put(H7,
                Arrays.asList(
                        new PawnCapture(Piece.BISHOP, H7, G8),
                        new PawnCapture(Piece.KNIGHT, H7, G8),
                        new PawnCapture(Piece.ROOK,   H7, G8),
                        new PawnCapture(Piece.QUEEN,  H7, G8)));
        // @formatter:on
        return pawnCaptures;
    }

    private static Map<Position, Collection<ChessMove>> generateAllPawnMoves() {
        var pawnAdvances = generateAllPawnAdvances();
        var pawnCaptures = generateAllPawnCaptures();
        var allPawnMoves = new EnumMap<Position, Collection<ChessMove>>(Position.class);
        var advancePositions = pawnAdvances.keySet();
        for (Position position : advancePositions) {
            List<ChessMove> moves = new LinkedList<>();
            moves.addAll(pawnAdvances.get(position));
            moves.addAll(pawnCaptures.get(position));
            allPawnMoves.put(position, moves);
        }
        return allPawnMoves;
    }

    private static void initKnightMovesFrom(Map<Position, Collection<ChessMove>> knightMoves, Position startPosition,
            Position... endPositions) {
        knightMoves.put(startPosition, Arrays.asList(endPositions).stream()
                .map(endPosition -> new KnightMove(startPosition, endPosition)).collect(Collectors.toList()));
    }

    private static Map<Position, Collection<ChessMove>> generateAllKnightMoves() {
        var knightMoves = new EnumMap<Position, Collection<ChessMove>>(Position.class);
        // @formatter:off
        initKnightMovesFrom(knightMoves, A1,                     B3,     C2);
        initKnightMovesFrom(knightMoves, A2,                     B4, C1, C3);
        initKnightMovesFrom(knightMoves, A3,                 B1, B5, C2, C4);
        initKnightMovesFrom(knightMoves, A4,                 B2, B6, C3, C5);
        initKnightMovesFrom(knightMoves, A5,                 B3, B7, C4, C6);
        initKnightMovesFrom(knightMoves, A6,                 B4, B8, C5, C7);
        initKnightMovesFrom(knightMoves, A7,                 B5,     C6, C8);
        initKnightMovesFrom(knightMoves, A8,                 B6,     C7    );

        initKnightMovesFrom(knightMoves, B1,             A3,     C3,     D2);
        initKnightMovesFrom(knightMoves, B2,             A4,     C4, D1, D3);
        initKnightMovesFrom(knightMoves, B3,         A1, A5, C1, C5, D2, D4);
        initKnightMovesFrom(knightMoves, B4,         A2, A6, C2, C6, D3, D5);
        initKnightMovesFrom(knightMoves, B5,         A3, A7, C3, C7, D4, D6);
        initKnightMovesFrom(knightMoves, B6,         A4, A8, C4, C8, D5, D7);
        initKnightMovesFrom(knightMoves, B7,         A5,     C5,     D6, D8);
        initKnightMovesFrom(knightMoves, B8,         A6,     C6,     D7    );

        initKnightMovesFrom(knightMoves, C1,     A2,     B3,     D3,     E2);
        initKnightMovesFrom(knightMoves, C2, A1, A3,     B4,     D4, E1, E3);
        initKnightMovesFrom(knightMoves, C3, A2, A4, B1, B5, D1, D5, E2, E4);
        initKnightMovesFrom(knightMoves, C4, A3, A5, B2, B6, D2, D6, E3, E5);
        initKnightMovesFrom(knightMoves, C5, A4, A6, B3, B7, D3, D7, E4, E6);
        initKnightMovesFrom(knightMoves, C6, A5, A7, B4, B8, D4, D8, E5, E7);
        initKnightMovesFrom(knightMoves, C7, A6, A8, B5,     D5,     E6, E8);
        initKnightMovesFrom(knightMoves, C8, A7,     B6,     D6,     E7    );

        initKnightMovesFrom(knightMoves, D1,     B2,     C3,     E3,     F2);
        initKnightMovesFrom(knightMoves, D2, B1, B3,     C4,     E4, F1, F3);
        initKnightMovesFrom(knightMoves, D3, B2, B4, C1, C5, E1, E5, F2, F4);
        initKnightMovesFrom(knightMoves, D4, B3, B5, C2, C6, E2, E6, F3, F5);
        initKnightMovesFrom(knightMoves, D5, B4, B6, C3, C7, E3, E7, F4, F6);
        initKnightMovesFrom(knightMoves, D6, B5, B7, C4, C8, E4, E8, F5, F7);
        initKnightMovesFrom(knightMoves, D7, B6, B8, C5,     E5,     F6, F8);
        initKnightMovesFrom(knightMoves, D8, B7,     C6,     E6,     F7    );

        initKnightMovesFrom(knightMoves, E1,     C2,     D3,     F3,     G2);
        initKnightMovesFrom(knightMoves, E2, C1, C3,     D4,     F4, G1, G3);
        initKnightMovesFrom(knightMoves, E3, C2, C4, D1, D5, F1, F5, G2, G4);
        initKnightMovesFrom(knightMoves, E4, C3, C5, D2, D6, F2, F6, G3, G5);
        initKnightMovesFrom(knightMoves, E5, C4, C6, D3, D7, F3, F7, G4, G6);
        initKnightMovesFrom(knightMoves, E6, C5, C7, D4, D8, F4, F8, G5, G7);
        initKnightMovesFrom(knightMoves, E7, C6, C8, D5,     F5,     G6, G8);
        initKnightMovesFrom(knightMoves, E8, C7,     D6,     F6,     G7    );

        initKnightMovesFrom(knightMoves, F1,     D2,     E3,     G3,     H2);
        initKnightMovesFrom(knightMoves, F2, D1, D3,     E4,     G4, H1, H3);
        initKnightMovesFrom(knightMoves, F3, D2, D4, E1, E5, G1, G5, H2, H4);
        initKnightMovesFrom(knightMoves, F4, D3, D5, E2, E6, G2, G6, H3, H5);
        initKnightMovesFrom(knightMoves, F5, D4, D6, E3, E7, G3, G7, H4, H6);
        initKnightMovesFrom(knightMoves, F6, D5, D7, E4, E8, G4, G8, H5, H7);
        initKnightMovesFrom(knightMoves, F7, D6, D8, E5,     G5,     H6, H8);
        initKnightMovesFrom(knightMoves, F8, D7,     E6,     G6,     H7    );

        initKnightMovesFrom(knightMoves, G1,     E2,     F3,     H3        );
        initKnightMovesFrom(knightMoves, G2, E1, E3,     F4,     H4        );
        initKnightMovesFrom(knightMoves, G3, E2, E4, F1, F5, H1, H5        );
        initKnightMovesFrom(knightMoves, G4, E3, E5, F2, F6, H2, H6        );
        initKnightMovesFrom(knightMoves, G5, E4, E6, F3, F7, H3, H7        );
        initKnightMovesFrom(knightMoves, G6, E5, E7, F4, F8, H4, H8        );
        initKnightMovesFrom(knightMoves, G7, E6, E8, F5,     H5            );
        initKnightMovesFrom(knightMoves, G8, E7,     F6,     H6            );

        initKnightMovesFrom(knightMoves, H1,     F2,     G3                );
        initKnightMovesFrom(knightMoves, H2, F1, F3,     G4                );
        initKnightMovesFrom(knightMoves, H3, F2, F4, G1, G5                );
        initKnightMovesFrom(knightMoves, H4, F3, F5, G2, G6                );
        initKnightMovesFrom(knightMoves, H5, F4, F6, G3, G7                );
        initKnightMovesFrom(knightMoves, H6, F5, F7, G4, G8                );
        initKnightMovesFrom(knightMoves, H7, F6, F8, G5                    );
        initKnightMovesFrom(knightMoves, H8, F7,     G6                    );
        // @formatter:on
        return knightMoves;
    }

    private static Map<Position, Collection<ChessMove>> generateAllBishopMoves(
            DiagonalMoveGenerator diagonalMoveGenerator) {
        var bishopMoves = new EnumMap<Position, Collection<ChessMove>>(Position.class);
        for (Position startingPosition : Position.values()) {
            bishopMoves.put(startingPosition, diagonalMoveGenerator.generateMoves(startingPosition, BishopMove::new));
        }
        return bishopMoves;
    }

    private static Map<Position, Collection<ChessMove>> generateAllRookMoves(
            StraightMoveGenerator straightMoveGenerator) {
        var rookMoves = new EnumMap<Position, Collection<ChessMove>>(Position.class);
        for (Position startingPosition : Position.values()) {
            rookMoves.put(startingPosition, straightMoveGenerator.generateMoves(startingPosition, RookMove::new));
        }
        return rookMoves;
    }

    private static Map<Position, Collection<ChessMove>> generateAllQueenMoves(
            StraightMoveGenerator straightMoveGenerator, DiagonalMoveGenerator diagonalMoveGenerator) {
        var queenMoves = new EnumMap<Position, Collection<ChessMove>>(Position.class);
        for (Position startingPosition : Position.values()) {
            Collection<ChessMove> moves = straightMoveGenerator.generateMoves(startingPosition, QueenMove::new);
            moves.addAll(diagonalMoveGenerator.generateMoves(startingPosition, QueenMove::new));
            queenMoves.put(startingPosition, moves);
        }
        return queenMoves;
    }

    private static Map<Position, Collection<ChessMove>> generateAllKingMoves(
            StraightMoveGenerator straightMoveGenerator, DiagonalMoveGenerator diagonalMoveGenerator) {
        var kingMoves = new EnumMap<Position, Collection<ChessMove>>(Position.class);
        for (Position startingPosition : Position.values()) {
            Collection<ChessMove> moves = straightMoveGenerator.generateMoves(startingPosition, KingMove::new);
            moves.addAll(diagonalMoveGenerator.generateMoves(startingPosition, KingMove::new));
            moves = moves.stream().filter(move -> move.getPath().length == 2)
                    .collect(Collectors.toCollection(LinkedList::new));
            if (startingPosition == E1) {
                moves.add(new KingSideCastle());
                moves.add(new QueenSideCastle());
            }
            kingMoves.put(startingPosition, moves);
        }
        return kingMoves;
    }
}
