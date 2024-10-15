package net.hydra.jojomod.event.index;

public class PacketDataIndex {
    /**Contexts for packets to interpret*/
    public static final byte NONE = 0;
    public static final byte PLAY_SOUND_C2S_CONTEXT = 0;



    public static final byte FLOAT_VELOCITY_BARBED_WIRE = 1;
    public static final byte FLOAT_STAR_FINGER_SIZE = 2;

    /**Int packets*/
    public static final byte INT_GLAIVE_TARGET = 1;
    public static final byte INT_TS_TIME = 2;
    public static final byte INT_RIDE_TICKS = 3;
    public static final byte INT_STAND_ATTACK = 4;

    /**Single Byte packets*/
    public static final byte SINGLE_BYTE_GLAIVE_START_SOUND = 1;
    public static final byte SINGLE_BYTE_ITEM_STOP_SOUND = 2;
    public static final byte SINGLE_BYTE_STAND_ARROW_START_SOUND = 3;
    public static final byte SINGLE_BYTE_SCOPE = 4;
    public static final byte SINGLE_BYTE_SCOPE_OFF = 5;

    /**S2C Simple packets*/
    public static final byte S2C_SIMPLE_GENERATE_POWERS = 1;
    public static final byte S2C_SIMPLE_FREEZE_STAND = 2;

    /**S2C Int packets*/
    public static final byte S2C_INT_OXYGEN_TANK = 3;
}
