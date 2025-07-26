package net.hydra.jojomod.mixin.time_stop;

import net.hydra.jojomod.access.IBlockEntityClientAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntity.class)
public class TimeStopBlockEntityClient implements IBlockEntityClientAccess {
    /**A client only mixin that stores the progress into a tick, so that when time is stopped, the block entity
     * (such as a chest) can freeze without jittering and jumping to an earlier or later state*/

    @Unique
    private float roundabout$PrevTick;

    @Override
    public float roundabout$getPreTSTick() {
        return this.roundabout$PrevTick;
    }

    @Override
    public void roundabout$setPreTSTick() {
        Minecraft mc = Minecraft.getInstance();
        roundabout$PrevTick = mc.getFrameTime();
    }
    @Override
    public void roundabout$resetPreTSTick() {
        roundabout$PrevTick = 0;
    }

}
