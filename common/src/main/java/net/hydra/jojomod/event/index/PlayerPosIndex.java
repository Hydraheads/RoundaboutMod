package net.hydra.jojomod.event.index;

public class PlayerPosIndex {
    //Pos 1
    public static final byte NONE = 0;
    public static final byte TS_FLOAT = 1;
    public static final byte DODGE_FORWARD = 2;
    public static final byte DODGE_BACKWARD = 3;
    public static final byte SUNLIGHT = 4;
    public static final byte SKATE_JUMP = 5;
    public static final byte SKATE_TWIRL = 6;
    public static final byte SKATE_GENERAL = 7;

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
    public static final byte HAIR_EXTENSION_2 = 10;
    public static final byte CLUTCH_WINDUP = 11;
    public static final byte CLUTCH_DASH = 12;
    public static final byte RIPPER_EYES_ACTIVE = 13;
    public static final byte VANISH_START = 14;
    public static final byte VANISH_PERSIST = 15;
    public static final byte CHARGE_SHOT = 16;
    public static final byte OASIS_KICK = 17;
    //Tragically vanilla desyncs your sprinting with client/server every time you hit a mob while sprinting
    //So this is visually necessary for other players to see you skating

    public static final byte SNUBNOSE_AIM = 3;

    public static boolean isHidingHeldItem(byte bt){

        return bt == BLOOD_SUCK;
    }
}
