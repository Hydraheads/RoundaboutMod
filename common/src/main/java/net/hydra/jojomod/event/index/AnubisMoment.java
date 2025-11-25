package net.hydra.jojomod.event.index;

public class AnubisMoment {


    public static final byte MOUSE_MOVEMENT = (byte) 0;
    public static final byte KEY_PRESS = (byte) 1;
    public static final byte MOUSE_PRESS = (byte) 2;

    byte type;
    int time;
    Object[] vargs;
    public AnubisMoment(byte type, int time, Object[] vargs) {
        this.type = type;
        this.time = time;
        this.vargs = vargs;
    }
}
