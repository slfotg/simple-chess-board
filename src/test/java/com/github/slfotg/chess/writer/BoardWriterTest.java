package com.github.slfotg.chess.writer;

import org.junit.jupiter.api.Test;

import com.github.slfotg.chess.Board;

class BoardWriterTest {

    @Test
    void testWriteBoard() {
        BoardWriter writer = new BoardWriter();
        writer.writeBoard(Board.initialBoard());
    }
}
