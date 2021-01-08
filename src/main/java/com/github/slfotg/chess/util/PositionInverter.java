package com.github.slfotg.chess.util;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.slfotg.chess.Board;
import com.github.slfotg.chess.enums.CastlingRights;
import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;
import com.github.slfotg.chess.move.ChessMove;

public class PositionInverter {

    private final Map<Position, Position> mirrorMap;

    public PositionInverter() {
        mirrorMap = new EnumMap<>(Position.class);
        mirrorMap.put(Position.A1, Position.A8);
        mirrorMap.put(Position.A2, Position.A7);
        mirrorMap.put(Position.A3, Position.A6);
        mirrorMap.put(Position.A4, Position.A5);
        mirrorMap.put(Position.A5, Position.A4);
        mirrorMap.put(Position.A6, Position.A3);
        mirrorMap.put(Position.A7, Position.A2);
        mirrorMap.put(Position.A8, Position.A1);

        mirrorMap.put(Position.B1, Position.B8);
        mirrorMap.put(Position.B2, Position.B7);
        mirrorMap.put(Position.B3, Position.B6);
        mirrorMap.put(Position.B4, Position.B5);
        mirrorMap.put(Position.B5, Position.B4);
        mirrorMap.put(Position.B6, Position.B3);
        mirrorMap.put(Position.B7, Position.B2);
        mirrorMap.put(Position.B8, Position.B1);

        mirrorMap.put(Position.C1, Position.C8);
        mirrorMap.put(Position.C2, Position.C7);
        mirrorMap.put(Position.C3, Position.C6);
        mirrorMap.put(Position.C4, Position.C5);
        mirrorMap.put(Position.C5, Position.C4);
        mirrorMap.put(Position.C6, Position.C3);
        mirrorMap.put(Position.C7, Position.C2);
        mirrorMap.put(Position.C8, Position.C1);

        mirrorMap.put(Position.D1, Position.D8);
        mirrorMap.put(Position.D2, Position.D7);
        mirrorMap.put(Position.D3, Position.D6);
        mirrorMap.put(Position.D4, Position.D5);
        mirrorMap.put(Position.D5, Position.D4);
        mirrorMap.put(Position.D6, Position.D3);
        mirrorMap.put(Position.D7, Position.D2);
        mirrorMap.put(Position.D8, Position.D1);

        mirrorMap.put(Position.E1, Position.E8);
        mirrorMap.put(Position.E2, Position.E7);
        mirrorMap.put(Position.E3, Position.E6);
        mirrorMap.put(Position.E4, Position.E5);
        mirrorMap.put(Position.E5, Position.E4);
        mirrorMap.put(Position.E6, Position.E3);
        mirrorMap.put(Position.E7, Position.E2);
        mirrorMap.put(Position.E8, Position.E1);

        mirrorMap.put(Position.F1, Position.F8);
        mirrorMap.put(Position.F2, Position.F7);
        mirrorMap.put(Position.F3, Position.F6);
        mirrorMap.put(Position.F4, Position.F5);
        mirrorMap.put(Position.F5, Position.F4);
        mirrorMap.put(Position.F6, Position.F3);
        mirrorMap.put(Position.F7, Position.F2);
        mirrorMap.put(Position.F8, Position.F1);

        mirrorMap.put(Position.G1, Position.G8);
        mirrorMap.put(Position.G2, Position.G7);
        mirrorMap.put(Position.G3, Position.G6);
        mirrorMap.put(Position.G4, Position.G5);
        mirrorMap.put(Position.G5, Position.G4);
        mirrorMap.put(Position.G6, Position.G3);
        mirrorMap.put(Position.G7, Position.G2);
        mirrorMap.put(Position.G8, Position.G1);

        mirrorMap.put(Position.H1, Position.H8);
        mirrorMap.put(Position.H2, Position.H7);
        mirrorMap.put(Position.H3, Position.H6);
        mirrorMap.put(Position.H4, Position.H5);
        mirrorMap.put(Position.H5, Position.H4);
        mirrorMap.put(Position.H6, Position.H3);
        mirrorMap.put(Position.H7, Position.H2);
        mirrorMap.put(Position.H8, Position.H1);
    }

    public Position invertPosition(Position position) {
        return mirrorMap.get(position);
    }

    public List<Position> invertPositions(List<Position> positions) {
        return positions.stream().map(this::invertPosition).collect(Collectors.toList());
    }

    public Board invertBoard(Board board) {
        var mirrorCurrentPieces = new EnumMap<Position, Piece>(Position.class);
        board.getOpponentPieces()
                .forEach((position, piece) -> mirrorCurrentPieces.put(invertPosition(position), piece));
        var mirrorOpponentPieces = new EnumMap<Position, Piece>(Position.class);
        board.getCurrentPieces()
                .forEach((position, piece) -> mirrorOpponentPieces.put(invertPosition(position), piece));
        return new Board(mirrorCurrentPieces, mirrorOpponentPieces, invertPosition(board.getOpponentKingPosition()),
                invertPosition(board.getKingPosition()));
    }

    public ChessMove invertChessMove(ChessMove chessMove) {
        return new InvertedChessMove(this, chessMove);
    }

    private static class InvertedChessMove implements ChessMove {

        private final PositionInverter positionInverter;
        private final ChessMove chessMove;

        public InvertedChessMove(PositionInverter positionInverter, ChessMove chessMove) {
            this.positionInverter = positionInverter;
            this.chessMove = chessMove;
        }

        @Override
        public Piece getPiece() {
            return chessMove.getPiece();
        }

        @Override
        public Position[] getPath() {
            Position[] originalPath = chessMove.getPath();
            Position[] path = new Position[originalPath.length];
            for (int i = 0; i < originalPath.length; i += 1) {
                path[i] = positionInverter.invertPosition(originalPath[i]);
            }
            return path;
        }

        @Override
        public Board applyMove(Board currentBoard) {
            return positionInverter.invertBoard(chessMove.applyMove(positionInverter.invertBoard(currentBoard)));
        }

        @Override
        public CastlingRights updateCastlingRights(CastlingRights currentPlayerRights) {
            return chessMove.updateCastlingRights(currentPlayerRights);
        }

        @Override
        public boolean hasCastlingRights(CastlingRights currentPlayerRights) {
            return chessMove.hasCastlingRights(currentPlayerRights);
        }

        @Override
        public boolean isAttackingMove() {
            return chessMove.isAttackingMove();
        }

        @Override
        public boolean isPawnMove() {
            return chessMove.isPawnMove();
        }

        @Override
        public Optional<List<Position>> getCastlingPositions() {
            return chessMove.getCastlingPositions().map(positionInverter::invertPositions);
        }

        @Override
        public Optional<Position> enPassantPosition() {
            return chessMove.enPassantPosition().map(positionInverter::invertPosition);
        }

        @Override
        public Optional<Position> getAttackedPosition() {
            return chessMove.getAttackedPosition().map(positionInverter::invertPosition);
        }

        @Override
        public String toString() {
            if (chessMove.toString().startsWith("0")) {
                return chessMove.toString();
            }
            return stringValue();
        }
    }
}
