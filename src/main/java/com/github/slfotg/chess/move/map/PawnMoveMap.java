package com.github.slfotg.chess.move.map;

import java.util.Collection;

import com.github.slfotg.chess.enums.Position;
import com.github.slfotg.chess.move.ChessMove;

public class PawnMoveMap extends DelegateImmutableMap<Position, Collection<ChessMove>> {

    public PawnMoveMap() {
        // TODO - implement pawn moves
        super(null);
    }

}
