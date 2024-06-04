package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IBlockEntityAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntity.class)
public class ZBlockEntity implements IBlockEntityAccess {
    private boolean roundaboutTimeInteracted = false;
    @Override
    public void setRoundaboutTimeInteracted(boolean roundaboutTimeInteracted) {
        this.roundaboutTimeInteracted = roundaboutTimeInteracted;
    }
    @Override
    public boolean getRoundaboutTimeInteracted() {
        return this.roundaboutTimeInteracted;
    }
}
