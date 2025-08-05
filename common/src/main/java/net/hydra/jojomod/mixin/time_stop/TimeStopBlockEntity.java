package net.hydra.jojomod.mixin.time_stop;

import net.hydra.jojomod.access.IBlockEntityAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntity.class)
public class TimeStopBlockEntity implements IBlockEntityAccess {
    /**Make it so that interacting with a chest during time stop unfreezes it, allowing it to be open
     * and closed normally by the person moving in stopped time... ultra specific detail*/

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
