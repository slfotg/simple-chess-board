package com.github.slfotg.chess.move;

import org.junit.jupiter.api.Test;

import com.github.slfotg.chess.Board;
import com.github.slfotg.chess.enums.CastlingRights;
import com.github.slfotg.chess.move.ChessMoveGenerator;

class MoveGeneratorTest {

    @Test
    void testInitialMoves() {
        Board board = Board.initialBoard();
        ChessMoveGenerator generator = new ChessMoveGenerator();

        var moves = generator.getPossibleMoves(board, CastlingRights.EITHER_SIDE, null);
        System.out.println(moves.size());
        moves.forEach(System.out::println);
    }
}
