package net.hydra.jojomod.entity.visages.mobs;

import net.hydra.jojomod.access.NoVibrationEntity;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.StandUsingNPC;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ShizukaNPC extends StandUsingNPC {
    public ShizukaNPC(EntityType<? extends JojoNPC> p_35384_, Level p_35385_) {
        super(p_35384_, p_35385_);
    }

    public ItemStack getBasis(){
        return ModItems.SHIZUKA_MASK.getDefaultInstance();
    }
    @Override
    public StandDiscItem getDisc(){
        return ((StandDiscItem) ModItems.STAND_DISC_ACHTUNG);
    }
}
