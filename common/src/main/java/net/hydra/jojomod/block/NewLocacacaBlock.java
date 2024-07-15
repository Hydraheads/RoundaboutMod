package net.hydra.jojomod.block;

import net.hydra.jojomod.item.ModItems;
import net.minecraft.world.level.ItemLike;

public class NewLocacacaBlock extends LocacacaBlock{
    public NewLocacacaBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ItemLike getFruitType(){
        return ModItems.NEW_LOCACACA;
    }
}
