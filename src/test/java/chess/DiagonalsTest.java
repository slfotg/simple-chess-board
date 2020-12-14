package chess;

import org.junit.jupiter.api.Test;

import com.github.slfotg.chess.enums.Position;
import com.github.slfotg.chess.move.util.Diagonals;

class DiagonalsTest {

    Diagonals diagonals = new Diagonals();

    @Test
    void testArrays() throws Exception {
        System.out.println(diagonals.diagonalsFrom(Position.A8));
        System.out.println(diagonals.diagonalsFrom(Position.C3));
    }
}
