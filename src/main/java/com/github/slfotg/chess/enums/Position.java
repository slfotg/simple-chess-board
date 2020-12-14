package com.github.slfotg.chess.enums;

import static com.github.slfotg.chess.enums.File.*;
import static com.github.slfotg.chess.enums.Rank.*;

public enum Position {

    // @formatter:off
    A8(EIGHT, A), B8(EIGHT, B), C8(EIGHT, C), D8(EIGHT, D), E8(EIGHT, E), F8(EIGHT, F), G8(EIGHT, G), H8(EIGHT, H),
    A7(SEVEN, A), B7(SEVEN, B), C7(SEVEN, C), D7(SEVEN, D), E7(SEVEN, E), F7(SEVEN, F), G7(SEVEN, G), H7(SEVEN, H),
    A6(SIX  , A), B6(SIX  , B), C6(SIX  , C), D6(SIX  , D), E6(SIX  , E), F6(SIX  , F), G6(SIX  , G), H6(SIX  , H),
    A5(FIVE , A), B5(FIVE , B), C5(FIVE , C), D5(FIVE , D), E5(FIVE , E), F5(FIVE , F), G5(FIVE , G), H5(FIVE , H),
    A4(FOUR , A), B4(FOUR , B), C4(FOUR , C), D4(FOUR , D), E4(FOUR , E), F4(FOUR , F), G4(FOUR , G), H4(FOUR , H),
    A3(THREE, A), B3(THREE, B), C3(THREE, C), D3(THREE, D), E3(THREE, E), F3(THREE, F), G3(THREE, G), H3(THREE, H),
    A2(TWO  , A), B2(TWO  , B), C2(TWO  , C), D2(TWO  , D), E2(TWO  , E), F2(TWO  , F), G2(TWO  , G), H2(TWO  , H),
    A1(ONE  , A), B1(ONE  , B), C1(ONE  , C), D1(ONE  , D), E1(ONE  , E), F1(ONE  , F), G1(ONE  , G), H1(ONE  , H);
    // @formatter:on

    private final Rank rank;
    private final File file;

    Position(Rank rank, File file) {
        this.rank = rank;
        this.file = file;
    }

    public Rank getRank() {
        return rank;
    }

    public File getFile() {
        return file;
    }
}
