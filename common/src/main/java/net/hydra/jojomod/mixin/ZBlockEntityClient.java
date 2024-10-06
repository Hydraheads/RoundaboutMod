package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IBlockEntityClientAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntity.class)
public class ZBlockEntityClient implements IBlockEntityClientAccess {
    /**Store extra data on block entities*/

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
