package net.hydra.jojomod.event.index;

public enum StandFireType {
    FIRELESS((byte) 0),
    ORANGE((byte) 1),
    BLUE((byte) 2),
    DREAD((byte) 3);


    public final byte id;
    StandFireType(byte b) {
        this.id = b;
    }
}
