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
    private float sba$PrevTick;

    @Override
    public float sba$getPreTSTick() {
        return this.sba$PrevTick;
    }

    @Override
    public void sba$setPreTSTick() {
        Minecraft mc = Minecraft.getInstance();
        sba$PrevTick = mc.getFrameTime();
    }
    @Override
    public void sba$resetPreTSTick() {
        sba$PrevTick = 0;
    }

}
