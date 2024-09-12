package net.hydra.jojomod.event.index;

public class PacketDataIndex {
    /**Contexts for packets to interpret*/
    public static final byte NONE = 0;
    public static final byte PLAY_SOUND_C2S_CONTEXT = 0;



    public static final byte FLOAT_VELOCITY_BARBED_WIRE = 1;

    /**Int packets*/
    public static final byte INT_GLAIVE_TARGET = 1;
    public static final byte INT_TS_TIME = 2;
    public static final byte INT_RIDE_TICKS = 3;

    /**Single Byte packets*/
    public static final byte SINGLE_BYTE_GLAIVE_START_SOUND = 1;
    public static final byte SINGLE_BYTE_GLAIVE_STOP_SOUND = 2;
}
