package net.hydra.jojomod.event.index;

import net.minecraft.world.item.Item;

public class AnubisMemory {

    public static final byte INPUT_ONLY = (byte)0;
    public static final byte DELTA_MOUSE = (byte)1;
    public static final byte MOUSE = (byte) 2;



    public Item item;
    public byte type = (byte)0;
    public AnubisMoment[] moments;
    public AnubisMemory(Item item, byte type, AnubisMoment[] moments) {
        this.item = item;
        this.type = type;
        this.moments = moments;
    }
}
