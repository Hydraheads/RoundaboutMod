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
    public static final byte SUMMON = (byte) 8;
    public static final byte DASH = (byte) 9;
    public static final byte ATTACK = (byte) 10;
    public static final byte INTERACT = (byte) 11;




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
}
