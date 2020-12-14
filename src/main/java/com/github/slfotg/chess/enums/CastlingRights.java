package com.github.slfotg.chess.enums;

public enum CastlingRights {

    NONE,
    KING_SIDE,
    QUEEN_SIDE,
    EITHER_SIDE;
    
    public boolean hasRights(Side side) {
        if (side == Side.QUEEN) {
            return this == QUEEN_SIDE || this == EITHER_SIDE;
        }
        return this == KING_SIDE || this == EITHER_SIDE;
    }

    public CastlingRights removeQueenSideRights() {
        if (this == QUEEN_SIDE) {
            return NONE;
        }
        if (this == EITHER_SIDE) {
            return KING_SIDE;
        }
        return this;
    }

    public CastlingRights removeKingSideRights() {
        if (this == KING_SIDE) {
            return NONE;
        }
        if (this == EITHER_SIDE) {
            return QUEEN_SIDE;
        }
        return this;
    }
}
