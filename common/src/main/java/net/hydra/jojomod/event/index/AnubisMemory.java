package net.hydra.jojomod.event.index;

import net.minecraft.world.item.Item;

import java.util.List;

public class AnubisMemory {




    public Item item;
    public List<AnubisMoment> moments;
    public boolean delta_mouse = false;
    public AnubisMemory(Item item, List<AnubisMoment> moments) {
        this.item = item;
        this.moments = moments;
    }
}
