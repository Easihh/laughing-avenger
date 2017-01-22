package com.zelda.common;

import java.util.HashMap;

public class Constants {
    public static final HashMap<Integer, Integer> messageTypeLengthMap = new HashMap<Integer, Integer>();
    public static final int BUFFER_READ_LENGTH = 256;
    public static final int LONG_BYTE_LENGTH = 8;
    public static final int SERVER_LOOP_DELAY = 100;
    public static final int CLIENT_MOVEMENT_DELAY_TO_SERVER = 100;
    
    static {// Type->Total Message Length
        messageTypeLengthMap.put(MessageType.MOVEMENT, MessageLength.MOVEMENT_LENGTH);
        messageTypeLengthMap.put(MessageType.HERO_ID, MessageLength.HERO_IDEN_LENGTH);
        messageTypeLengthMap.put(MessageType.POSITION, MessageLength.POSITION_LENGTH);
        messageTypeLengthMap.put(MessageType.OBJ_REMOVAL, MessageLength.OBJ_REMOVAL_LENGTH);
        messageTypeLengthMap.put(MessageType.HERO_MAIN_ATTACK, MessageLength.HERO_MAIN_ATTACK_LENGTH);
    }
    
    public static class Size {
        public static int MAX_TILE_HEIGHT = 32;
        public static int MAX_TILE_WIDTH = 32;
        public static int WORLD_SCALE_X = 10;
        public static int WORLD_SCALE_Y = 10;
    }

    public static class MessageType {
        public static final int MOVEMENT = 1;
        public static final int POSITION = 2;
        public static final int OBJ_REMOVAL = 3;
        public static final int HERO_MAIN_ATTACK = 4;
        public static final int LENGTH = 4;
        public static final int HERO_ID = 9999;
    }
    
    public static class Command {
        public static final String MOV_LEFT = "0001L";
        public static final String MOV_RIGHT = "0001R";
        public static final String MOV_UP = "0001U";
        public static final String MOV_DOWN = "0001D";
        public static final String MAIN_SWD_ATTACK = "0004";
    }

    public static class Direction {
        public static final int UP = 0;
        public static final int DOWN = 1;
        public static final int LEFT = 2;
        public static final int RIGHT = 3;
    }

    public static class ServerMessageType {
        public static final int MOVEMENT = 1;
        public static final int HERO_ATTACK = 2;
    }

    public static class Movement {
        public static final String LEFT = "L";
        public static final String RIGHT = "R";
        public static final String UP = "U";
        public static final String DOWN = "D";
    }

    public static class ObjectType {
        public static final String HERO = "HE";
        public static final String TILE = "TI";
        public static final String NORMAL_SWORD = "SW";
    }

    public static class ObjectState {
        public static final int INACTIVE = 0;
        public static final int NORMAL = 1;
        public static final int ATTACKING = 2;
    }

    public static class MessageLength {
        public static final int OBJ_STR_TYPE_LENGTH = 2;
        public static final int OBJ_INT_IDENTIFIER_LENGTH = 4;
        public static final int OBJ_INT_STATE_LENGTH = 4;
        public static final int MOVEMENT_DIRECTION = 1;
        public static final int MOVEMENT_LENGTH = MessageType.LENGTH + MOVEMENT_DIRECTION;
        public static final int POSITION_X = 4;
        public static final int POSITION_Y = 4;
        public static final int POSITION_LENGTH = MessageType.LENGTH + OBJ_STR_TYPE_LENGTH + OBJ_INT_IDENTIFIER_LENGTH
                        + POSITION_X + POSITION_Y + MOVEMENT_DIRECTION + OBJ_INT_STATE_LENGTH;
        public static final int HERO_IDEN_LENGTH = MessageType.LENGTH + MessageLength.OBJ_STR_TYPE_LENGTH
                        + MessageLength.OBJ_INT_IDENTIFIER_LENGTH;
        public static final int OBJ_REMOVAL_LENGTH = MessageType.LENGTH + MessageLength.OBJ_STR_TYPE_LENGTH
                        + MessageLength.OBJ_INT_IDENTIFIER_LENGTH;
        public static final int HERO_MAIN_ATTACK_LENGTH = MessageType.LENGTH;
    }

}