package net.hydra.jojomod.event.index;

import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class AnubisMemory {


    public static final byte INPUTS = (byte) 0;
    public static final byte MOUSE = (byte)1;
    public static final byte MOUSE_INVENTORY = (byte)2;


    public Item item;
    public List<AnubisMoment> moments;
    public List<Vec3> rots;
    public byte memory_type = 0;
    public AnubisMemory(Item item, List<AnubisMoment> moments) {
        this.item = item;
        this.moments = moments;
        this.rots = new ArrayList<>();
    }
}
