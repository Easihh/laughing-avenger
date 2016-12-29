package com.zelda.game;

import com.zelda.common.Constants;

public class GameUtility {

    public static int directiontToInt(String movement) {
        switch (movement) {
        case Constants.Movement.LEFT:
            return Constants.Direction.LEFT;
        case Constants.Movement.RIGHT:
            return Constants.Direction.RIGHT;
        case Constants.Movement.UP:
            return Constants.Direction.UP;
        case Constants.Movement.DOWN:
            return Constants.Direction.DOWN;
        default:
            return -1;
        }
    }
}
