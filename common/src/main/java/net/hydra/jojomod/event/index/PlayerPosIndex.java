package net.hydra.jojomod.event.index;

public class PlayerPosIndex {
    //Pos 1
    public static final byte NONE = 0;
    public static final byte TS_FLOAT = 1;
    public static final byte DODGE_FORWARD = 2;
    public static final byte DODGE_BACKWARD = 3;
    public static final byte SUNLIGHT = 4;

    //Pos 2
    public static final byte NONE_2 = 0;
    public static final byte BLOOD_SUCK = 1;
    public static final byte HAIR_EXTENSION = 3;
    public static final byte GUARD = 4;
    public static final byte BARRAGE_CHARGE = 5;
    public static final byte BARRAGE = 6;
    public static final byte SWEEP_KICK = 7;
    public static final byte HAIR_SPIKE = 8;
    public static final byte HAIR_SPIKE_2 = 9;

    public static final byte SNUBNOSE_AIM = 3;

    public static boolean isHidingHeldItem(byte bt){

        return bt == BLOOD_SUCK;
    }
}
