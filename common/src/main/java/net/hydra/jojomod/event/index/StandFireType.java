package net.hydra.jojomod.event.index;

import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.world.level.block.Block;

public enum StandFireType {
    FIRELESS((byte) 0),
    ORANGE((byte) 1),
    BLUE((byte) 2),
    PURPLE((byte) 3),
    GREEN((byte) 4),
    DREAD((byte) 5);

    public static Block getFireBlockByType(byte Ftype){
        if (Ftype == BLUE.id){
            return ModBlocks.BLUE_FIRE;
        } else if (Ftype == PURPLE.id){
            return ModBlocks.PURPLE_FIRE;
        } else if (Ftype == GREEN.id){
            return ModBlocks.GREEN_FIRE;
        } else if (Ftype == DREAD.id){
            return ModBlocks.DREAD_FIRE;
        }
        return ModBlocks.ORANGE_FIRE;
    }

    public final byte id;
    StandFireType(byte b) {
        this.id = b;
    }
}
