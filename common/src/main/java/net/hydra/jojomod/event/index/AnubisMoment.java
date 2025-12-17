package net.hydra.jojomod.event.index;

public class AnubisMoment {

    public static final byte NONE = (byte) 0;
    public static final byte UP = (byte) 1;
    public static final byte DOWN = (byte) 2;
    public static final byte LEFT = (byte) 3;
    public static final byte RIGHT = (byte) 4;
    public static final byte JUMP = (byte) 5;
    public static final byte SPRINT = (byte) 6;
    public static final byte CROUCH = (byte) 7;
    public static final byte CROUCH_TOGGLE = (byte) 8;
    public static final byte SUMMON = (byte) 9;
    public static final byte ABILITY_1 = (byte) 10;
    public static final byte ABILITY_2 = (byte) 11;
    public static final byte DASH = (byte) 12;
    public static final byte ABILITY_3 = (byte) 13;
    public static final byte ATTACK = (byte) 14;
    public static final byte INTERACT = (byte) 15;
    public static final byte SWAP_OFFHAND = (byte) 16;
    public static final byte USE_OFFHAND = (byte) 17;


    public static final byte[] HOTBAR = {21,22,23,24,25,26,27,28,29};






    public byte type;
    public int time;
    public boolean vargs;

    public AnubisMoment(byte type, int time, boolean vargs) {
        this.type = type;
        this.time = time;
        this.vargs = vargs;
    }
    @Override
    public String toString() {
        String a = "time: " + time + " type: " + type + " vargs: ";
        String b = ""+vargs;

        return a + b + "\n";
    }

    public String toConfig() {
        return type+"_"+time+"_"+(vargs ? 1 : 0)+"_";
    }
}
