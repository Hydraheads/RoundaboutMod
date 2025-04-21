package net.hydra.jojomod.event.powers;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class VisageStoreEntry {
    /**
     * The itemstack representing the visage or item.
     */

    public ItemStack stack;

    /**
     * An int representing the page number.
     */
    public int page;
    /**
     * An int representing the level cost.
     */
    public double costL;
    /**
     * An int representing the emerald cost.
     */
    public double costE;


    public VisageStoreEntry(ItemStack stack, int page, int costL, int costE){
        this.stack = stack;
        this.page = page;
        this.costL = costL;
        this.costE = costE;
    }
}
