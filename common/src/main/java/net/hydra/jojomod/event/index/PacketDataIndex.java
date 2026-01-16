package net.hydra.jojomod.event.index;

import net.hydra.jojomod.networking.ModPacketHandler;

public class PacketDataIndex {
    /**Contexts for packets to interpret*/
    public static final byte NONE = 0;
    public static final byte PLAY_SOUND_C2S_CONTEXT = 0;



    public static final byte FLOAT_VELOCITY_BARBED_WIRE = 1;
    public static final byte FLOAT_STAR_FINGER_SIZE = 2;
    public static final byte FLOAT_DISTANCE_OUT = 3;
    public static final byte FLOAT_SIZE_PERCENT = 4;
    public static final byte FLOAT_IDLE_ROTATION = 5;
    public static final byte FLOAT_IDLE_Y_OFFSET = 6;
    public static final byte FLOAT_UPDATE_STAND_MOVE = 7;
    public static final byte FLOAT_BIG_JUMP = 8;
    public static final byte FLOAT_BIG_JUMP_CANCEL = 9;


    /**C2S Inventory*/
    public static final byte ADD_FOG_ITEM = 1;
    public static final byte DROP_FOG_ITEM = 2;
    public static final byte ITEM_SWITCH_MAIN = 4;
    public static final byte ITEM_SWITCH_SECONDARY = 5;
    public static final byte FOG_CHECK = 6;


    /**Int packets*/
    public static final byte INT_GLAIVE_TARGET = 1;
    public static final byte INT_TS_TIME = 2;
    public static final byte INT_RIDE_TICKS = 3;
    public static final byte INT_STAND_ATTACK = 4;
    public static final byte INT_STAND_ATTACK_2 = 5;
    public static final byte INT_ANCHOR_PLACE = 6;
    public static final byte INT_UPDATE_MOVE = 7;
    public static final byte INT_UPDATE_PILOT = 8;
    public static final byte INT_ANCHOR_PLACE_ATTACK = 9;
    public static final byte INT_INDEX_OF_VISAGE_LEVEL = 10;
    public static final byte INT_INDEX_OF_VISAGE_EMERALDS = 11;
    public static final byte INT_RELLA_START = 12;
    public static final byte INT_RELLA_CANCEL = 13;
    public static final byte INT_CURRENT_ITEM = 14;
    public static final byte INT_GRAVITY_FLIP = 15;
    public static final byte INT_GRAVITY_FLIP_2 = 16;
    public static final byte INT_GRAVITY_FLIP_3 = 17;
    public static final byte INT_GRAVITY_FLIP_4 = 18;
    public static final byte INT_VAMPIRE_SKILL_BUY = 19;

    /**Single Byte packets*/
    public static final byte SINGLE_BYTE_GLAIVE_START_SOUND = 1;
    public static final byte SINGLE_BYTE_ITEM_STOP_SOUND = 2;
    public static final byte SINGLE_BYTE_STAND_ARROW_START_SOUND = 3;
    public static final byte SINGLE_BYTE_SCOPE = 4;
    public static final byte SINGLE_BYTE_SCOPE_OFF = 5;
    public static final byte SINGLE_BYTE_FORWARD_BARRAGE = 6;
    public static final byte SINGLE_BYTE_SILENT_SUMMON = 7;
    public static final byte SINGLE_BYTE_OPEN_POWER_INVENTORY = 9;
    public static final byte SINGLE_BYTE_SKIN_LEFT = 10;
    public static final byte SINGLE_BYTE_SKIN_RIGHT = 11;
    public static final byte SINGLE_BYTE_IDLE_LEFT = 12;
    public static final byte SINGLE_BYTE_IDLE_RIGHT = 13;
    public static final byte SINGLE_BYTE_OPEN_FOG_INVENTORY = 14;
    public static final byte SINGLE_BYTE_SMELT = 15;
    public static final byte SINGLE_BYTE_DESUMMON = 16;
    public static final byte SINGLE_BYTE_SKIN_LEFT_SEALED = 30;
    public static final byte SINGLE_BYTE_SKIN_RIGHT_SEALED = 31;
    public static final byte END_BLOOD_SPEED = 32;
    public static final byte QUERY_STAND_UPDATE = 33;
    public static final byte QUERY_STAND_UPDATE_2 = 34;
    public static final byte QUERY_VAMPIRE_UPDATE = 35;
    public static final byte SINGLE_BYTE_LEFT_POWERS = 36;
    public static final byte SINGLE_BYTE_RIGHT_POWERS = 37;

    /**Byte packets*/
    public static final byte BYTE_CHANGE_MORPH = 6;
    public static final byte BYTE_STRIKE_POSE = 7;
    public static final byte BYTE_UPDATE_COOLDOWN = 8;
    public static final byte BYTE_SUMMON_CORPSE = 9;
    public static final byte BYTE_CORPSE_TACTICS = 10;
    public static final byte BYTE_RESPAWN_STRATEGY = 11;

    /**S2C Simple packets*/
    public static final byte S2C_SIMPLE_GENERATE_POWERS = 1;
    public static final byte S2C_SIMPLE_FREEZE_STAND = 2;
    public static final byte S2C_SIMPLE_SUSPEND_RIGHT_CLICK = 3;
    public static final byte S2C_SIMPLE_CLOSE_THE_RELLA = 4;
    public static final byte S2C_RESPAWN = 5;


    /**S2C Int packets*/
    public static final byte S2C_INT_OXYGEN_TANK = 3;
    public static final byte S2C_INT_GRAB_ITEM = 4;
    public static final byte S2C_INT_ATD = 5;
    public static final byte S2C_POWER_INVENTORY = 6;
    public static final byte S2C_INT_SEAL = 7;
    public static final byte S2C_INT_BUBBLE_FINISH = 8;
    public static final byte S2C_INT_VAMPIRE_SPEED = 9;
    public static final byte S2C_INT_STAND_MODE = 10;
    public static final byte S2C_INT_FLESH_BUD = 11;
    public static final byte S2C_INT_CORNER_CUT = 12;
    public static final byte S2C_INT_COMBO_AMT = 13;
    public static final byte S2C_INT_COMBO_SEC_LEFT = 14;
    public static final byte S2C_INT_FADE = 15;
    public static final byte S2C_INT_FADE_UPDATE = 16;

    /**S2C BUNDLE packets*/
    public static final byte S2C_BUNDLE_POWER_INV = 1;
}
