package net.hydra.jojomod.block;

import net.hydra.jojomod.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ChamomilePlantBlock
        extends PlantBlock {

    public ChamomilePlantBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }


    @Override
    public Item getDrop() {
        return ModItems.CHAMOMILE;
    }
    @Override
    public Item getPlant() {return ModItems.CHAMOMILE_SEEDS;}

}
