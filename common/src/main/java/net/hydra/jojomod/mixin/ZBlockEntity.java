package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IBlockEntityAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntity.class)
public class ZBlockEntity implements IBlockEntityAccess {
    @Unique
    private boolean roundabout$TimeInteracted = false;
    @Override
    public void roundabout$setRoundaboutTimeInteracted(boolean roundaboutTimeInteracted) {
        this.roundabout$TimeInteracted = roundaboutTimeInteracted;
    }
    @Override
    public boolean roundabout$getRoundaboutTimeInteracted() {
        return this.roundabout$TimeInteracted;
    }
}
