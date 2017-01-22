package com.zelda.game;

import com.zelda.common.network.PositionMessage;
import com.zelda.common.Constants.Size;
public class PositionUpdater {

    private int serverX;
    private int serverY;
    private int direction;
    private int state;

    public PositionUpdater(PositionMessage pMessage) {
        int dir = GameUtility.directiontToInt(pMessage.getDirection());
        serverX = pMessage.getX() * Size.WORLD_SCALE_X;;
        serverY = pMessage.getY() * Size.WORLD_SCALE_Y;
        direction = dir;
        state = pMessage.getObjState();
    }

    public int getserverX() {
        return serverX;
    }

    public int getserverY() {
        return serverY;
    }
    
    public int getDirection() {
        return direction;
    }
    
    public int getState() {
        return state;
    }
}
