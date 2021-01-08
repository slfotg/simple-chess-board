package com.github.slfotg.chess;

import java.util.Random;

import com.github.slfotg.chess.writer.BoardWriter;

public class RandomChessGame {

    private static Random random = new Random();

    public static void main(String... args) {
        BoardWriter writer = new BoardWriter();
        ChessGame game;
        for (game = ChessGame.newGame(); game.getResult().isEmpty(); game = nextRandomGame(game)) {
            writer.writeBoard(game.getBoard());
            System.out.println("\n");
        }
        writer.writeBoard(game.getBoard());
        System.out.println(game.getResult().get());
    }

    static ChessGame nextRandomGame(ChessGame game) {
        ChessGame[] nextGame = game.nextPossibleChessGames().toArray(new ChessGame[0]);
        return nextGame[random.nextInt(nextGame.length)];
    }
}
