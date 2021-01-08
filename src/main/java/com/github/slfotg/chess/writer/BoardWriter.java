package com.github.slfotg.chess.writer;

import static com.github.slfotg.chess.enums.Piece.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.StringJoiner;

import com.github.slfotg.chess.Board;
import com.github.slfotg.chess.enums.Piece;
import com.github.slfotg.chess.enums.Position;

public class BoardWriter {

    // @formatter:off
    private static final boolean DRAW_DIVIDER = false;

    private static final String UPPER_BLOCK  = "▀▀▀";
    private static final String LOWER_BLOCK  = "▄▄▄";
    private static final String PADDING      = "   ";
    private static final String RANK_PADDING = " %d ";
    private static final String CELL         = " %s ";

    private static final String ANSI_RESET     = "\u001B[0m";
    private static final String ANSI_ITALIC    = "\u001B[3m";
    private static final String ANSI_BLACK     = "\u001B[38;5;16m";
    private static final String ANSI_FG_GREEN  = "\u001B[38;5;34m";
    private static final String ANSI_FG_YELLOW = "\u001B[38;5;220m";
    private static final String ANSI_BG_GREEN  = "\u001B[48;5;34m";
    private static final String ANSI_BG_YELLOW = "\u001B[48;5;220m";
    private static final String[][] FILES = new String[][] {
        new String[] { "A", "B" },
        new String[] { "C", "D" },
        new String[] { "E", "F" },
        new String[] { "G", "H" },
    };
    private static final int[][] RANKS = new int[][] {
        new int[] { 8, 7 },
        new int[] { 6, 5 },
        new int[] { 4, 3 },
        new int[] { 2, 1 },
    };
    private static final Map<Piece, String> BLACK_PIECE_MAP;
    private static final Map<Piece, String> WHITE_PIECE_MAP;
    static {
        BLACK_PIECE_MAP = new EnumMap<>(Piece.class);
        BLACK_PIECE_MAP.put(KING,   "♚");
        BLACK_PIECE_MAP.put(QUEEN,  "♛");
        BLACK_PIECE_MAP.put(ROOK,   "♜");
        BLACK_PIECE_MAP.put(BISHOP, "♝");
        BLACK_PIECE_MAP.put(KNIGHT, "♞");
        BLACK_PIECE_MAP.put(PAWN,   "♟︎");
        WHITE_PIECE_MAP = new EnumMap<>(Piece.class);
        WHITE_PIECE_MAP.put(KING,   "♔");
        WHITE_PIECE_MAP.put(QUEEN,  "♕");
        WHITE_PIECE_MAP.put(ROOK,   "♖");
        WHITE_PIECE_MAP.put(BISHOP, "♗");
        WHITE_PIECE_MAP.put(KNIGHT, "♘");
        WHITE_PIECE_MAP.put(PAWN,   "♙");
    }
    // @formatter:on

    public void writeBoard(Board board) {
        StringJoiner joiner = new StringJoiner("\n");
        if (DRAW_DIVIDER) {
            joiner.add(upper(ANSI_FG_YELLOW, ANSI_FG_GREEN));
        }
        StringJoiner rankJoiner = new StringJoiner(
                DRAW_DIVIDER ? "\n" + divider(ANSI_FG_YELLOW, ANSI_BG_GREEN) + "\n" : "\n");
        for (int[] ranks : RANKS) {
            StringJoiner rowJoiner = new StringJoiner("\n");
            rowJoiner.add(rank(ranks[0], board, ANSI_BG_YELLOW, ANSI_BG_GREEN));
            if (DRAW_DIVIDER) {
                rowJoiner.add(divider(ANSI_FG_GREEN, ANSI_BG_YELLOW));
            }
            rowJoiner.add(rank(ranks[1], board, ANSI_BG_GREEN, ANSI_BG_YELLOW));
            rankJoiner.add(rowJoiner.toString());
        }
        joiner.add(rankJoiner.toString());
        joiner.add(lower(ANSI_FG_YELLOW, ANSI_FG_GREEN));
        System.out.println(joiner.toString());
    }

    private String upper(String whiteCellForeground, String blackCellForeground) {
        StringBuilder builder = new StringBuilder(ANSI_RESET);
        builder.append(PADDING);
        for (int i = 0; i < 4; i += 1) {
            builder.append(whiteCellForeground);
            builder.append(LOWER_BLOCK);
            builder.append(blackCellForeground);
            builder.append(LOWER_BLOCK);
        }
        builder.append(ANSI_RESET);
        return builder.toString();
    }

    private String lower(String whiteCellForeground, String blackCellForeground) {
        StringBuilder builder = new StringBuilder(ANSI_RESET);
        if (DRAW_DIVIDER) {
            builder.append(PADDING);
            for (int i = 0; i < 4; i += 1) {
                builder.append(blackCellForeground);
                builder.append(UPPER_BLOCK);
                builder.append(whiteCellForeground);
                builder.append(UPPER_BLOCK);
            }
            builder.append(ANSI_RESET);
            builder.append("\n");
        }
        builder.append(ANSI_ITALIC);
        builder.append(PADDING);
        for (String[] files : FILES) {
            for (String file : files) {
                builder.append(String.format(CELL, file));
            }
        }
        builder.append(ANSI_RESET);
        return builder.toString();
    }

    private String divider(String foreground, String background) {
        StringBuilder builder = new StringBuilder(ANSI_RESET);
        builder.append(PADDING);
        builder.append(foreground);
        builder.append(background);
        for (int i = 0; i < 4; i += 1) {
            builder.append(LOWER_BLOCK);
            builder.append(UPPER_BLOCK);
        }
        builder.append(ANSI_RESET);
        return builder.toString();
    }

    private String rank(int rank, Board board, String firstCellBackground, String secondCellBackground) {
        StringBuilder builder = new StringBuilder(ANSI_RESET);
        builder.append(ANSI_ITALIC);
        builder.append(String.format(RANK_PADDING, rank));
        builder.append(ANSI_RESET);
        for (int i = 0; i < 4; i += 1) {
            builder.append(firstCellBackground);
            builder.append(String.format(CELL, getPiece(FILES[i][0] + rank, board)));
            builder.append(secondCellBackground);
            builder.append(String.format(CELL, getPiece(FILES[i][1] + rank, board)));
        }
        builder.append(ANSI_RESET);
        return builder.toString();
    }

    private String getPiece(String positionString, Board board) {
        Position position = Position.valueOf(positionString);
        var currentPieces = board.getCurrentPieces();
        if (currentPieces.containsKey(position)) {
            return ANSI_BLACK + WHITE_PIECE_MAP.get(currentPieces.get(position));
        }
        var opponentPieces = board.getOpponentPieces();
        if (opponentPieces.containsKey(position)) {
            return ANSI_BLACK + BLACK_PIECE_MAP.get(opponentPieces.get(position));
        }
        var king = board.getKingPosition();
        if (king == position) {
            return ANSI_BLACK + WHITE_PIECE_MAP.get(KING);
        }
        var opponentKing = board.getOpponentKingPosition();
        if (opponentKing == position) {
            return ANSI_BLACK + BLACK_PIECE_MAP.get(KING);
        }
        return " ";
    }
}
