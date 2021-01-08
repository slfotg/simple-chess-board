package com.github.slfotg.chess;

import java.util.Random;

import com.github.slfotg.chess.move.ChessMove;
import com.github.slfotg.chess.writer.BoardWriter;

public class RandomChessGame {

    private static Random random = new Random();

    public static void main(String... args) {
        BoardWriter writer = new BoardWriter();
        ChessGame game;
        for (game = ChessGame.newGame(); game.getResult().isEmpty(); game = nextRandomGame(game)) {
            writer.writeBoard(game.getBoard());
        }
        writer.writeBoard(game.getBoard());
        System.out.println(game.getResult().get());
    }

    static ChessGame nextRandomGame(ChessGame game) {
        ChessMove[] nextMoves = game.getPossibleMoves().toArray(new ChessMove[0]);
        ChessMove randomMove = nextMoves[random.nextInt(nextMoves.length)];
        System.out.println(randomMove);
        return game.applyMove(randomMove);
    }
}
