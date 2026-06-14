package net.hydra.jojomod.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HandItem extends Item {

    static final byte
        PLAYER = 0,
        ILLAGER = 1,
        VILLAGER = 2,
        ROTTEN = 3;

    public byte  type = PLAYER;

    public HandItem(Item.Properties $$0) {
        super($$0);
        this.type = PLAYER;
    }

    public void setHandType(byte type) {
        this.type = type;
    }

}
