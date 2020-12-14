package com.github.slfotg.chess;

import com.github.slfotg.chess.enums.CastlingRights;
import com.github.slfotg.chess.enums.Position;
import com.github.slfotg.chess.enums.Color;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Builder
@Getter
public class GameState {

    @NonNull
    private final Color activeColor;
    private final Position enPassant;
    private final int halfMoveClock;
    private final int fullMoveNumber;
    @NonNull
    private final Board board;
    @NonNull
    private final CastlingRights whiteRights;
    @NonNull
    private final CastlingRights blackRights;
}
